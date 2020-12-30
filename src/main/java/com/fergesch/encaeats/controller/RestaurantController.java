package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.model.Restaurant;
import com.fergesch.encaeats.service.RestaurantService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/restaurant")
@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    Gson gson = new Gson();

    @GetMapping("/{restaurantName}")
    @ResponseBody
    public ResponseEntity<String> restaurant(@PathVariable("restaurantName") String restaurantName) {
        Restaurant restaurant = restaurantService.findByName(restaurantName);
        if(restaurant != null) {
            return new ResponseEntity<>(gson.toJson(restaurant), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
