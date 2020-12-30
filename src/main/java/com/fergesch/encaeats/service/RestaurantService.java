package com.fergesch.encaeats.service;

import com.fergesch.encaeats.model.Restaurant;
import com.fergesch.encaeats.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository repository;

    public Restaurant findByName(String name) {
        Iterable<Restaurant> resultList = repository.findRestaurantByName(name);
        return resultList.iterator().hasNext()
                ? resultList.iterator().next()
                : null;
    }
}
