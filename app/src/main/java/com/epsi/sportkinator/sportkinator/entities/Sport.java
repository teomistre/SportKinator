package com.epsi.sportkinator.sportkinator.entities;

/**
 * Created by rbertal on 10/04/2015.
 */
public class Sport extends Tag{

    private String imagePath;

    public Sport(){
        super();
    }

    public Sport(String name, String imagePath){
        super(name);
        this.imagePath = imagePath;
    }

    public String getImage() {
        return imagePath;
    }
    public void setImagePath(String imagePath) { this.imagePath = imagePath;}

     @Override
    public String toString() {
        return "Sport{" +
                "name='" + name + '\'' +
                ", image='" + imagePath + '\'' +
                '}';
    }
}
