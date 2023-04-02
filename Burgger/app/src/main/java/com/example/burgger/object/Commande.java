package com.example.burgger.object;

public class Commande {

    private int idCommande;
    private String burger;
    private String modification;
    private boolean pret;

    public Commande(int idCommande, String burger, String modification, boolean pret){
        this.idCommande = idCommande;
        this.burger = burger;
        this.modification = modification;
        this.pret = pret;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }



    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public boolean isPret() {
        return pret;
    }

    public void setPret(boolean pret) {
        this.pret = pret;
    }

    public String getBurger() {
        return burger;
    }

    public void setBurger(String burger) {
        this.burger = burger;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", burger='" + burger + '\'' +
                ", modification='" + modification + '\'' +
                ", pret=" + pret +
                '}';
    }
}
