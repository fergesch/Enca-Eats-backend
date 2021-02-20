package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.RestaurantDao;
import com.fergesch.encaeats.dao.UserInteractionsDao;
import com.fergesch.encaeats.model.Restaurant;
import com.fergesch.encaeats.model.UserInteractions;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/userInteractions")
@Controller
public class UserInteractionsController {

    @Autowired
    UserInteractionsDao userInteractionsDao;

    @Autowired
    RestaurantDao restaurantDao;

    Gson gson = new Gson();

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> updateUserInteractions(@RequestBody UserInteractions userInteractions) {
        if (userInteractions.getId() == null) {
            userInteractions.setId();
        }
        return new ResponseEntity<>(gson.toJson(userInteractionsDao.upsertItem(userInteractions)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getInteractionsForUser(@RequestHeader("User-Email") String email) {
        List<UserInteractions> result = userInteractionsDao.getFromStringValue("email", email);

        List<String> restAlias = new LinkedList<>();
        result.forEach(userInteractions -> {
            restAlias.add(userInteractions.getRest_alias());
        });

        HashMap<String, Restaurant> restaurants = new HashMap<>();
        restaurantDao.multiGet("alias", restAlias).forEach(restaurant -> {
            restaurants.put(restaurant.getAlias(), restaurant);
        });

        List<Restaurant> wishList = new LinkedList<>();
        List<Restaurant> notes = new LinkedList<>();
        List<Restaurant> visited = new LinkedList<>();

        result.forEach(userInteractions -> {
            Restaurant r = restaurants.get(userInteractions.getRest_alias());
            r.setUserInteractions(userInteractions);
            if (userInteractions.checkWishList()) {
                wishList.add(r);
            }
            if (userInteractions.checkVisited()) {
                visited.add(r);
            }
            if (userInteractions.getNotes().size() > 0) {
                notes.add(r);
            }
        });
        Map<String, List<Restaurant>> responseMap = new HashMap<>();
        responseMap.put("wish_list", wishList);
        responseMap.put("notes", notes);
        responseMap.put("visited", visited);

        return new ResponseEntity<>(gson.toJson(responseMap), HttpStatus.OK);
    }

}
