package com.fergesch.encaeats.service;

import com.fergesch.encaeats.model.Restaurant;
import com.fergesch.encaeats.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository repository;

    private static final String CATEGORIES = "categories";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String NEIGHBORHOOD = "neighborhood";

    private static final String[] SEARCH_PARAMS =
            new String[]{CATEGORIES, PRICE, RATING, NEIGHBORHOOD};

    public Restaurant findByName(String name) {
        Iterable<Restaurant> resultList = repository.findRestaurantByName(name);
        return resultList.iterator().hasNext()
                ? resultList.iterator().next()
                : null;
    }

    //need to deal with deduplication
    public Set<Restaurant> search(Map<String, String> searchFilters) {

        //get all open restaurants
        Streamable<Restaurant> results = repository.findAll();

        for(String type : SEARCH_PARAMS) {
            Object criteria = searchFilters.getOrDefault(type, null);
            if(criteria != null) {
                //search by neighborhood
                switch (type) {
                    case NEIGHBORHOOD:
                        results = results.and(repository.findRestaurantsByNeighborhoodEquals(criteria.toString()));
                        break;
                    //search by PRICE
                    case PRICE:
                        results = results.and(repository.findRestaurantsByPriceEquals(criteria.toString()));
                        break;
                    //search by RATING
                    case RATING:
                        results = results.and(repository.findRestaurantsByRatingEquals(Double.parseDouble(criteria.toString())));
                        break;
                    case CATEGORIES:
                        //need to fetch categories and then do a search
                        break;
                }
            }
        }
        return results.toSet();
    }
}
