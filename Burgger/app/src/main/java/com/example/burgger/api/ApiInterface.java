package com.example.burgger.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("searchUser.php")
    Call<ResponseBody> searchUser(@Field("username") String username);

    @POST("getUsers.php")
    Call<ResponseBody> getUsers();

    @FormUrlEncoded
    @POST("dellUser.php")
    Call<ResponseBody> dellUser(
            @Field("id_user") int id_user
    );

    @FormUrlEncoded
    @POST("banUser.php")
    Call<ResponseBody> banUser(
            @Field("id_user") int id_user,
            @Field("ban") int ban
    );
    @FormUrlEncoded
    @POST("getCart.php")
    Call<ResponseBody> getCart(
            @Field("id_user") int id_user

    );

    @FormUrlEncoded
    @POST("addQte.php")
    Call<ResponseBody> addQte(
            @Field("id_user") int id_user,
            @Field("id_burger") int id_burger
    );


    @FormUrlEncoded
    @POST("rmQte.php")
    Call<ResponseBody> rmQte(
            @Field("id_user") int id_user,
            @Field("id_burger") int id_burger
    );


    @POST("getCommandesPrete.php")
    Call<ResponseBody> getCommandesPrete();
    @POST("getCommandes.php")
    Call<ResponseBody> getCommandes();

    @POST("supprimerCommandePrete.php")
    Call<ResponseBody> supprimerCommandePrete();

    @FormUrlEncoded
    @POST("majCommandePret.php")
    Call<ResponseBody> majPret(@Field("idCommande") int idCommande);


    @FormUrlEncoded
    @POST("addBurgerToCart.php")
    Call<ResponseBody> addBurgerToCart(
            @Field("id_user") int id_user,
            @Field("id_burger") int id_burger
    );


    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("modification.php")
    Call<ResponseBody> modification(
            @Field("username") String username,
            @Field("newpass") String newpass,
            @Field("name") String name,
            @Field("firstName") String firstName,
            @Field("address") String address,
            @Field("city") String city,
            @Field("oldpass") String oldpass,
            @Field("newCpass") String newCpass
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("firstName") String firstName,
            @Field("email") String email,
            @Field("address") String address,
            @Field("city") String city
    );


    @FormUrlEncoded
    @POST("editBurger.php")
    Call<ResponseBody> editBurger(
            @Field("burgerName") String burgerName,
            @Field("burgerDescription") String burgerDescription,
            @Field("burgerPrice") double BurgerPrice,
            @Field("BurgerReduction") double BurgerReduction,
            @Field("burgerPhoto") String BurgerPhoto,
            @Field("id_burger") int id_burger
    );



}
