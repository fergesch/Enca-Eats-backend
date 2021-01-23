package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.UserInteractionsDao;
import com.fergesch.encaeats.model.UserInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/userInteractions")
@Controller
public class UserInteractionsController {

    @Autowired
    UserInteractionsDao userInteractionsDao;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUserInteractions(@RequestBody UserInteractions userInteractions) {
        if(userInteractions.getId() == null) {
            userInteractions.setId(UUID.randomUUID());
        }
        userInteractionsDao.upsertItem(userInteractions);
    }

}
