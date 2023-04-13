package com.example.burgger.cuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.burgger.R;
import com.example.burgger.object.Commande;

import java.util.List;

public class CommandeUserAdapter extends ArrayAdapter<Commande> {

    private Context mContext;
    private int mResource;

    public CommandeUserAdapter(Context context, int resource, List<Commande> commandes) {
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
// Récupérer le layout
        LinearLayout layout = view.findViewById(R.id.layout_commandUser);

// Vérifier si l'unif est vrai ou faux
        if (commande.isPret()) {
            // Si l'unif est vrai, définir le fond d'écran arrondi vert
            layout.setBackgroundResource(R.drawable.bg_rounded_corners_vert);
        } else {
            layout.setBackgroundResource(R.drawable.bg_rounded_corners);
        }

        commandeIdTextView.setText("N°" +commande.getIdCommande()+"    ");
        burgerIdTextView.setText( commande.getBurger()+ "  ");
        modificationTextView.setText(commande.getModification());

        return view;
    }




}
