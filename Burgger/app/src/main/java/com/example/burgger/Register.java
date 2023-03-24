package com.example.burgger;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
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

    private EditText nameEditText;

    private EditText firstNameEditText;
    private EditText passwordEditText;
    private EditText ConfirmpasswordEditText;
    private EditText emailEditText;

    private EditText cityEditText;

    private EditText addressEditText;
    private TextView errorMsgTextView,acceuilButton ;
    private CheckBox acceptConditionsCheckBox;
    private      Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = findViewById(R.id.username_edittext);
        nameEditText = findViewById(R.id.name_edittext);
        firstNameEditText = findViewById(R.id.firstname_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        ConfirmpasswordEditText = findViewById(R.id.confirmpassword_edittext);
        acceptConditionsCheckBox=findViewById(R.id.acceptConditionsCheckBox);
        emailEditText = findViewById(R.id.email_edittext);
        errorMsgTextView = findViewById(R.id.errorMsg_textView);
        registerButton = findViewById(R.id.register_button);
        acceuilButton = findViewById(R.id.acceuil_button);
        addressEditText = findViewById(R.id.address_edittext);
        cityEditText = findViewById(R.id.city_edittext);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String firstname= firstNameEditText.getText().toString();


                if(checkForm( username,password, email,city,address,name,firstname))
                    RegisterUser(username,password,email,city,address,name,firstname);
            }
        });

        acceuilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
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
        String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("register.php")
        Call<ResponseBody> register(
                @Field("username") String username,
                @Field("password") String password,
                @Field("name") String name,
                @Field("firstName") String firstName,
                @Field("email") String email,
                @Field("address") String address,
                @Field("city") String city
        );

    }

    public void RegisterUser(String username, String password, String email,String city,String adress,String name , String firtName) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.register(username, password,name,firtName, email,adress,city);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    System.out.println(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    boolean success = jsonObject.getBoolean("success");
                    if (!success) {
                        errorMsgTextView.setText(jsonObject.getString("msg"));
                    } else {
                        // Inscription réussie
                        String successMessage = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

    public boolean checkForm(String username,String password,String email,String city,String adresse,String name,String firstname){

        if (!isValidEmail(email)){
            errorMsgTextView.setText("Email invalide");
        }else if (name.isEmpty()){
            errorMsgTextView.setText("veuillez votre nom");
        }
        else if (firstname.isEmpty()){
            errorMsgTextView.setText("veuillez votre prénom");
        }
        else if(!isPasswordValid(password)) {
            errorMsgTextView.setTextSize(15);
            errorMsgTextView.setText("Votre mot de passe doit contenir Contient au moins 8 caractères\n" +
                    "Contient au moins une majuscule\n" +
                    "Contient au moins une minuscule\n" +
                    "Contient au moins un chiffre\n" +
                    "Contient au moins un caractère spécial");
        } else if (city.isEmpty()) {
            errorMsgTextView.setText("veuillez entrer une ville");
        } else if (adresse.isEmpty()) {
            errorMsgTextView.setText("veuillez entrez votre adresse");
        } else if (!password.equals(ConfirmpasswordEditText.getText().toString())) {
            errorMsgTextView.setText("Vos mot de passes ne sont pas indentiques");
        } else if (username.isEmpty()) {
            errorMsgTextView.setText("veuillez entrer un nom d'utilisateur");
        } else if (!acceptConditionsCheckBox.isChecked()) {
            errorMsgTextView.setText("veuillez accepter les conditions d'utilisations");
        }
        return true;
    }

}

