package com.fergesch.encaeats.model;

import lombok.Data;

import java.util.List;

@Data
public class CategoryHierarchy {
    private String id;
    private String alias;
    private String title;
    private List<Category> children;

}
