package com.example.burgger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CommandeAdapter extends ArrayAdapter<Commande> {

    private Context mContext;
    private int mResource;

    public CommandeAdapter(Context context, int resource, List<Commande> commandes) {
        super(context, resource, commandes);
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

        Commande commande = getItem(position);


        TextView commandeIdTextView = view.findViewById(R.id.commandeIdTextView);
        TextView burgerIdTextView = view.findViewById(R.id.burgerIdTextView);
        TextView modificationTextView = view.findViewById(R.id.modificationTextView);


        commandeIdTextView.setText("N°" +commande.getIdCommande()+"    ");
        burgerIdTextView.setText( commande.getBurger()+ "  ");
        modificationTextView.setText(commande.getModification());

        return view;
    }




}
