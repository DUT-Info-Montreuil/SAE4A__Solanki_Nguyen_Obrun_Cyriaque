package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class HomeActivity extends AppCompatActivity {

    private User user;
    private ImageView profilImawgeView;

    private BurgerAdapter burgerListAdapter;
    private ListView burgersListView;

    private ArrayList<Burger> burgers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);
        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        profilImawgeView=findViewById(R.id.imageViewProfil);
        burgersListView = findViewById(R.id.burger_list_view);

        burgers=new ArrayList<>();
        getAllBurgers();
        burgerListAdapter = new BurgerAdapter(this,R.layout.burger_list_item,burgers);
        burgersListView.setAdapter(burgerListAdapter);
        burgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = burgers.get(position);
                System.out.println(selectedBurger);
                // Ouvrir une nouvelle activité avec les détails du burger sélectionné

                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(user);
                editor.putString("burger_price", ""+selectedBurger.getPrice());
                editor.putString("burger_name", selectedBurger.getBurgerNamme());
                editor.putString("burger_photo", selectedBurger.getPhoto());
                editor.apply();


                Intent burgerDetailIntent = new Intent(getApplicationContext(), BurgerDetailActivity.class);
                startActivity(burgerDetailIntent);
            }
        });


        profilImawgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(registerActivity);

            }
        });
    }

    public interface ApiInterface {

        @POST("getBurgers.php")
        Call<ResponseBody> getBurgers();

    }

    public void getAllBurgers(){
        HomeActivity.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(HomeActivity.ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getBurgers();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject burgerObject = jsonArray.getJSONObject(i);
                        int burgerId = burgerObject.getInt("id_burger");
                        String burgerName = burgerObject.getString("burgername");
                        Double burgerPrice = burgerObject.getDouble("price");
                        String burgerPhoto = burgerObject.getString("photo");

                        burgers.add(new Burger(burgerId,burgerName,burgerPrice,burgerPhoto));

                    }

                    burgerListAdapter.notifyDataSetChanged();

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

