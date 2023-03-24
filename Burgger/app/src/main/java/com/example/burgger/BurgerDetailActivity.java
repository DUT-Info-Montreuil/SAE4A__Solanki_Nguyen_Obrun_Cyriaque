package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BurgerDetailActivity extends AppCompatActivity {

    private ImageView burgerImageView;

    private TextView burgerNameTextView, burgerPriceTextView,burgerDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger_detail);

        burgerImageView = findViewById(R.id.burger_detail_photo);
        burgerNameTextView = findViewById(R.id.burger_detail_name);
        burgerPriceTextView = findViewById(R.id.burger_detail_price);
        burgerDescriptionTextView = findViewById(R.id.burger_detail_description);



        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String burger_name = sharedPreferences.getString("burger_name", "");
        String burger_price = sharedPreferences.getString("burger_price", "");
        String burger_photo = sharedPreferences.getString("burger_photo", "");
        String burger_description = sharedPreferences.getString("burger_description", "");

        System.out.println(burger_name+"\n\n\n\n\n");
        burgerImageView.setImageResource(this.getResources().getIdentifier(burger_photo, "drawable", this.getPackageName()));
        burgerNameTextView.setText(burger_name);
        burgerPriceTextView.setText(burger_price+"€");
        burgerDescriptionTextView.setText(burger_description);

    }
}