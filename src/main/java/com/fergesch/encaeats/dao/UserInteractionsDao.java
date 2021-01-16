package com.fergesch.encaeats.dao;

import com.fergesch.encaeats.model.UserInteractions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class UserInteractionsDao extends GenericCosmosDao<UserInteractions>{
    @Value("${azure.cosmos.userInteractions.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(UserInteractions.class, tableName);
    }

    public UserInteractions findUserInteractionsByAlias(Map<String, String> params) {
        List<UserInteractions> queryResults = getFromStringValue(params);
        return queryResults.iterator().hasNext()
                ? queryResults.iterator().next()
                : new UserInteractions();
    }
}