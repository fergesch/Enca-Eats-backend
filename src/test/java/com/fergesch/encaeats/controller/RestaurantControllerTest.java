package com.fergesch.encaeats.controller;

import com.fergesch.encaeats.model.Category;
import com.fergesch.encaeats.model.Restaurant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    Gson gson = new Gson();

    @Test
    public void getRestaurantByAlias() {
        String restaurantAlias = "the-fat-shallot-food-truck-chicago-4";
        ResponseEntity<String> response = getRequest("", "alias=" + restaurantAlias);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Restaurant result = gson.fromJson(response.getBody(), Restaurant.class);
        assertThat("Find restaurant by alias did not match", result.getAlias(), equalTo(restaurantAlias));
    }

    @Test
    public void getNonExistingRestaurantByAlias() {
        String restaurantAlias = "dummy";
        ResponseEntity<String> response = getRequest("", "alias=" + restaurantAlias);
        assertThat("No restaurant found by alias response code did not match", response.getStatusCode(),
                equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void searchRestaurants() {
        String neighborhood = "Lake View";
        String price = "$";
        String categories = "icecream";
        double rating = 2.0;
        String queryParams = "neighborhoods=" + neighborhood + "&price=" + price + "&categories=" + categories
                + "&rating=" + rating;
        ResponseEntity<String> response = getRequest("/search", queryParams);
        List<Restaurant> resultList = gson.fromJson(response.getBody(), new TypeToken<List<Restaurant>>() {
        }.getType());
        assertThat("Successful restaurant search did not yield any results ", resultList.size(),
                greaterThanOrEqualTo(1));
        checkResults(resultList, neighborhood, price, categories, rating);
    }

    @Test
    public void multiSelectRestaurantSearch() {
        String neighborhood = "Lake View, West Loop";
        String price = "$,$$";
        String categories = "icecream, sushi";
        double rating = 2.0;
        String queryParams = "neighborhoods=" + neighborhood + "&price=" + price + "&categories=" + categories
                + "&rating=" + rating;
        ResponseEntity<String> response = getRequest("/search", queryParams);
        List<Restaurant> resultList = gson.fromJson(response.getBody(), new TypeToken<List<Restaurant>>() {
        }.getType());
        assertThat("Successful restaurant search did not yield any results ", resultList.size(),
                greaterThanOrEqualTo(1));
        checkResults(resultList, neighborhood, price, categories, rating);
    }

    @Test
    public void noSearchResults() {
        String neighborhood = "West Loop";
        String price = "$$$$";
        String categories = "icecream";
        double rating = 4.0;
        String queryParams = "neighborhoods=" + neighborhood + "&price=" + price + "&categories=" + categories
                + "&rating=" + rating;
        ResponseEntity<String> response = getRequest("/search", queryParams);
        assertThat("No results restaurant search response code did not match", response.getStatusCode(),
                equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void noValidSearchParameters() {
        String queryParams = "bogus=fake";
        ResponseEntity<String> response = getRequest("/search", queryParams);
        assertThat("Invalid search parameters response code did not match", response.getStatusCode(),
                equalTo(HttpStatus.BAD_REQUEST));
    }

    private void checkResults(List<Restaurant> results, String neighborhood, String price, String categories,
            double rating) {
        for (Restaurant r : results) {
            neighborhoodCheck(r, neighborhood);
            priceCheck(r, price);
            categoryCheck(r, categories);
            ratingCheck(r, rating);
        }
    }

    private void neighborhoodCheck(Restaurant r, String neighborhood) {
        assertThat("search result neighborhood check failed", neighborhood,
                containsStringIgnoringCase(r.getNeighborhood()));
    }

    private void priceCheck(Restaurant r, String price) {
        assertThat("price", price, containsStringIgnoringCase(r.getPrice()));
    }

    // TODO deal with children categories
    private void categoryCheck(Restaurant r, String categories) {
        boolean result = false;
        for (Category c : r.getCategories()) {
            if (categories.contains(c.getAlias())) {
                result = true;
            }
        }
        assertThat("search result category did not have appropriate category matching", result, equalTo(true));
    }

    private void ratingCheck(Restaurant r, double rating) {
        assertThat("search result price check failed", r.getRating(), greaterThanOrEqualTo(rating));
    }

    private String buildUri(String path) {
        return "http://localhost:" + port + "/restaurant" + path;
    }


    private ResponseEntity<String> getRequest(String path, String queryParams) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Email", "enca.butt@gmail.com");
        return restTemplate.exchange(buildUri(path + "?" + queryParams), HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
    }

}
