package com.example.burgger.promotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.burgger.BurgerDetailActivity;
import com.example.burgger.MainActivity;
import com.example.burgger.ProfilActivity;
import com.example.burgger.R;
import com.example.burgger.RetrofitClientInstance;
import com.example.burgger.cart.CartActivity;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.Burger;
import com.example.burgger.object.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class PromotionActivity extends AppCompatActivity {

    private User user;

    private BurgerAdapterPromotion burgerListAdapter;

    private ArrayList<Burger> burgers;

    private ImageView burgerList , cartList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

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

        ImageView profilImageView = findViewById(R.id.imageViewProfil);
        ListView burgersListView = findViewById(R.id.burger_list_view);

        burgers=new ArrayList<>();
        getAllBurgers();
        burgerListAdapter = new BurgerAdapterPromotion(this,R.layout.burger_list_item_promotion,burgers);
        burgersListView.setAdapter(burgerListAdapter);
        burgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = burgers.get(position);

                System.out.println(selectedBurger.toString());
                // Ouvrir une nouvelle activité avec les détails du burger sélectionné

                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(user);
                editor.putInt("burger_id",selectedBurger.getId_burger());
                editor.putString("burger_price", ""+selectedBurger.getPrice());
                editor.putString("burger_name", selectedBurger.getBurgerNamme());
                editor.putString("burger_photo", selectedBurger.getPhoto());
                editor.putString("burger_description", selectedBurger.getDesription());
                editor.apply();


                Intent burgerDetailIntent = new Intent(getApplicationContext(), BurgerDetailActivity.class);
                startActivity(burgerDetailIntent);
            }
        });


        profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(registerActivity);

            }
        });

        burgerList = findViewById(R.id.imageViewBurger);
        burgerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        cartList = findViewById(R.id.cartimageView);
        cartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

    }

    public interface ApiInterface {
        @POST("getBurgersPromotion.php")
        Call<ResponseBody> getBurgers();
    }

    public void getAllBurgers(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
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
                        Double reduction = burgerObject.getDouble("reduction");
                        Double prixReduction = burgerPrice - ( (reduction/100)*burgerPrice );
                        String burgerPhoto = burgerObject.getString("photo");
                        String burgerDescription = burgerObject.getString("description");
                        burgers.add(new Burger(burgerId,burgerName,prixReduction,burgerPhoto,burgerDescription, burgerPrice));

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