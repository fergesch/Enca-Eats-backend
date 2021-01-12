package com.fergesch.encaeats.dao;

import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.model.CategoryHierarchy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CategoryDao extends GenericCosmosDao<CategoryHierarchy> {
    @Value("${azure.cosmos.categoryHierarchy.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(CategoryHierarchy.class, tableName);
    }

    public List<Category> getCategoryChildren(String alias) {
        List<CategoryHierarchy> results = getFromStringValue("alias", alias);
        return results.get(0).getChildren();
    }
}
