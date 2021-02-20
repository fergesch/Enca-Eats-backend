package com.fergesch.encaeats.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class UserInteractions {
    String email;
    String rest_alias;
    DateBool wish_list;
    DateBool visited;
    ArrayList<Note> notes;
    String id;

    public UserInteractions(String email, String rest_alias) {
        this.wish_list = new DateBool();
        this.visited = new DateBool();
        this.notes = new ArrayList<>();
        this.email = email;
        this.rest_alias = rest_alias;
        setId();
    }

    public UserInteractions(String email, String rest_alias, DateBool wish_list, DateBool visited, ArrayList<Note> notes, String id) {
        this.email = email;
        this.rest_alias = rest_alias;
        this.wish_list = wish_list;
        this.visited = visited;
        this.notes = notes;
        setId();
    }

    public boolean checkVisited() {
       return checkDateBool(this.visited);
    }

    public boolean checkWishList() {
       return checkDateBool(this.wish_list);
    }

    private boolean checkDateBool(DateBool dateBool) {
      return dateBool != null && dateBool.getBool() != null && dateBool.getBool();
    }

    public void setId() {
        this.id = this.email + "||" + this.rest_alias;
    }
}