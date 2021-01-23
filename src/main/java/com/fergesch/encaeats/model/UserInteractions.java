package com.fergesch.encaeats.model;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Data;

@Data
public class UserInteractions {
    String user_id;
    String rest_alias;
    DateBool wish_list;
    DateBool visited;
    ArrayList<Note> notes;
    UUID id;

   public UserInteractions(){
        this.wish_list = new DateBool();
        this.visited = new DateBool();
        this.notes = new ArrayList<>();
    }

    public UserInteractions(String user_id, String rest_alias) {
        this.wish_list = new DateBool();
        this.visited = new DateBool();
        this.notes = new ArrayList<>();
        this.user_id = user_id;
        this.rest_alias = rest_alias;
    }
}