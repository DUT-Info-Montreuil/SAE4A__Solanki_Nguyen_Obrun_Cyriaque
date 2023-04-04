package com.example.burgger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private Button mConfirmationButton,mchangePhoto;

    private EditText mOldEditText;
    private EditText mNewEditText;
    private EditText mNewCEditText;

    private TextView mErrorMessage;

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int REQUEST_IMAGE_PICK = 101;

    private Uri mImageUri;

    private ImageView mImageView;
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

        mImageView = findViewById(R.id.photo_preview);
        mchangePhoto = findViewById(R.id.select_photo);
        mchangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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
                        JSONObject result = jsonObject.getJSONObject("result");
                        String name = result.getString("name");
                        String fisrtName = result.getString("firstname");
                        String address = result.getString("address");
                        String city = result.getString("city");
                        int id_user = result.getInt("id_user");

                        user.setId_user(id_user);
                        user.setAddress(address);
                        user.setCity(city);
                        user.setFisrtname(fisrtName);
                        user.setName(name);


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
                        uploadImage();
                        Intent registerActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                        startActivity(registerActivity);
                        finish();

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

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission required to access files", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(mImageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void uploadImage() {
        if (mImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(mImageUri);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), byteArray);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", getFileName(mImageUri), requestBody);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            RequestBody photoName = RequestBody.create(MediaType.parse("text/plain"),user.getUsername());
            RequestBody id_user = RequestBody.create(MediaType.parse("text/plain"),""+user.getId_user());



            Call<ResponseBody> call = apiInterface.uploadImageProfil(filePart,photoName,id_user);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String jsonString = null;
                        try {
                            jsonString = response.body().string();
                            user.setPhoto(jsonString);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}