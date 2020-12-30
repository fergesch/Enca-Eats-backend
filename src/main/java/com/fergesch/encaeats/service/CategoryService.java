package com.fergesch.encaeats.service;

import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public ArrayList<Category> getAllCategories() {
        Iterable<Category> resultList = repository.getAllCategories();
        ArrayList<Category> categoryList = new ArrayList<>();
        resultList.forEach(categoryList::add);
        return categoryList;
    }

}
