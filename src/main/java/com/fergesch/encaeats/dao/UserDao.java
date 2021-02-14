package com.fergesch.encaeats.dao;

import com.fergesch.encaeats.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Component
public class UserDao extends GenericCosmosDao<User> {

    @Value("${azure.cosmos.user.container}")
    private String tableName;

    @PostConstruct
    public void init() {
        super.init(User.class, tableName);
    }

    public User getUserData(String email) {
        List<User> queryResults = getFromStringValue("email", email);
        return queryResults.iterator().hasNext() ? queryResults.iterator().next() : null;
    }

    public User upsertUser(User user) {
        List<User> queryResults = getFromStringValue("email", user.getEmail());
        if (queryResults.iterator().hasNext()) {
            return queryResults.iterator().next();
        } else {
            user.setId(UUID.randomUUID().toString());
            return insertItem(user);
        }
    }
}
