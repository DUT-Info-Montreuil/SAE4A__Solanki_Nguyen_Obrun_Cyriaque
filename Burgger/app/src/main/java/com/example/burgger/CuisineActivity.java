package com.example.burgger;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class CuisineActivity extends AppCompatActivity  {

    private User user;
    private ArrayList<Commande> commandes;
    private ArrayList<Commande> commandesPrete;
    private CommandeAdapter mCommandeAdapter;
    private CommandeAdapter mCommandeAdapterPret;
    private ListView commandeList;
    private ListView commandeListPrete;
    private ImageView refresh;
    private ImageView supprimer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);
        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        commandeList = findViewById(R.id.commande_list_view);
        commandeListPrete = findViewById(R.id.commande_list_view_prete);
        refresh = findViewById(R.id.refresh_commande);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
            }
        });

        supprimer = findViewById(R.id.supprimer_commande_prete);
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimerCommandePrete();
                finish();
                startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
            }
        });

        initializeCommande();
    }



    public interface ApiInterface {
        @POST("getCommandesPrete.php")
        Call<ResponseBody> getCommandesPrete();
        @POST("getCommandes.php")
        Call<ResponseBody> getCommandes();

        @POST("supprimerCommandePrete.php")
        Call<ResponseBody> supprimerCommandePrete();

        @FormUrlEncoded
        @POST("majCommandePret.php")
        Call<ResponseBody> majPret(@Field("idCommande") int idCommande);

    }

    public void getAllCommandes(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCommandes();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        System.out.println(jsonObject.getString("msg"));
                    } else {

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

    public void getAllCommandesPrete(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCommandesPrete();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);

                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        System.out.println(jsonObject.getString("msg"));
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject burgerObject = jsonArray.getJSONObject(i);
                            int commandeId = burgerObject.getInt("id_commande");
                            String burgerName = burgerObject.getString("burgername");
                            String modification = burgerObject.getString("modification");
                            int pret = burgerObject.getInt("pret");
                            boolean p;
                            if(pret==0)
                                p=false;
                            else
                                p=true;

                            commandesPrete.add(new Commande(commandeId,burgerName,modification,p));
                        }
                        mCommandeAdapterPret.notifyDataSetChanged();
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


    private  void initializeCommande(){
        commandes=new ArrayList<>();
        commandesPrete = new ArrayList<>();
        getAllCommandes();
        getAllCommandesPrete();
        mCommandeAdapter = new CommandeAdapter(this,R.layout.commande_list_item,commandes);
        mCommandeAdapterPret = new CommandeAdapter(this,R.layout.commande_list_item_pret,commandesPrete);
        commandeList.setAdapter(mCommandeAdapter);
        commandeListPrete.setAdapter(mCommandeAdapterPret);
        commandeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer la commande sélectionné
                Commande selectedCommande = commandes.get(position);
                System.out.println(selectedCommande);
                Bundle args = new Bundle();
                args.putInt("ouiouioui", selectedCommande.getIdCommande());



                showNavigationDialog(args);
            }
        });
    }



    private void showNavigationDialog(Bundle args) {
        ValidationDialogFragment dialogFragment = new ValidationDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "validationDialog");
    }


    public void supprimerCommandePrete(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.supprimerCommandePrete();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("les commandes pretes sont supprimer");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}