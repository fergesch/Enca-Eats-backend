package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.service.CategoryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/categories")
@Controller
public class CategoryController {

    Gson gson = new Gson();

    @Autowired
    CategoryService categoryService;

    @GetMapping
    @ResponseStatus
    @ResponseBody
    public String getCategories() {
        return gson.toJson(categoryService.getAllCategories());
    }
}
