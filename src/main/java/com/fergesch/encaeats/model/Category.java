package com.fergesch.encaeats.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;

import java.util.List;

@Data
@Container(containerName = "categories")
public class Category {
    private String alias;
    private String title;
    private List<String> parent_aliases;
    private List<String> country_whitelist;
    private List<String> country_blacklist;
    private String id;
}
