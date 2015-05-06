package com.epsi.sportkinator.sportkinator.entities;

/**
 * Created by rbertal on 10/04/2015.
 */
public class Question extends Tag {

    private String type;
    private String response;
    private String id;

    public Question(String name,  String response, String id) {
        super(name);
        this.response = response;
        this.id = id;
    }

    public Question() {
        super();
        this.type = "";
        this.response = "";
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Question{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", response='" + response + '\'' +
                ", id=" + id +
                '}';
    }
}

