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

        mUsernameEditText = findViewById(R.id.edit_username);
        mUsernameEditText.setText(user.getUsername());

        mNameEditText = findViewById(R.id.edit_name);
        mNameEditText.setHint(user.getName());

        mFirstNameEditText = findViewById(R.id.edit_firstName);
        mFirstNameEditText.setHint(user.getFisrtname());

        mEmailEditText = findViewById(R.id.edit_email);
        mEmailEditText.setText(user.getEmail());

        mAddressEditText = findViewById(R.id.edit_address);
        mAddressEditText.setHint(user.getAddress());

        mCityEditText = findViewById(R.id.edit_city);
        mCityEditText.setHint(user.getCity());

        mConfirmationButton = findViewById(R.id.confirmationButton);

        mOldEditText = findViewById(R.id.edit_oldpass);
        mNewEditText = findViewById(R.id.edit_newpass);
        mNewCEditText = findViewById(R.id.edit_newCpass);
        mErrorMessage = findViewById(R.id.errorMsg_textView);

        mConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ne peut pas avoir le meme pseudo et la meme adresse mail + verification de l'adresse mail

            }
        });


    }

    public static boolean isPasswordValid(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public void modificationProfil(String username, String newpass, String city,String adress,String name , String firtName, String oldpass, String newCpass) {
        ModifierProfilActivity.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ModifierProfilActivity.ApiInterface.class);
        Call<ResponseBody> call = apiInterface.modifierProfil(username, newpass,name,firtName, adress,city, oldpass, newCpass);
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
                        Intent intent=new Intent(getApplicationContext(),ProfilActivity.class);
                        startActivity(intent);
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
        Call<ResponseBody> modifierProfil(
                @Field("username") String username,
                @Field("newpass") String newpass,
                @Field("name") String name,
                @Field("firstName") String firstName,
                @Field("address") String address,
                @Field("city") String city,
                @Field("olspass") String oldpass,
                @Field("newCpass") String newCpass
        );
    }



}