package com.example.burgger.promotion;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.R;
import com.example.burgger.object.Burger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class BurgerAdapterPromotion extends ArrayAdapter<Burger> {

    private Context mContext;
    private int mResource;

    public BurgerAdapterPromotion(Context context, int resource, List<Burger> burgers) {
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
        TextView priceInitial = view.findViewById(R.id.priceTextViewInitial);

        nameTextView.setText(burger.getBurgerNamme());
        priceTextView.setText(String.format("%.2f €", burger.getPrice()) + "   ");
        String imageUrl = "https://burgerr7.000webhostapp.com/img/" + burger.getBurgerNamme().replaceAll("\\s", "%20") + ".png";
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(photoImageView, new Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                photoImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                photoImageView.setImageResource(mContext.getResources().getIdentifier("burgerdefault", "drawable", mContext.getPackageName()));

            }
        });
        priceInitial.setText(burger.getPrix_initial()+" €");
        priceInitial.setPaintFlags(priceInitial.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        priceInitial.setTextAppearance(getContext(), R.style.StrikeThroughRed);

        return view;
    }


}
