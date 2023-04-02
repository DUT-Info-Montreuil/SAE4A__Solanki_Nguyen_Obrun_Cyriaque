package com.example.burgger.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.MainActivity;
import com.example.burgger.ProfilActivity;
import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.Burger;
import com.example.burgger.object.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBurgerActivity extends AppCompatActivity {

    private EditText nameEditText, priceEditText, descriptionEditText,reductionEditText;
    private ImageView photoImageView;
    private TextView nameTextView, priceTextView, descriptionTextView,reductiontextView;

    private  Burger burger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_burger);

        nameEditText = findViewById(R.id.edit_burger_name);
        priceEditText = findViewById(R.id.edit_burger_price);
        descriptionEditText = findViewById(R.id.edit_burger_description);
        photoImageView = findViewById(R.id.edit_burger_photo_preview);
        nameTextView = findViewById(R.id.edit_burger_name);
        reductionEditText= findViewById(R.id.edit_burger_reduction);
        descriptionTextView = findViewById(R.id.edit_burger_description);
        // Récupération des données du burger à partir des SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("burger", "");
        burger = gson.fromJson(json, Burger.class);
        System.out.println(burger.getDesription());

        nameEditText.setText(burger.getBurgerNamme());
        priceEditText.setText(String.valueOf(burger.getPrice()));
        reductionEditText.setText(String.valueOf(burger.getReduction()));
        descriptionEditText.setText(burger.getDesription());
        photoImageView.setImageResource(this.getResources().getIdentifier(burger.getPhoto(), "drawable", this.getPackageName()));

        Button updateButton = findViewById(R.id.edit_burger_save_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBurger();
            }
        });

    }

    @Override
    public void onBackPressed() {
            Intent intent = new Intent(this, ManageBurgerActivity.class);
            startActivity(intent);
            finish();
    }


    public void editBurger() {
        // Créer une instance de RetrofitClientInstance
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        // Appeler la méthode editBurger de l'interface d'API
        Call<ResponseBody> call = apiInterface.editBurger(
                nameEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                Double.parseDouble(priceEditText.getText().toString()),
                Double.parseDouble(reductionEditText.getText().toString()),
                burger.getPhoto(),
                burger.getId_burger()
        );

        // Exécuter la requête de manière asynchrone
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    finish();
                    Intent intent = new Intent(getApplicationContext(), ManageBurgerActivity.class);

                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Burger modifié",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Traitement de l'erreur
                // Afficher un message d'erreur ou une boîte de dialogue pour demander à l'utilisateur de réessayer
            }
        });
    }

}
