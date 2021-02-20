package com.fergesch.encaeats.controller;

import java.util.HashMap;
import java.util.UUID;

import com.fergesch.encaeats.dao.UserDao;
import com.fergesch.encaeats.model.User;
import com.google.gson.Gson;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsBlankString.blankOrNullString;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    Gson gson = new Gson();

    private static final String TEST_USER = "test@gmail.com";

    private static final String OTHER_USER = "test2@gmail.com";

    private static final String NON_EXISTANT = "non-existant@gmail.com";

    private static User testUser;

    @MockBean
    private UserDao userDao;

    static HashMap<String, User> repository = new HashMap<>();

    @BeforeAll
    public static void insertTestUser() {
        testUser = User.builder().email(TEST_USER).id(UUID.randomUUID().toString()).build();
        repository.put(TEST_USER, testUser);
    }

    @AfterAll
    public static void cleanupUsers() {
        // repository.remove(TEST_USER);
        repository.clear();
    }

    @Test
    public void getUser() {
        Mockito.when(userDao.getUserData(TEST_USER)).thenReturn(repository.get(TEST_USER));
        ResponseEntity<String> response = getRequest("", TEST_USER);
        assertThat("Failed to get user", response.getStatusCode(), equalTo(HttpStatus.OK));
        User user = gson.fromJson(response.getBody(), User.class);
        assertThat("Mis match email", user.getEmail(), equalTo(TEST_USER));
    }

    @Test
    public void userNotFound() {
        Mockito.when(userDao.getUserData(NON_EXISTANT)).thenReturn(repository.get(NON_EXISTANT));
        ResponseEntity<String> response = getRequest("", NON_EXISTANT);
        assertThat("Wrong status", response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void login() {
        User user = User.builder().email(OTHER_USER).build();
        Mockito.when(userDao.upsertUser(user)).thenReturn(upsert(user));
        ResponseEntity<String> response = postRequest("/login", user);
        User responseUser = gson.fromJson(response.getBody(), User.class);
        assertThat("User has Id", responseUser.getId(), not(blankOrNullString()));
    }

    private User upsert(User user) {
        User returnUser = repository.getOrDefault(user.getEmail(), null);
        if (returnUser != null) {
            return returnUser;
        } else {
            user.setId(UUID.randomUUID().toString());
            repository.put(user.getEmail(), user);
            return repository.get(user.getEmail());
        }

    }
    
    private String buildUri(String path) {
        return "http://localhost:" + port + "/user" + path;
    }

    private ResponseEntity<String> getRequest(String path, String userEmail) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Email", userEmail);
        return restTemplate.exchange(buildUri(path), HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    private ResponseEntity<String> postRequest(String path, User user) {
        return restTemplate.exchange(buildUri(path), HttpMethod.POST, new HttpEntity<>(user, null), String.class);
    }


}
