package com.fergesch.encaeats.service;

import com.fergesch.encaeats.model.Restaurant;
import com.fergesch.encaeats.repository.RestaurantRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository repository;

    Gson gson = new Gson();

    public String findByName(String name) {
        Iterable<Restaurant> list = repository.findByName(name);
        return gson.toJson(list.iterator().next());
    }
}
