package com.epsi.sportkinator.sportkinator.entities;

/**
 * Created by rbertal on 10/04/2015.
 */
public class Sport extends Tag{

    public Sport(){
        super();
    }

    public Sport(String n){
        super(n);
    }

     @Override
    public String toString() {
        return "Sport{" +
                "response='" + name + '\'' +
                '}';
    }
}
