package com.example.burgger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorMsgTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        errorMsgTextView = findViewById(R.id.errorMsg_textView);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty())
                    errorMsgTextView.setText("Veuillez entrer un nom d'utilisateur");
                else
                    loginUser(username,password);
            }
        });

        TextView registerButton=findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), Register.class);
                startActivity(registerActivity);
            }
        });
    }
    public void loginUser(String username, String password) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        errorMsgTextView.setText(jsonObject.getString("msg"));
                    } else {
                        // Connexion réussie
                        String successMessage = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
                        JSONObject result = jsonObject.getJSONObject("result");
                        int idUser = result.getInt("id_user");


                        // Stocker l'ID utilisateur dans SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", idUser);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
        @POST("login.php")
        Call<ResponseBody> login(
                @Field("username") String username,
                @Field("password") String password
        );
    }


}



