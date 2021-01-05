package com.fergesch.encaeats.dao;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.model.CategoryHierarchy;
import com.fergesch.encaeats.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CosmosDao {

    @Value("${azure.cosmos.restaurants.container}")
    private String restaurantDbName;

    @Value("${azure.cosmos.categoryHierarchy.container}")
    private String categoryHierarchyDbName;

    @Autowired
    private CosmosDatabase cosmosDatabase;

    private CosmosContainer categoryContainer;
    private CosmosContainer restaurantContainer;

    private static final String CATEGORIES = "categories";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String NEIGHBORHOOD = "neighborhood";

    private static final String[] SEARCH_PARAMS =
            new String[]{CATEGORIES, PRICE, RATING, NEIGHBORHOOD};

    @PostConstruct
    public void init() {
        restaurantContainer = cosmosDatabase.getContainer(restaurantDbName);
        categoryContainer = cosmosDatabase.getContainer(categoryHierarchyDbName);
    }

    public Restaurant findRestaurantByAlias(String alias) {
        String sql = "SELECT * FROM c WHERE c.alias = '" + alias + "'";
        CosmosPagedIterable<Restaurant> queryResults =
                restaurantContainer.queryItems(sql, new CosmosQueryRequestOptions(), Restaurant.class);
        return queryResults.iterator().hasNext()
                ? queryResults.iterator().next()
                : null;
    }

    public Set<Restaurant> search(Map<String, String> searchFilters) {
        Set<Restaurant> results = new HashSet<>();
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
                        List<Category> childrenCategories = getCategoryChildren(criteria);
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
                restaurantContainer.queryItems(sql.toString(), new CosmosQueryRequestOptions(), Restaurant.class);
        queryResults.forEach(results::add);
        return results;
    }

    public List<Category> getCategoryChildren(String alias) {
        String sql = "SELECT c.children FROM category_hierarchy c WHERE c.alias = '" + alias + "'";
        CosmosPagedIterable<CategoryHierarchy> queryResults =
                categoryContainer.queryItems(sql, new CosmosQueryRequestOptions(), CategoryHierarchy.class);
        return queryResults.iterator().next().getChildren();
    }

    public List<CategoryHierarchy> getAllCategories() {
        String sql = "SELECT * from category_hierarchy c";
        ArrayList<CategoryHierarchy> results = new ArrayList<>();
        CosmosPagedIterable<CategoryHierarchy> queryResults =
                categoryContainer.queryItems(sql, new CosmosQueryRequestOptions(), CategoryHierarchy.class);
        queryResults.forEach(results::add);
        return results;
    }

}
