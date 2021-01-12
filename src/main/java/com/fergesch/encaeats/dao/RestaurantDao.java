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

    private static final String CATEGORIES = "categories";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String NEIGHBORHOOD = "neighborhood";

    private static final String[] SEARCH_PARAMS =
            new String[]{CATEGORIES, PRICE, RATING, NEIGHBORHOOD};

    @PostConstruct
    public void init() {
       super.init(Restaurant.class, tableName);
    }

    public String[] getSearchParams() { return SEARCH_PARAMS; };

    public Restaurant findRestaurantByAlias(String alias) {
        List<Restaurant> queryResults = getFromStringValue("alias", alias);
        return queryResults.iterator().hasNext()
                ? queryResults.iterator().next()
                : null;
    }

    public Set<Restaurant> restaurantSearch(Map<String, String> searchFilters) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.alias, r.name, r.image_url, r.url, r.location, r.neighborhood, " +
                        "r.price, r.categories, r.review_count, r.rating " +
                "FROM restaurants r " +
                "JOIN c in r.categories WHERE not r.is_closed");

        for(String type : SEARCH_PARAMS) {
            String criteria = searchFilters.getOrDefault(type, null);
            if(criteria != null) {
                //search by neighborhood
                switch (type) {
                    case NEIGHBORHOOD:
                        sql.append(" AND r.neighborhood = '").append(criteria).append("'");
                        break;
                    //search by PRICE
                    case PRICE:
                        sql.append(" AND r.price = '").append(criteria).append("'");
                        break;
                    //search by RATING
                    case RATING:
                        sql.append(" AND r.rating >= ").append(Double.parseDouble(criteria));
                        break;
                    case CATEGORIES:
                        // TODO add check if not valid category
                        List<Category> childrenCategories = categoryDao.getCategoryChildren(criteria);
                        ArrayList<String> aliases = new ArrayList<>();
                        childrenCategories.forEach(child -> {
                            aliases.add("'" + child.getAlias() + "'");
                        });
                        String aliasString = String.join(",", aliases);
                        sql.append(" AND c.alias IN (").append(aliasString).append(")");
                        break;
                }
            }
        }

        CosmosPagedIterable<Restaurant> queryResults =
                container.queryItems(sql.toString(), new CosmosQueryRequestOptions(), Restaurant.class);
        Set<Restaurant> results = new HashSet<>();
        queryResults.forEach(results::add);
        return results;
    }
}
