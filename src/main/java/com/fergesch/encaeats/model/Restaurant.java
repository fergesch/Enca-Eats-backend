package com.fergesch.encaeats.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import java.util.ArrayList;

@Data
@Container(containerName = "restaurants")
public class Restaurant {

    private String id;
    private String alias;
    private String name;
    private String image_url;
    private boolean is_closed;
    private String url;
    private int review_count;
    private ArrayList<Category> categories;
    private double rating;
    private Coordinates coordinates;
    private ArrayList<String> transactions;
    private String price;
    private Location location;
    private String phone;
    private String display_phone;
    private double distance;
    private String neighborhood;
}
