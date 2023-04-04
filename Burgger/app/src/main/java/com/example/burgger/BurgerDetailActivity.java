package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class BurgerDetailActivity extends AppCompatActivity {

    private ImageView burgerImageView;

    private TextView burgerNameTextView, burgerPriceTextView,burgerDescriptionTextView;

    private Button addBurgerToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger_detail);

        burgerImageView = findViewById(R.id.burger_detail_photo);
        burgerNameTextView = findViewById(R.id.burger_detail_name);
        burgerPriceTextView = findViewById(R.id.burger_detail_price);
        burgerDescriptionTextView = findViewById(R.id.burger_detail_description);
        addBurgerToCartButton = findViewById(R.id.addBurgerToCart);




        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String burger_name = sharedPreferences.getString("burger_name", "");
        String burger_price = sharedPreferences.getString("burger_price", "");
        String burger_photo = sharedPreferences.getString("burger_photo", "");
        String burger_description = sharedPreferences.getString("burger_description", "");
        int burger_id = sharedPreferences.getInt("burger_id",0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);

        System.out.println(burger_name+"\n\n\n\n\n");

        String imageUrl = "https://burgerr7.000webhostapp.com/img/" + burger_name.replaceAll("\\s", "%20") + ".png";
// Ajouter un paramètre aléatoire à l'URL de l'image
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(burgerImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                burgerImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                // Gérer les erreurs de chargement de l'image
            }
        });

        burgerNameTextView.setText(burger_name);
        burgerPriceTextView.setText(burger_price+"€");
        burgerDescriptionTextView.setText(burger_description);

        System.out.println(burger_id);
        addBurgerToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBurgerToCart(burger_id,user.getId_user());
            }
        });
    }



    private void addBurgerToCart(int id_burger,int id_user){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addBurgerToCart(id_user,id_burger);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Toast.makeText(getApplicationContext(),"burger ajouter au paniere",Toast.LENGTH_LONG);
                    finish();
                    Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(HomeIntent);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }




}