package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ModifierProfilActivity extends AppCompatActivity {

    private User user;
    private TextView mUsernameEditText;
    private EditText mNameEditText;
    private EditText mFirstNameEditText;
    private TextView mEmailEditText;
    private EditText mAddressEditText;
    private EditText mCityEditText;
    private Button mConfirmationButton;

    private EditText mOldEditText;
    private EditText mNewEditText;
    private EditText mNewCEditText;

    private TextView mErrorMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        user = gson.fromJson(json, User.class);
        System.out.println(user.toString());

        mUsernameEditText = findViewById(R.id.edit_username);
        mUsernameEditText.setText(user.getUsername());

        mNameEditText = findViewById(R.id.edit_name);
        mNameEditText.setText(user.getName().toString());

        mFirstNameEditText = findViewById(R.id.edit_firstName);
        mFirstNameEditText.setText(user.getFisrtname().toString());

        mEmailEditText = findViewById(R.id.edit_email);
        mEmailEditText.setText(user.getEmail());

        mAddressEditText = findViewById(R.id.edit_address);
        mAddressEditText.setText(user.getAddress().toString());

        mCityEditText = findViewById(R.id.edit_city);
        mCityEditText.setText(user.getCity().toString());

        mConfirmationButton = findViewById(R.id.confirmationButton);

        mOldEditText = findViewById(R.id.edit_oldpass);
        mNewEditText = findViewById(R.id.edit_newpass);
        mNewCEditText = findViewById(R.id.edit_newCpass);
        mErrorMessage = findViewById(R.id.errorMsg_textView);

        mConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameEditText.getText().toString();
                String newpass = mNewEditText.getText().toString();
                String oldpass = mOldEditText.getText().toString();
                String newCpass = mNewCEditText.getText().toString();
                String city = mCityEditText.getText().toString();
                String address = mAddressEditText.getText().toString();
                String name = mNameEditText.getText().toString();
                String firstname= mFirstNameEditText.getText().toString();


                if(checkForm( newpass,oldpass, city, address, name, firstname))
                    modificationProfil(username,newpass,city,address,name,firstname, oldpass, newCpass);
            }


        });


    }

    public static boolean isPasswordValid(String password) {
        if (password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&*]).{8,}$")) {
            return true;
        } else {
            return false;
        }
    }


    public void modificationProfil(String username, String newpass, String city,String address,String name , String firtName, String oldpass, String newCpass) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.modification(username, newpass,name,firtName, address,city, oldpass, newCpass);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    System.out.println(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        mErrorMessage.setText(jsonObject.getString("msg"));
                    } else {
                        // modification réussite
                        String successMessage = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        editor.putString("user", json);
                        editor.apply();
                        finish();
                        Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                        startActivity(registerActivity);

                    }
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

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("modification.php")
        Call<ResponseBody> modification(
                @Field("username") String username,
                @Field("newpass") String newpass,
                @Field("name") String name,
                @Field("firstName") String firstName,
                @Field("address") String address,
                @Field("city") String city,
                @Field("oldpass") String oldpass,
                @Field("newCpass") String newCpass
        );
    }

    public boolean checkForm(String password, String oldpassword,String city,String address,String name,String firstname){

        if (name.isEmpty()){
            mErrorMessage.setText("veuillez entrer votre nom");
        }
        else if (firstname.isEmpty()){
            mErrorMessage.setText("veuillez entrer votre prénom");
        }
        else if(!isPasswordValid(password)) {
            if(!oldpassword.isEmpty()) {
                mErrorMessage.setTextSize(10);
                mErrorMessage.setText("Votre mot de passe doit contenir Contient au moins 8 caractères\n" +
                        "Contient au moins une majuscule\n" +
                        "Contient au moins une minuscule\n" +
                        "Contient au moins un chiffre\n" +
                        "Contient au moins un caractère spécial");
            }
        } else if (city.isEmpty()) {
            mErrorMessage.setText("veuillez entrer une ville");
        } else if (address.isEmpty()) {
            mErrorMessage.setText("veuillez entrez votre adresse");
        }
        return true;
    }



}