package com.example.burgger.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.burgger.BurgerDetailFragment;
import com.example.burgger.MainActivity;
import com.example.burgger.cart.CartFragment;
import com.example.burgger.profil.ProfilActivity;
import com.example.burgger.R;
import com.example.burgger.object.Burger;
import com.example.burgger.object.User;
import com.example.burgger.promotion.PromotionFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

public class HomeActivity extends AppCompatActivity {

    private User user;

    private BurgerAdapter burgerListAdapter;

    private ArrayList<Burger> burgers;

    private ImageView mPromotionImageView,mBurgerImageview;

    private  ListView burgersListView;

    private  ImageView profilImageView,cartImageView;

    private PromotionFragment promotionFragment = new PromotionFragment();
    private BurgerFragment burgerFragment = new BurgerFragment();
    private CartFragment cartFragment = new CartFragment();

    public BurgerDetailFragment burgerDetailFragment = new BurgerDetailFragment();

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, burgerFragment);
        fragmentTransaction.commit();
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

         profilImageView = findViewById(R.id.imageViewProfil);
         cartImageView = findViewById(R.id.cartimageView);


         setOnclick();




        profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(registerActivity);

            }
        });

        mPromotionImageView = findViewById(R.id.imageViewPromotion);
        mPromotionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_framelayout, promotionFragment);
                fragmentTransaction.commit();
            }
        });
        mBurgerImageview = findViewById(R.id.imageViewBurger);
        mBurgerImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_framelayout, burgerFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public interface ApiInterface {

        @POST("getBurgers.php")
        Call<ResponseBody> getBurgers();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Déconnecter l'utilisateur et renvoyer à l'écran de connexion
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.clear();
                        edit.apply();
                        finish();
                        Intent registerActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(registerActivity);
                    }
                })
                .setNegativeButton("Non", null)
                .show();

    }

    private void setOnclick(){
        profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(profilActivity);

            }
        });  cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_framelayout, cartFragment);
                fragmentTransaction.commit();

            }
        });
    }


}

