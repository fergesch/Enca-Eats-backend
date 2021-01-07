package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.model.Neighborhood;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NeighborhoodControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    Gson gson = new Gson();

    @Test
    public void getAllNeighborhoods() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/neighborhood", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Neighborhood> resultList = gson.fromJson(response.getBody(), new TypeToken<List<Neighborhood>>(){}.getType());
        assertThat(resultList.size()).isGreaterThan(1);
    }

}
