package com.epsi.sportkinator.sportkinator.entities;

/**
 * Created by rbertal on 14/04/2015.
 */
public class Tag {

    protected  String name;

    public Tag(String name) {
        this.name = name;
    }
    public Tag() {
        this.name ="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}
