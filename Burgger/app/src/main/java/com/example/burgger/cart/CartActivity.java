package com.example.burgger.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.burgger.ProfilActivity;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.promotion.PromotionActivity;
import com.example.burgger.R;
import com.example.burgger.api.RetrofitClientInstance;
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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CartActivity extends AppCompatActivity {
    private ImageView profilImageView;
    private CartBurgerAdapter burgerListAdapter;
    private ArrayList<Burger> cart;
    private ListView burgersListView;
    private TextView totalTextView, textAucuneCommande;
    private ImageView burgerList, promotion;

    private Button buttonContinuer;

    private double total=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setOnclick();
        buttonContinuer = findViewById(R.id.continuer_button);
        burgersListView = findViewById(R.id.burger_list_view);
        textAucuneCommande = findViewById(R.id.aucuneCommandeCart);
        initializeCart();

        burgerList = findViewById(R.id.imageViewBurger);


        burgerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        promotion = findViewById(R.id.imageViewPromotion);
        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), PromotionActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

    }



    private void setOnclick(){
        profilImageView = findViewById(R.id.imageViewProfil);

        totalTextView = findViewById(R.id.TotaltextView);
        profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(profilActivity);

            }
        });
    }


    public   void initializeCart(){

        cart=new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);
        getCart(user.getId_user());

        burgerListAdapter = new CartBurgerAdapter(this,R.layout.cart_list_item,cart,user.getId_user(),totalTextView,cart, textAucuneCommande);
        burgersListView.setAdapter(burgerListAdapter);
        burgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = cart.get(position);
                System.out.println(selectedBurger);
            }
        });

    }


    public void getCart(int id_user){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCart(id_user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        System.out.println(jsonObject.getString("msg"));
                        textAucuneCommande.setText("Aucune commande");
                    } else {
                        textAucuneCommande.setText("");

                        System.out.println(jsonString);

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        System.out.println(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject burgerObject = jsonArray.getJSONObject(i);
                            int burgerId = burgerObject.getInt("id_burger");
                            String burgerName = burgerObject.getString("burgername");
                            Double burgerPrice = burgerObject.getDouble("price");
                            String burgerPhoto = burgerObject.getString("photo");
                            String burgerDescription = burgerObject.getString("description");
                            Double reduction = burgerObject.getDouble("COALESCE(reduction, 0)");
                            if (reduction == 0) {
                                cart.add(new Burger(burgerId, burgerName, burgerPrice, burgerPhoto, burgerDescription, burgerObject.getInt("quantity")));
                                total += burgerObject.getInt("quantity") * burgerPrice;
                            } else {
                                Double prixReduction = burgerPrice - ((reduction / 100) * burgerPrice);
                                cart.add(new Burger(burgerId, burgerName, prixReduction, burgerPhoto, burgerDescription, burgerObject.getInt("quantity")));
                                total += burgerObject.getInt("quantity") * prixReduction;
                            }

                        }

                        burgerListAdapter.notifyDataSetChanged();
                        totalTextView.setText("Total : " + total + " €");
                    }
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