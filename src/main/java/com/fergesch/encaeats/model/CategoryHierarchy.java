package com.fergesch.encaeats.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;

import java.util.List;

@Data
@Container(containerName = "category_hierarchy")
public class CategoryHierarchy {
    private String id;
    private String alias;
    private String title;
    private List<Category> children;

}
