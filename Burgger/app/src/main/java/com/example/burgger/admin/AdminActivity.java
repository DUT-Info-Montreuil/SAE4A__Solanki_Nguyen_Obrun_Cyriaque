package com.example.burgger.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.burgger.R;

public class AdminActivity extends AppCompatActivity {

    private Button manageUser,mangeBurger,addBurger;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageUser = findViewById(R.id.manageUser_button);

        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivity = new Intent(getApplicationContext(), ManageUserActivity.class);
                startActivity(manageActivity);
            }
        });

        mangeBurger = findViewById(R.id.manageBurger_button);

        mangeBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivity = new Intent(getApplicationContext(), ManageBurgerActivity.class);
                startActivity(manageActivity);
            }
        });

        addBurger = findViewById(R.id.addBurger_button);

        addBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBurgerActivity = new Intent(getApplicationContext(), AddBurgerActivity.class);
                startActivity(addBurgerActivity);
            }
        });

    }
}