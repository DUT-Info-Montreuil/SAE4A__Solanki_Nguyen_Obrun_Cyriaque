package com.example.burgger.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.MainActivity;
import com.example.burgger.R;
import com.example.burgger.object.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class ProfilActivity extends AppCompatActivity {
    private User user;

    private TextView mUserNameTextView;
    private TextView mNameTextView;
    private TextView mFirstNameTextView;
    private TextView mEmailTextView;
    private TextView mAddressTextView;
    private TextView mCityTextView;

    private Button mEditProfilButton;

    private ImageView mRetour,mProfilPhoto, mDeco;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);

        mUserNameTextView = findViewById(R.id.textViewUserName);
        mUserNameTextView.setText("Pseudo : " + user.getUsername());

        mNameTextView = findViewById(R.id.textViewName);
        mNameTextView.setText("Nom : "+user.getName());

        mFirstNameTextView = findViewById(R.id.textViewFirstName);
        mFirstNameTextView.setText("Prénom : "+user.getFisrtname());

        mEmailTextView = findViewById(R.id.textViewEmail);
        mEmailTextView.setText("Email : "+user.getEmail());

        mAddressTextView = findViewById(R.id.textViewAddress);
        mAddressTextView.setText("Adresse : "+user.getAddress());

        mCityTextView = findViewById(R.id.textViewCity);
        mCityTextView.setText("Ville : "+user.getCity());

        mEditProfilButton = findViewById(R.id.editButton);

        mProfilPhoto = findViewById(R.id.imageViewPP);


        String imageUrl = "https://burgerr7.000webhostapp.com/img/"+user.getUsername()+".png";
        System.out.println(imageUrl);
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(mProfilPhoto, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                mProfilPhoto.invalidate();
            }

            @Override
            public void onError(Exception e) {
                mProfilPhoto.setImageResource(getApplicationContext().getResources().getIdentifier("userlogo", "drawable", getApplicationContext().getPackageName()));

            }
        });


        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


        mEditProfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent registerActivity = new Intent(getApplicationContext(), ModifierProfilActivity.class);
                startActivity(registerActivity);

            }
        });

        mRetour = findViewById(R.id.imageViewRetour);
        mRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDeco = findViewById(R.id.imageViewDeco);
        mDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.apply();
                finish();
                Intent registerActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(registerActivity);

            }
        });

    }





}