package com.example.github_dmos5.model;

import java.io.Serializable;

public class GitHub implements Serializable {

    long id;
    String name;
    String isPrivate;

    public GitHub(long id, String name, String isPrivate){
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }
}
