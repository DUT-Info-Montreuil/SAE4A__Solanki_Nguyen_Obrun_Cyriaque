package com.example.burgger.commande;

import android.content.Context;
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
import com.example.burgger.object.Burger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CommandeBurgerAdapter extends ArrayAdapter<Burger> {

    private Context mContext;
    private int mResource;

    private int id_user;


    private ArrayList<Burger> cart;

    public CommandeBurgerAdapter(Context context, int resource, List<Burger> burgers, int id_user,  ArrayList<Burger> cart) {
        super(context, resource, burgers);
        mContext = context;
        mResource = resource;
        this.id_user=id_user;
        this.cart=cart;
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
        ImageView photoImageView = view.findViewById(R.id.photoImageView);
        TextView modification = view.findViewById(R.id.modificationTextView);
        nameTextView.setText(burger.getBurgerNamme());
        photoImageView.setImageResource(mContext.getResources().getIdentifier(burger.getPhoto(), "drawable", mContext.getPackageName()));

        return view;
    }




}
