package com.fergesch.encaeats.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String id;
    String name;
    String email;
    String neighborhood;
}
