package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.Burger;
import com.example.burgger.object.Ingredient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifierIngredientActivity extends AppCompatActivity {

    private int id_burger;

    private IngredientAdapter mAdapter;

    private ListView ingrListView;
    private ArrayList<Ingredient> ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_ingredient);
        Intent intent = getIntent();
        ingrListView = findViewById(R.id.ingredient_list_view);
        id_burger = (int) intent.getSerializableExtra("burger");

        System.out.println(id_burger);
        initializeIngredient();

    }

    public void initializeIngredient(){
        ingredients=new ArrayList<>();
        getBurgerIngredient();

        mAdapter = new IngredientAdapter(this, R.layout.activity_ingredient_adapter, ingredients);

        ingrListView.setAdapter(mAdapter);
        ingrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Ingredient selectedIngredient = ingredients.get(position);
                System.out.println(selectedIngredient.toString());

            }
        });
    }

    public  void trierListeParPosition() {
        Collections.sort(ingredients, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient o1, Ingredient o2) {
                int position1 = ((Ingredient) o1).getPosition();
                int position2 = ((Ingredient) o2).getPosition();
                return Integer.compare(position1, position2);
            }
        });
        if(ingredients.isEmpty())
            System.out.println("nfoengjffojzeifjfoezjfjidnfjdvnoznvi");
        System.out.println(ingredients.toString());
    }

    public void getBurgerIngredient(){
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
                        mAdapter.notifyDataSetChanged();
                    }
                    trierListeParPosition();
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