package com.example.burgger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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


        commandeIdTextView.setText("Commande :" +commande.getIdCommande()+"    ");
        burgerIdTextView.setText( "Burger :" +commande.getIdBurger()+ "    ");
        modificationTextView.setText(commande.getModification());

        return view;
    }

}
