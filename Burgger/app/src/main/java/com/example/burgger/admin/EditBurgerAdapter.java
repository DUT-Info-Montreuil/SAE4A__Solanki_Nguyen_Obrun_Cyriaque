package com.example.burgger.admin;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.cart.CartActivity;
import com.example.burgger.object.Burger;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
                photoImageView.setImageResource(mContext.getResources().getIdentifier("burgerdefault", "drawable", mContext.getPackageName()));

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

        Button dellBugerBUtton = view.findViewById(R.id.DellBUrgerbutton);

        dellBugerBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation");
                builder.setMessage("Voulez vous vraiment supprimer le burger id:"+burger.getId_burger()+" "+burger.getBurgerNamme());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
                        Call<ResponseBody> call = apiInterface.dellBurger(burger.getId_burger());
                        call.enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(mContext, "Burger supprimé", Toast.LENGTH_LONG).show();
                                remove(burger);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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