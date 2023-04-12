package com.example.burgger.cart;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.commande.CommandeActivity;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.Burger;
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


public class CartFragment extends Fragment {

    private ImageView profilImageView;
    private CartBurgerAdapter burgerListAdapter;
    private ArrayList<Burger> cart;
    private ListView burgersListView;
    private TextView totalTextView, textAucuneCommande;
    private ImageView burgerList, promotion;

    private Button buttonContinuer;

    private double total=0;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        buttonContinuer = view.findViewById(R.id.continuer_button);
        burgersListView = view.findViewById(R.id.burger_list_view);
        textAucuneCommande = view.findViewById(R.id.aucuneCommandeCart);
        totalTextView = view.findViewById(R.id.TotaltextView);
        // Inflate the layout for this fragment
        setOnclick();
        initializeCart();
        return view;
    }




    private void setOnclick(){



        buttonContinuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Vous n'avez pas de commandes", Toast.LENGTH_LONG).show();
                    System.out.println("oui");
                }else{
                    Intent profilActivity = new Intent(getActivity().getApplicationContext(), CommandeActivity.class);
                    startActivity(profilActivity);
                    getActivity().finish();
                }
            }
        });
    }


    public   void initializeCart(){

        cart=new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);
        getCart(user.getId_user());

        burgerListAdapter = new CartBurgerAdapter(getActivity().getApplicationContext(),R.layout.cart_list_item,cart,user.getId_user(),totalTextView,cart, textAucuneCommande);
        burgersListView.setAdapter(burgerListAdapter);
        burgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = cart.get(position);
                System.out.println(selectedBurger);
            }
        });

    }


    public void getCart(int id_user){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCart(id_user);
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

                        System.out.println(jsonString);

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        System.out.println(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject burgerObject = jsonArray.getJSONObject(i);
                            int burgerId = burgerObject.getInt("id_burger");
                            String burgerName = burgerObject.getString("burgername");
                            Double burgerPrice = burgerObject.getDouble("price");
                            String burgerPhoto = burgerObject.getString("photo");
                            String burgerDescription = burgerObject.getString("description");
                            Double reduction = burgerObject.getDouble("COALESCE(reduction, 0)");
                            if (reduction == 0) {
                                cart.add(new Burger(burgerId, burgerName, burgerPrice, burgerPhoto, burgerDescription, burgerObject.getInt("quantity")));
                                total += burgerObject.getInt("quantity") * burgerPrice;
                            } else {
                                Double prixReduction = burgerPrice - ((reduction / 100) * burgerPrice);
                                cart.add(new Burger(burgerId, burgerName, prixReduction, burgerPhoto, burgerDescription, burgerObject.getInt("quantity")));
                                total += burgerObject.getInt("quantity") * prixReduction;
                            }

                        }

                        burgerListAdapter.notifyDataSetChanged();
                        totalTextView.setText("Total : " + total + " €");
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