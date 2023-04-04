package com.example.burgger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.object.Burger;
import com.example.burgger.object.Ingredient;

import java.util.List;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    private int mResource;


    public IngredientAdapter(Context context, int resource, List<Ingredient> ingr) {
        super(context, resource, ingr);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Ingredient ingredient = getItem(position);


        TextView nameTextView = view.findViewById(R.id.nameTextView);


        nameTextView.setText(ingredient.getName());


        return view;
    }
}