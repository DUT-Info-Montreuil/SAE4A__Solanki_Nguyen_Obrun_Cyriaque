package com.example.burgger.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgger.MainActivity;
import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.Burger;

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

public class AddBurgerActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextDescription;

    private ImageView imgBuger;

    private TextView errorMsgTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_burger);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        imgBuger = findViewById(R.id.imageViewBurgerADD);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        editTextPrice.setFilters(new InputFilter[] { filter });
        editTextDescription = findViewById(R.id.editTextDescription);
        errorMsgTextView = findViewById(R.id.errorMsg_textView);

        Button imgButton = findViewById(R.id.buttonSelectImage);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        Button buttonAddBurger = findViewById(R.id.buttonAddBurger);
        buttonAddBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    String name = editTextName.getText().toString().trim();
                    String priceString = editTextPrice.getText().toString().trim();
                    double price = Double.parseDouble(priceString);
                    String description = editTextDescription.getText().toString().trim();

                    addBurger(name,price,description);
                    uploadImage();
                }


            }
        });
    }

    public void addBurger(String name,double price,String desription){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addBurger(name, desription,price);
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
                        Intent intent=new Intent(getApplicationContext(), AdminActivity.class);
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
    private boolean checkFields() {
        String name = editTextName.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (name.isEmpty()) {
            errorMsgTextView.setText("Veuillez entrer un nom pour le burger.");
            return false;
        }

        if (priceString.isEmpty()) {
            errorMsgTextView.setText("Veuillez entrer un prix pour le burger.");
            return false;
        }

        if (description.isEmpty()) {
            errorMsgTextView.setText("Veuillez entrer une description pour le burger.");
            return false;
        }

        return true;
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
            imgBuger.setImageBitmap(bitmap);
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
            RequestBody photoName = RequestBody.create(MediaType.parse("text/plain"),editTextName.getText().toString() );



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