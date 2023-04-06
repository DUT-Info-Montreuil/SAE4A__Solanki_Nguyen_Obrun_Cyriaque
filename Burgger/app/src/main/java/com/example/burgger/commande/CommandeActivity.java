package com.example.burgger.commande;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.Burger;
import com.example.burgger.object.Ingredient;
import com.example.burgger.object.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandeActivity extends AppCompatActivity {

    private ImageView imageRetour;
    private Button buttonValider;
    private ListView burgerCommandeList;
    private ArrayList<Burger> burgerListCart;
    private IngredientAdapter mAdapter;
    private CommandeBurgerAdapter burgerListAdapter;
    private User user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);

        imageRetour = findViewById(R.id.imageViewRetour);
        imageRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonValider = findViewById(R.id.valider_button);
        burgerCommandeList = findViewById(R.id.burger_list_view_commande);
        initializeCart();

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimerPanier();
                for (Burger b: burgerListCart) {
                    ajouterCommande(b);
                }
                finish();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);

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
                    } else {
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
                            int quantity = burgerObject.getInt("quantity");
                            Double prix;
                            if (reduction == 0) {
                                prix = burgerPrice;
                            } else {
                                prix = burgerPrice - ((reduction / 100) * burgerPrice);
                            }

                            for(int index=1 ; index<=quantity ; index++){
                                burgerListCart.add(new Burger(burgerId, burgerName, prix, burgerPhoto, burgerDescription,quantity , getBurgerIngredient(burgerId)));
                            }
                        }
                        burgerListAdapter.notifyDataSetChanged();
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


    public void initializeCart(){
        burgerListCart=new ArrayList<>();
        getCart(user.getId_user());
        burgerListAdapter = new CommandeBurgerAdapter(this,R.layout.activity_commande_burger_adapter,burgerListCart,user.getId_user(), burgerListCart);
        burgerCommandeList.setAdapter(burgerListAdapter);
        burgerCommandeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = burgerListCart.get(position);
                System.out.println(selectedBurger);
                System.out.println(selectedBurger.getIngredients().toString());
                Intent intent = new Intent(getApplicationContext(), ModifierIngredientActivity.class);
                intent.putExtra("ingredients",  selectedBurger.getIngredients());
                intent.putExtra("idBurgerUnique", selectedBurger.getBurgerIDUnique());
                startActivity(intent);
            }
        });
    }

    public void supprimerPanier(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.supprimerPanier(user.getId_user());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    System.out.println(jsonString);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void ajouterCommande(Burger burger){
        String modification = getModificationBurger(burger.getBurgerIDUnique());

        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.passerCommande(burger.getId_burger(), modification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    System.out.println(jsonString);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        getApplicationContext().deleteSharedPreferences("burger"+burger.getBurgerIDUnique());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public ArrayList<Ingredient> getBurgerIngredient(int id_burger){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getBurgerIngredient(id_burger);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    System.out.println(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        System.out.println(jsonObject.getString("msg"));
                    } else {
                        // modification réussite
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        System.out.println(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject ingredient = jsonArray.getJSONObject(i);

                            String ingredientName = ingredient.getString("ingrname");
                            int ingredientPosition = ingredient.getInt("position");

                            ingredients.add(new Ingredient(ingredientName, ingredientPosition));
                        }
                    }
                    trierListeParPosition(ingredients);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return ingredients;
    }

    public  void trierListeParPosition(ArrayList<Ingredient> ingr) {
        Collections.sort(ingr, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient o1, Ingredient o2) {
                int position1 = ((Ingredient) o1).getPosition();
                int position2 = ((Ingredient) o2).getPosition();
                return Integer.compare(position1, position2);
            }
        });
    }

    public String getModificationBurger(int idBurgerUnique){
        SharedPreferences sharedPreferences = getSharedPreferences("burger"+idBurgerUnique, MODE_PRIVATE);

        return sharedPreferences.getString("burger"+idBurgerUnique, "aucune modification");

    }


}
