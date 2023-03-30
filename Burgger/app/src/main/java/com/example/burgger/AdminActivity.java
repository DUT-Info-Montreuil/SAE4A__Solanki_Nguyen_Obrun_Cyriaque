package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button manageUser,mangeBurger;

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

    }
}