package com.example.burgger.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.burgger.BurgerDetailFragment;
import com.example.burgger.MainActivity;
import com.example.burgger.R;
import com.example.burgger.api.RetrofitClientInstance;
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


public class BurgerFragment extends Fragment {

    private User user;

    private BurgerAdapter burgerListAdapter;

    private ArrayList<Burger> burgers;

    private ImageView mPromotionImageView,mBurgerImageview;

    private  ListView burgersListView;

    private  ImageView profilImageView,cartImageView;

    public BurgerFragment() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_burger, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);
        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        burgersListView = view.findViewById(R.id.burger_list_view);
        initializeBurger();
        return view;
    }

    public void getAllBurgers(){
        HomeActivity.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(HomeActivity.ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getBurgers();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject burgerObject = jsonArray.getJSONObject(i);
                        int burgerId = burgerObject.getInt("id_burger");
                        String burgerName = burgerObject.getString("burgername");
                        Double burgerPrice = burgerObject.getDouble("price");
                        String burgerPhoto = burgerObject.getString("photo");
                        String burgerDescription = burgerObject.getString("description");
                        Double reduction = burgerObject.getDouble("COALESCE(reduction, 0)");
                        System.out.println(reduction);
                        if(reduction == 0){
                            burgers.add(new Burger(burgerId,burgerName,burgerPrice,burgerPhoto,burgerDescription));
                        }else{
                            Double prixReduction = burgerPrice - ( (reduction/100)*burgerPrice );
                            burgers.add(new Burger(burgerId,burgerName,prixReduction,burgerPhoto,burgerDescription));
                        }
                    }
                    burgerListAdapter.notifyDataSetChanged();
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
        burgers=new ArrayList<>();
        getAllBurgers();
        burgerListAdapter = new BurgerAdapter(getActivity().getApplicationContext(),R.layout.burger_list_item,burgers);
        burgersListView.setAdapter(burgerListAdapter);
        burgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Burger selectedBurger = burgers.get(position);
                System.out.println(selectedBurger);
                // Ouvrir une nouvelle activité avec les détails du burger sélectionné

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(user);
                editor.putString("burger_price", ""+selectedBurger.getPrice());
                editor.putString("burger_name", selectedBurger.getBurgerNamme());
                editor.putString("burger_photo", selectedBurger.getPhoto());
                editor.putString("burger_description", selectedBurger.getDesription());
                editor.putInt("burger_id",selectedBurger.getId_burger());
                editor.apply();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_framelayout, new BurgerDetailFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}