package com.fergesch.encaeats.dao;

import com.fergesch.encaeats.model.Neighborhood;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NeighborhoodDao extends GenericCosmosDao<Neighborhood>{

    @Value("${azure.cosmos.neighborhood.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(Neighborhood.class, tableName);
    }
}
