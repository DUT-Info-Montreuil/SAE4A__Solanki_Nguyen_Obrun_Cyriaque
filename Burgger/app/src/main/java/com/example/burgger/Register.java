package com.example.burgger;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class Register extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private TextView errorMsgTextView;
    private      Button registerButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        errorMsgTextView = findViewById(R.id.errorMsg_textView);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();

                User user = new User(username, password, email);

                ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
                Call<User> call = apiInterface.register(username,password,email);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        System.out.printf("suced\t\t\t\t");
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println(t.getMessage());

                    }
                });
            }
        });



        registerButton.setEnabled(false);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Cette méthode est appelée avant que le texte ne soit modifié
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidEmail(emailEditText.getText().toString())) {
                    registerButton.setEnabled(true);
                    errorMsgTextView.setText("");
                }
                else{
                    registerButton.setEnabled(false);
                    errorMsgTextView.setText("Votre  email est incorrect");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Cette méthode est appelée après que le texte a été modifié
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Cette méthode est appelée avant que le texte ne soit modifié
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (isPasswordValid(passwordEditText.getText().toString())) {
//                    registerButton.setEnabled(true);
//                    errorMsgTextView.setText("");
//                } else {
//                    registerButton.setEnabled(false);
//                    errorMsgTextView.setText("Votre mot de passe doit contenir Contient au moins 8 caractères\n" +
//                            "Contient au moins une majuscule\n" +
//                            "Contient au moins une minuscule\n" +
//                            "Contient au moins un chiffre\n" +
//                            "Contient au moins un caractère spécial");
//                }


            }
            @Override
            public void afterTextChanged(Editable s) {
                // Cette méthode est appelée après que le texte a été modifié
            }
        });

    }

    public static boolean isPasswordValid(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("register.php")
        Call<User>  register(
                @Field("username") String username,
                @Field("password") String password,
                @Field("email") String email
        );
    }


}

