package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.CosmosDao;
import com.fergesch.encaeats.model.Restaurant;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RequestMapping("/restaurant")
@Controller
public class RestaurantController {

    @Autowired
    private CosmosDao cosmosDao;

    Gson gson = new Gson();

    @GetMapping
    public ResponseEntity<String> restaurant(@RequestParam(name = "alias") String restaurantAlias) {
        Restaurant restaurant = cosmosDao.findRestaurantByAlias(restaurantAlias);
        if(restaurant != null) {
            return new ResponseEntity<>(gson.toJson(restaurant), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<String> restaurantSearch(
            @RequestParam Map<String, String> searchCriteria) {

        Set<Restaurant> searchResults = cosmosDao.search(searchCriteria);
        if(searchResults.size() > 0) {
            return new ResponseEntity<>(gson.toJson(searchResults), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}


