package com.fergesch.encaeats.dao;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fergesch.encaeats.model.UserInteractions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class UserInteractionsDao extends GenericCosmosDao<UserInteractions> {
    @Value("${azure.cosmos.userInteractions.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(UserInteractions.class, tableName);
    }

    public UserInteractions findUserInteractionsByAlias(Map<String, String> params) {
        List<UserInteractions> queryResults = getFromStringValue(params);
        return queryResults.iterator().hasNext() ? queryResults.iterator().next()
                : new UserInteractions(params.get("email"), params.get("rest_alias"));
    }

    public HashMap<String, UserInteractions> multiGetUserInteractions(String email, List<String> restAliases) {
        HashMap<String, UserInteractions> results = new HashMap<>();
        String aliases = String.join("', '", restAliases);
        String query = "SELECT * FROM c WHERE c.email = '" + email + "' AND c.rest_alias IN ('" + aliases + "')";
        CosmosPagedIterable<UserInteractions> queryResults = container.queryItems(query,
                new CosmosQueryRequestOptions(), this.type);
        queryResults.forEach(result -> {
            results.put(result.getRest_alias(), result);
        });
        return results;
    }
}