package com.example.burgger;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.cuisine.CommandeAdapter;
import com.example.burgger.cuisine.CommandeUserAdapter;
import com.example.burgger.object.Commande;
import com.example.burgger.object.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommandeUserFragment extends Fragment {

    private User user;
    private ArrayList<Commande> commandes;
    private ListView commandeList;

    private CommandeUserAdapter mCommandeAdapter;

    private TextView textAucuneCommande;
    public CommandeUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_commande_user, container, false);
        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);
        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        textAucuneCommande = view.findViewById(R.id.aucuneCommande);
        commandeList = view.findViewById(R.id.comandeUser_list_view);
        initializeCommande();
        return view;
    }

    private  void initializeCommande(){
        commandes=new ArrayList<>();
        getAllCommandes();
        mCommandeAdapter = new CommandeUserAdapter(getActivity().getApplicationContext(),R.layout.usercommande_list_item_p,commandes);
        commandeList.setAdapter(mCommandeAdapter);

    }


    public void getAllCommandes(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getUserCommandes(user.getId_user());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        System.out.println(jsonObject.getString("msg"));
                        textAucuneCommande.setText("Aucune commande");
                    } else {
                        textAucuneCommande.setText("");
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject burgerObject = jsonArray.getJSONObject(i);
                            int commandeId = burgerObject.getInt("id_commande");
                            String burgerName = burgerObject.getString("burgername");
                            String modification = burgerObject.getString("modification");
                            int pret = burgerObject.getInt("pret");
                            boolean p;
                            if (pret == 0)
                                p = false;
                            else
                                p = true;

                            commandes.add(new Commande(commandeId, burgerName, modification, p));
                        }
                        mCommandeAdapter.notifyDataSetChanged();

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}