package com.example.burgger.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("supprimerPanier.php")
    Call<ResponseBody> supprimerPanier(
            @Field("id_user") int id_user
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

    @FormUrlEncoded
    @POST("passerCommande.php")
    Call<ResponseBody> passerCommande(
            @Field("id_burger") int id_burger,
            @Field("modification") String modification
    );

    @FormUrlEncoded
    @POST("getBurgerIngredient.php")
    Call<ResponseBody> getBurgerIngredient(
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

    @retrofit2.http.Multipart
    @retrofit2.http.POST("upload-picture.php")
     Call<ResponseBody> uploadImage(@retrofit2.http.Part MultipartBody.Part image,@retrofit2.http.Part ("photoName") RequestBody photoName);

    @retrofit2.http.Multipart
    @retrofit2.http.POST("upload-picturePP.php")
    Call<ResponseBody> uploadImageProfil(@retrofit2.http.Part MultipartBody.Part image, @retrofit2.http.Part ("photoName") RequestBody photoName, @retrofit2.http.Part ("id_user") RequestBody id_user);


    @FormUrlEncoded
    @POST("addBurger.php")
    Call<ResponseBody> addBurger(
            @Field("burgerName") String burgerName,
            @Field("burgerDescription") String burgerDescription,
            @Field("burgerPrice") double BurgerPrice
    );

    @FormUrlEncoded
    @POST("dellBurger.php")
    Call<ResponseBody> dellBurger(
            @Field("id_burger") int id_burger

    );

    @FormUrlEncoded
    @POST("addIngredientsBurger.php")
    Call<ResponseBody> addIngredientsBurger(
            @Field("nameIngr") String nameIngr,
            @Field("nameBurger") String nameBurger,
            @Field("position") int position

    );


    @POST("getAllIngredients.php")
    Call<ResponseBody> getAllIngredients(
    );

}
