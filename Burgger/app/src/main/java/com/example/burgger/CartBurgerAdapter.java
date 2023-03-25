package com.example.burgger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartBurgerAdapter extends ArrayAdapter<Burger> {

    private Context mContext;
    private int mResource;

    public CartBurgerAdapter(Context context, int resource, List<Burger> burgers) {
        super(context, resource, burgers);
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

        Burger burger = getItem(position);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        ImageView photoImageView = view.findViewById(R.id.photoImageView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        nameTextView.setText(burger.getBurgerNamme());
        priceTextView.setText(""+burger.getPrice()* burger.getQuantity());
        photoImageView.setImageResource(mContext.getResources().getIdentifier(burger.getPhoto(), "drawable", mContext.getPackageName()));
        quantityTextView.setText("qté: "+burger.getQuantity());
        return view;
    }
}
