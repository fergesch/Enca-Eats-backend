package com.fergesch.encaeats.dao;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class RestaurantDao extends GenericCosmosDao<Restaurant> {

    @Value("${azure.cosmos.restaurants.container}")
    private String tableName;

    @Autowired
    CategoryDao categoryDao;

    public static final String CATEGORIES = "categories";
    public static final String PRICE = "price";
    public static final String RATING = "rating";
    public static final String NEIGHBORHOOD = "neighborhoods";
    public static final String WISH_LIST = "wish_list";
    public static final String VISITED = "visited";

    private static final String[] SEARCH_PARAMS = new String[] { CATEGORIES, PRICE, RATING, NEIGHBORHOOD, WISH_LIST,
            VISITED };

    private static final String[] USER_INTERACTIONS_PARAMS = new String[] { WISH_LIST, VISITED };

    @PostConstruct
    public void init() {
        super.init(Restaurant.class, tableName);
    }

    public String[] getSearchParams() {
        return SEARCH_PARAMS;
    };

    public Restaurant findRestaurantByAlias(String alias) {
        List<Restaurant> queryResults = getFromStringValue("alias", alias);
        return queryResults.iterator().hasNext() ? queryResults.iterator().next() : null;
    }

    public List<Restaurant> fuzzySearchByName(String name) {
        return fuzzyStringSearch("name", name);
    }

    public Set<Restaurant> restaurantSearch(Map<String, String> searchFilters) {
        StringBuilder sql = new StringBuilder("SELECT r.alias, r.name, r.image_url, r.url, r.location, r.neighborhood, "
                + "r.price, r.categories, r.review_count, r.rating " + "FROM restaurants r "
                + "JOIN c in r.categories WHERE not r.is_closed");

        for (String type : SEARCH_PARAMS) {
            String criteria = searchFilters.getOrDefault(type, null);
            if (criteria != null) {
                String criteriaList = criteria.replaceAll("\\s*,\\s*", "','");

                // search by neighborhood
                switch (type) {
                    case NEIGHBORHOOD:
                        sql.append(" AND r.neighborhood IN ( '").append(criteriaList).append("') ");
                        break;
                    // search by PRICE
                    case PRICE:
                        sql.append(" AND r.price IN ( '").append(criteriaList).append("' ) ");
                        break;
                    // search by RATING
                    case RATING:
                        sql.append(" AND r.rating >= ").append(Double.parseDouble(criteria));
                        break;
                    case CATEGORIES:
                        // TODO add check if not valid category
                        String aliasString = categorySearch(criteria);
                        sql.append(" AND c.alias IN (").append(aliasString).append(")");
                        break;
                }
            }
        }

        CosmosPagedIterable<Restaurant> queryResults = container.queryItems(sql.toString(),
                new CosmosQueryRequestOptions(), Restaurant.class);
        Set<Restaurant> results = new HashSet<>();
        queryResults.forEach(results::add);
        return results;
    }

    public String categorySearch(String categories) {
        String[] categoryMultiSelect = categories.split(",");
        ArrayList<String> aliases = new ArrayList<>();
        for (String category : categoryMultiSelect) {
            List<Category> childrenCategories = categoryDao.getCategoryChildren(category.trim());
            childrenCategories.forEach(child -> {
                aliases.add("'" + child.getAlias() + "'");
            });
        }
        return String.join(",", aliases);
    }
}
