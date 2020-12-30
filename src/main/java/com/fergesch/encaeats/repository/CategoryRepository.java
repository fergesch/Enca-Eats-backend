package com.fergesch.encaeats.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.fergesch.encaeats.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CosmosRepository<Category, String> {

    @Query(value = "Select * FROM categories")
    Iterable<Category> getAllCategories();
}
