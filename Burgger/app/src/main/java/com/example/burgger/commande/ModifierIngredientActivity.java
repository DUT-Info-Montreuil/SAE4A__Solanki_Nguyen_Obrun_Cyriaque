package com.example.burgger.commande;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.burgger.R;
import com.example.burgger.commande.IngredientAdapter;
import com.example.burgger.object.Ingredient;

import java.util.ArrayList;

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
                System.out.println("burger"+idBurgerUnique + " f            jirjfdijjdifjosfdoijifdsijfijdsifsjiofjs");
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("burger"+idBurgerUnique, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("burger"+idBurgerUnique, getModificationString(ingredients));
                editor.apply();
                finish();
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
        return sharedPreferences.getInt(ingredient.getName()+ingredient.getPosition(), 1);
    }

    private String getModificationString(ArrayList<Ingredient> ingr){
        String modification = "";
        for (Ingredient i : ingr) {
            if(i.getPresent()==0){
                modification = modification + "sans " + i.getName() + "\n";
            }
        }
        return modification;
    }




}