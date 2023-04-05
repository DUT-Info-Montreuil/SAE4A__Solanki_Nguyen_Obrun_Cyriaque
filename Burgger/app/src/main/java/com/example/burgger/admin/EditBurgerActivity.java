package com.example.burgger.admin;

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

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.Burger;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBurgerActivity extends AppCompatActivity {

    private EditText nameEditText, priceEditText, descriptionEditText,reductionEditText;
    private ImageView photoImageView;
    private TextView nameTextView, priceTextView, descriptionTextView,reductiontextView;

    private static final int REQUEST_PERMISSIONS_CODE = 1;

    private static final int PICK_IMAGE_REQUEST = 1;

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
        String imageUrl = "https://burgerr7.000webhostapp.com/img/" + burger.getBurgerNamme().replaceAll("\\s", "%20") + ".png";
// Ajouter un paramètre aléatoire à l'URL de l'image
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(photoImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                photoImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                // Gérer les erreurs de chargement de l'image
            }
        });


        Button photoButton = findViewById(R.id.edit_burger_select_photo);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


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
                nameEditText.getText().toString(),
                burger.getId_burger()
        );

        uploadImage();

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




    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int REQUEST_IMAGE_PICK = 101;

    private Uri mImageUri;

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
            photoImageView.setImageBitmap(bitmap);
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
            RequestBody photoName = RequestBody.create(MediaType.parse("text/plain"),nameEditText.getText().toString() );



            Call<ResponseBody> call = apiInterface.uploadImage(filePart,photoName);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
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
