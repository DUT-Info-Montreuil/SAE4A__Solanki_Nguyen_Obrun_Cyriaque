package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class ProfilActivity extends AppCompatActivity {
    private User user;

    private TextView mUserNameTextView;
    private TextView mNameTextView;
    private TextView mFirstNameTextView;
    private TextView mEmailTextView;
    private TextView mAddressTextView;
    private TextView mCityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);

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
        mCityTextView.setText("City : "+user.getCity());




        if (user.getId_user() == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        System.out.println(user);

    }





}