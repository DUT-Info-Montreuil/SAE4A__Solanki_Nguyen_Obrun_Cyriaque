package com.example.burgger;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import retrofit2.http.POST;

public class CuisineActivity extends AppCompatActivity {

    private User user;
    private ArrayList<Commande> commandes;
    private CommandeAdapter mCommandeAdapter;
    private ListView commandeList;
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

        initializeBurger();
    }

    public interface ApiInterface {
        @POST("getCommandes.php")
        Call<ResponseBody> getCommandes();
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
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject burgerObject = jsonArray.getJSONObject(i);
                        int commandeId = burgerObject.getInt("id_commande");
                        int burgerId = burgerObject.getInt("id_burger");
                        String modification = burgerObject.getString("modification");
                        int pret = burgerObject.getInt("pret");
                        boolean p;
                        if(pret==0)
                            p=false;
                        else
                            p=true;

                        commandes.add(new Commande(commandeId,burgerId,modification,p));
                    }
                    mCommandeAdapter.notifyDataSetChanged();
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


    private  void initializeBurger(){
        commandes=new ArrayList<>();
        getAllCommandes();
        mCommandeAdapter = new CommandeAdapter(this,R.layout.commande_list_item,commandes);
        commandeList.setAdapter(mCommandeAdapter);
        commandeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer la commande sélectionné
                Commande selectedCommande = commandes.get(position);
                System.out.println(selectedCommande);
                LinearLayout ln = (LinearLayout) parent.getChildAt(position);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_corners_vert);
                ln.setBackground(drawable);
                
                // Ouvrir une nouvelle activité avec les détails du burger sélectionné


            }
        });
    }


}