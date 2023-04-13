package com.example.burgger.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.burgger.R;
import com.example.burgger.object.Ingredient;

import java.util.List;


public class AdminAddBurgerAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    private int mResource;
    private List<Ingredient> ingrs;

    public AdminAddBurgerAdapter(@NonNull Context context, int resource, List<Ingredient> listIngr) {
        super(context, resource, listIngr);
        mContext = context;
        mResource = resource;
        ingrs = listIngr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Ingredient ingr = getItem(position);

        TextView nomIngr = view.findViewById(R.id.nomIngredient);
        Button suppButton = view.findViewById(R.id.suppIngr);

        nomIngr.setText(ingr.getName());

        suppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Supprimer l'ingrédient de la liste
                ingrs.remove(position);
                notifyDataSetChanged();
            }
        });


        return view;
    }
}
