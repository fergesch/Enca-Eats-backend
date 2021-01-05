package com.fergesch.encaeats.dao;

import com.fergesch.encaeats.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class UserDao extends GenericCosmosDao<User> {

    @Value("${azure.cosmos.user.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(User.class, tableName);
    }

    public User getUserData(String id) {
        List<User> queryResults = getFromStringValue("id", id);
        return queryResults.iterator().hasNext()
                ? queryResults.iterator().next()
                : null;
    }
}
