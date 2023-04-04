package com.example.burgger.object;

public class Ingredient {

    private String name;
    private int position;

    public Ingredient(String name , int position){
        this.name = name;
        this.position = position;

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

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
