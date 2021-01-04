package com.fergesch.encaeats.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.model.Restaurant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RestaurantRepository extends CosmosRepository<Restaurant, String> {

    Streamable<Restaurant> findRestaurantByName(String name);

    Streamable<Restaurant> findByCategories(ArrayList<Category> categories);

    Streamable<Restaurant> findRestaurantsByNeighborhoodEquals(String neighborhood);
    Streamable<Restaurant> findRestaurantsByNeighborhood(ArrayList<String> neighborhood);
    Streamable<Restaurant> findRestaurantsByNeighborhoodIn(ArrayList<String> neighborhood);

    Streamable<Restaurant> findRestaurantsByPriceEquals(String price);

    Streamable<Restaurant> findRestaurantsByRatingEquals(double rating);

    Streamable<Restaurant> findAll();

}
