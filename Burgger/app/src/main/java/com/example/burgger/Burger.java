package com.example.burgger;

public class Burger {
    private int id_burger;
    private String burgerNamme;
    private double price;
    private  String photo;

    private String desription;

    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
    }

    public int getId_burger() {
        return id_burger;
    }

    public void setId_burger(int id_burger) {
        this.id_burger = id_burger;
    }

    public String getBurgerNamme() {
        return burgerNamme;
    }

    public void setBurgerNamme(String burgerNamme) {
        this.burgerNamme = burgerNamme;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }




    @Override
    public String toString() {
        return "Burger{" +
                "id_burger=" + id_burger +
                ", burgerNamme='" + burgerNamme + '\'' +
                ", price=" + price +
                ", photo='" + photo + '\'' +
                '}';
    }
}
