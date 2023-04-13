package com.example.burgger;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.home.BurgerFragment;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BurgerDetailFragment extends Fragment {
    private ImageView burgerImageView;

    private TextView burgerNameTextView, burgerPriceTextView,burgerDescriptionTextView;

    private Button addBurgerToCartButton;


    public BurgerDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_burger_detail, container, false);

        burgerImageView = view.findViewById(R.id.burger_detail_photo);
        burgerNameTextView = view.findViewById(R.id.burger_detail_name);
        burgerPriceTextView = view.findViewById(R.id.burger_detail_price);
        burgerDescriptionTextView = view.findViewById(R.id.burger_detail_description);
        addBurgerToCartButton = view.findViewById(R.id.addBurgerToCart);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String burger_name = sharedPreferences.getString("burger_name", "");
        String burger_price = sharedPreferences.getString("burger_price", "");
        String burger_photo = sharedPreferences.getString("burger_photo", "");
        String burger_description = sharedPreferences.getString("burger_description", "");
        int burger_id = sharedPreferences.getInt("burger_id",0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);

        System.out.println(burger_name+"\n\n\n\n\n");

        String imageUrl = "https://burgerr7.000webhostapp.com/img/" + burger_name.replaceAll("\\s", "%20") + ".png";
// Ajouter un paramètre aléatoire à l'URL de l'image
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(burgerImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                burgerImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                // Gérer les erreurs de chargement de l'image
            }
        });

        burgerNameTextView.setText(burger_name);
        burgerPriceTextView.setText(burger_price+"€");
        burgerDescriptionTextView.setText(burger_description);

        System.out.println(burger_id);
        addBurgerToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBurgerToCart(burger_id,user.getId_user());
            }
        });
        return view;
    }
    private void addBurgerToCart(int id_burger,int id_user){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addBurgerToCart(id_user,id_burger);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Toast.makeText(getActivity().getApplicationContext(),"burger ajouter au panier",Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_framelayout, new BurgerFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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