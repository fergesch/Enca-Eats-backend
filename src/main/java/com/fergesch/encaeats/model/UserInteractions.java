package com.fergesch.encaeats.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class UserInteractions {
    String user_id;
    String rest_alias;
    DateBool wish_list;
    DateBool visited;
    ArrayList<Note> notes;

    public UserInteractions(){
        wish_list = new DateBool();
        visited = new DateBool();
        notes = new ArrayList<>();
    }
}