package com.example.burgger.object;

import java.io.Serializable;

public class Ingredient  implements  Serializable{

    private String name;
    private int position;
    private int present;

    public Ingredient(String name , int position){
        this.name = name;
        this.position = position;
        this.present = 1;

    }

    public Ingredient(String name ){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", present=" + present +
                '}';
    }
}
