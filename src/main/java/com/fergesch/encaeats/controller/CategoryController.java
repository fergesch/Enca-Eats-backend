package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.dao.CategoryDao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value="/categories", produces = "application/json")
@Controller
public class CategoryController {

    Gson gson = new Gson();

    @Autowired
    CategoryDao dao;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String getCategories() {
        return gson.toJson(dao.getAllType());
    }
}
