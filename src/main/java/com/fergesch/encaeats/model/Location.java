package com.fergesch.encaeats.model;


import lombok.Data;

import java.util.List;

@Data
public class Location {

      private String address1;
      private String address2;
      private String address3;
      private String city;
      private String zip_code;
      private String country;
      private String state;
      private List<String> display_address;
}
