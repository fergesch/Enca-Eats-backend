package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.UserDao;
import com.fergesch.encaeats.model.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired
    UserDao userDao;

    Gson gson = new Gson();

    @GetMapping
    public ResponseEntity<String> getUserProfile(@RequestHeader("User-Email") String email) {

        User user = userDao.getUserData(email);
        if (user != null) {
            return new ResponseEntity<>(gson.toJson(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<String> upsertUserProfile(@RequestBody User user) {
        User retreivedUser = userDao.upsertUser(user);
        return new ResponseEntity<>(gson.toJson(retreivedUser), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> updateUserProfile(@RequestBody User user) {
        return new ResponseEntity<>(gson.toJson(userDao.upsertItem(user)), HttpStatus.OK);
    }
}
