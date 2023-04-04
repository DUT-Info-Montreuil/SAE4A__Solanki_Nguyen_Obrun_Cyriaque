package com.example.burgger.admin;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.cart.CartActivity;
import com.example.burgger.object.Burger;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Response;

public class EditBurgerAdapter extends ArrayAdapter<Burger> {

    private Context mContext;
    private int mResource;

    private Activity mManageBurgerActivity;

    private ArrayList<Burger> burgers;
    public EditBurgerAdapter(Context context, int resource, List<Burger> burgers ,  Activity manageBurgerActivity) {
        super(context, resource, burgers);
        mContext = context;
        mResource = resource;
        mManageBurgerActivity = manageBurgerActivity;
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

        nameTextView.setText(burger.getBurgerNamme());
        priceTextView.setText(String.format("%.2f €", burger.getPrice()));

        String imageUrl = "https://burgerr7.000webhostapp.com/img/" + burger.getBurgerNamme().replaceAll("\\s", "%20") + ".png";
// Ajouter un paramètre aléatoire à l'URL de l'image
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(photoImageView, new Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                photoImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                // Gérer les erreurs de chargement de l'image
            }
        });
        Button editBugerBUtton = view.findViewById(R.id.EditBurgerbutton);

        editBugerBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(burger);
                editor.putString("burger", json);
                editor.apply();
               goToEditBurgerActivity();
            }
        });
        return view;
    }

    public void goToEditBurgerActivity() {
        Intent EditBurgerActivity = new Intent(mContext, EditBurgerActivity.class);
        mContext.startActivity(EditBurgerActivity);
        mManageBurgerActivity.finish();

    }

}