package com.example.burgger;

public class Burger {
    private int id_burger;

    private int quantity;
    private String burgerNamme;
    private double price , prix_initial;
    private  String photo;

    private String desription;

    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
    }

    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription, double prix_initial) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.prix_initial = prix_initial;
    }



    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription,int quantity) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.quantity = quantity;
    }

    public int getId_burger() {
        return id_burger;
    }

    public double getPrix_initial() {
        return prix_initial;
    }

    public void setPrix_initial(double prix_initial) {
        this.prix_initial = prix_initial;
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


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
