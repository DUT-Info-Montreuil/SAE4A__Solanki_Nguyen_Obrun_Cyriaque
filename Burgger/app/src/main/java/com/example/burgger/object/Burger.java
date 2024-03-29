package com.example.burgger.object;

import java.util.ArrayList;

public class Burger {
    private int id_burger;

    private int quantity;




    private String burgerNamme;
    private double price , prix_initial,reduction;
    private  String photo;

    private static int autoIncrement = 0;

    private int BurgerIDUnique;
    private ArrayList<Ingredient> mIngredients;
    private String desription;

    public Burger(int id_burger, double reduction, String burgerNamme, double price, String photo, String desription) {
        this.id_burger = id_burger;
        this.reduction = reduction;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
    }

    public Burger(int id_burger, String burgerNamme, double price, String photo, String desription) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.BurgerIDUnique = autoIncrement;
        autoIncrement++;
    }

    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription, double prix_initial) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.prix_initial = prix_initial;
        this.BurgerIDUnique = autoIncrement;
        autoIncrement++;
    }



    public Burger(int id_burger, String burgerNamme, double price, String photo,String desription,int quantity) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.quantity = quantity;
        this.BurgerIDUnique = autoIncrement;
        autoIncrement++;
    }

    public Burger(int id_burger, String burgerNamme, double price, String photo, String desription, int quantity, ArrayList<Ingredient> ingr) {
        this.id_burger = id_burger;
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.photo = photo;
        this.desription = desription;
        this.quantity = quantity;
        this.mIngredients = ingr;

        this.BurgerIDUnique = autoIncrement;
        autoIncrement++;
    }

    public Burger(String burgerNamme, double price, String desription) {
        this.burgerNamme = burgerNamme;
        this.price = price;
        this.desription = desription;
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


    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
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


    public int getBurgerIDUnique() {
        return BurgerIDUnique;
    }

    public void setBurgerIDUnique(int burgerIDUnique) {
        BurgerIDUnique = burgerIDUnique;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
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
