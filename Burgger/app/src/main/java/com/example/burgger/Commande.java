package com.example.burgger;

public class Commande {

    private int idCommande;
    private int idBurger;
    private String modification;
    private boolean pret;

    public Commande(int idCommande, int idBurger, String modification, boolean pret){
        this.idCommande = idCommande;
        this.idBurger = idBurger;
        this.modification = modification;
        this.pret = pret;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdBurger() {
        return idBurger;
    }

    public void setIdBurger(int idBurger) {
        this.idBurger = idBurger;
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

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", idBurger=" + idBurger +
                ", modification='" + modification + '\'' +
                ", pret=" + pret +
                '}';
    }
}
