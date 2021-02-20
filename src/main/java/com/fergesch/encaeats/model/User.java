package com.fergesch.encaeats.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class User {
    String id;
    String name;
    String email;
    String neighborhood;
}
