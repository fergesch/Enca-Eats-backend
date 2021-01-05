package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.NeighborhoodDao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/neighborhood")
public class NeighborhoodController {

    @Autowired
    NeighborhoodDao neighborhoodDao;

    Gson gson = new Gson();

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String getNeighborhoods() {
        return gson.toJson(neighborhoodDao.getAllType());
    }
}
