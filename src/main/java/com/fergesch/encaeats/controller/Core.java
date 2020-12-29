package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class Core {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/health")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String health() {
      return "I'm alive";
    }


    @GetMapping("/restaurant/{restaurantName}")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String restaurants(@PathVariable String restaurantName) {
       return restaurantService.findByName(restaurantName);
    }
}
