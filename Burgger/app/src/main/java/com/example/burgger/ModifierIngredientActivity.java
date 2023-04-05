package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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



    private IngredientAdapter mAdapter;
    private ListView ingrListView;
    private ArrayList<Ingredient> ingredients;

    private int idBurgerUnique;
    private Button validerModif;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_ingredient);
        Intent intent = getIntent();
        ingrListView = findViewById(R.id.ingredient_list_view);
        validerModif = findViewById(R.id.valideIngrButton);
        ingredients = (ArrayList<Ingredient>) intent.getSerializableExtra("ingredients");
        idBurgerUnique = (int) intent.getSerializableExtra("idBurgerUnique");
        initializeIngredient();

        validerModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(ingredients.toString());
                finish();
                System.out.println(idBurgerUnique +"                dojvdnjnncejfbvnefjvnfnjvkndvjnvjknfdjvnjvfndjkvndf");
            }
        });

    }

    public void initializeIngredient(){
        for (Ingredient ingredient : ingredients) {
            int state = getIngredientState(ingredient); // Récupère l'état enregistré de l'ingrédient
            ingredient.setPresent(state);
        }
        mAdapter = new IngredientAdapter(this, R.layout.activity_ingredient_adapter, ingredients, idBurgerUnique);
        ingrListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private int getIngredientState(Ingredient ingredient) {
        SharedPreferences sharedPreferences = getSharedPreferences("burger"+idBurgerUnique, MODE_PRIVATE);
        return sharedPreferences.getInt(ingredient.getName()+ingredient.getPosition(), 0);
    }




}