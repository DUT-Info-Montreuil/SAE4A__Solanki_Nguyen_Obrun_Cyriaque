package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MdpOublieActivity extends AppCompatActivity {

    private Button confirmButton;

    private EditText emailEditText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdp_oublie);

        confirmButton = findViewById(R.id.confirm_button);
        emailEditText = findViewById(R.id.email_edittext);



    }
}