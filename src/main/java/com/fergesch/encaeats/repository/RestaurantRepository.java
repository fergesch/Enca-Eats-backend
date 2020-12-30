package com.fergesch.encaeats.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.fergesch.encaeats.model.Restaurant;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends CosmosRepository<Restaurant, String> {

    @Query(value = "Select * FROM restaurant r WHERE r.name = @name")
    Iterable<Restaurant> findRestaurantByName(String name);

    @Query(value = "Select * FROM restaurant r WHERE @conditions")
    Iterable<Restaurant> findByConditions(String conditions);
}
