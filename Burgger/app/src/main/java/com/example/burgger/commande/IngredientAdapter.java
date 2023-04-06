package com.example.burgger.commande;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.R;
import com.example.burgger.object.Burger;
import com.example.burgger.object.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    private int mResource;

    private int shared;
    private List<Ingredient> mIngredients;


    public IngredientAdapter(Context context, int resource, List<Ingredient> ingr, int sharedId) {
        super(context, resource, ingr);
        mContext = context;
        mResource = resource;
        mIngredients = ingr;
        this.shared = sharedId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }



        Ingredient ingredient = getItem(position);
        ImageView imageIngredient = view.findViewById(R.id.imageIngredient);
        Button addButton = view.findViewById(R.id.addIngr);
        Button rmButton = view.findViewById(R.id.rmIngr);
        imageIngredient.setImageResource(mContext.getResources().getIdentifier(ingredient.getName(), "drawable", mContext.getPackageName()));

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("burger"+shared, Context.MODE_PRIVATE);
        if(!sharedPreferences.contains("burger"+shared)){
            addButton.setEnabled(false);
            imageIngredient.setAlpha(1f);
            ingredient.setPresent(1);
            rmButton.setEnabled(true);
        }else{
            if(ingredient.getPresent() == 1){
                addButton.setEnabled(false);
                imageIngredient.setAlpha(1f);
                ingredient.setPresent(1);
                rmButton.setEnabled(true);
            } else if (ingredient.getPresent() == 0) {
                rmButton.setEnabled(false);
                imageIngredient.setAlpha(0.3f);
                ingredient.setPresent(0);
                addButton.setEnabled(true);
            }
        }




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rmButton.setEnabled(true);
                addButton.setEnabled(false);
                imageIngredient.setAlpha(1f);
                ingredient.setPresent(1);
                saveIngredientState(ingredient);
            }
        });

        rmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rmButton.setEnabled(false);
                addButton.setEnabled(true);
                imageIngredient.setAlpha(0.3f);
                ingredient.setPresent(0);
                saveIngredientState(ingredient);
            }
        });

        return view;
    }

    private void saveIngredientState(Ingredient ingredient) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("burger"+shared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ingredient.getName()+ingredient.getPosition(), ingredient.getPresent());
        editor.apply();
    }
}