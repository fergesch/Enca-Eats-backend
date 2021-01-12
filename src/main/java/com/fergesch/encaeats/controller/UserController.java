package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.UserDao;
import com.fergesch.encaeats.model.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    Gson gson = new Gson();

    @GetMapping
    public ResponseEntity<String> getUserProfile(@RequestParam String id) {

        User user = userDao.getUserData(id);
        if(user != null) {
            return new ResponseEntity<>(gson.toJson(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
