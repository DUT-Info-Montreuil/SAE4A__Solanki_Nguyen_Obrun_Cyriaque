package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    private int userId;
    private ImageView profilImawgeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profilImawgeView=findViewById(R.id.imageViewProfil);
        profilImawgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(registerActivity);

            }
        });

        // Récupérer l'ID utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            // L'ID utilisateur n'a pas été trouvé dans SharedPreferences, renvoyer l'utilisateur à l'écran de connexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        System.out.println(userId);
    }

    // Utiliser l'ID utilisateur
    // ...
}
