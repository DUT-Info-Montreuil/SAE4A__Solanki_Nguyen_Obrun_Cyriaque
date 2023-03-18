package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        errorMsgTextView=findViewById(R.id.errorMsg_textView);
        registerButton = findViewById(R.id.register_button);
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
                if (isPasswordValid(passwordEditText.getText().toString())) {
                    registerButton.setEnabled(true);
                    errorMsgTextView.setText("");
                }
                else{
                    registerButton.setEnabled(false);
                    errorMsgTextView.setText("Votre mot de passe doit contenir Contient au moins 8 caractères\n" +
                            "Contient au moins une majuscule\n" +
                            "Contient au moins une minuscule\n" +
                            "Contient au moins un chiffre\n" +
                            "Contient au moins un caractère spécial");
                }

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




}