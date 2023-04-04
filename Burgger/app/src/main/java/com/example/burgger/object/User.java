package com.example.burgger.object;

public class User {

    private int id_user;



    private boolean ban;
    public User(int id_user, String username, String name, String fisrtname, String email, String city, String address) {
        this.id_user = id_user;
        this.username = username;
        this.name = name;
        this.fisrtname = fisrtname;
        this.email = email;
        this.city = city;
        this.address = address;
    }

    public User() {

    }

    private String username;
    private String name;
    private String fisrtname;
    private String email;
    private String city;
    private String address;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;



    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFisrtname() {
        return fisrtname;
    }

    public void setFisrtname(String fisrtname) {
        this.fisrtname = fisrtname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }


    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }
    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", fisrtname='" + fisrtname + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
