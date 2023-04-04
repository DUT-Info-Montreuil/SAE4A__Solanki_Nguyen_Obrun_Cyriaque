package com.example.burgger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Toast;

import com.example.burgger.api.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TESTDRIVE extends AppCompatActivity {
//    private static final int REQUEST_PERMISSIONS_CODE = 1;
//    private Uri mImageUri;
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_testdrive);
//
//        requestStoragePermissions();
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSIONS_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openFileChooser();
//            } else {
//                Toast.makeText(this, "Permissions are required to access files", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void requestStoragePermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);
//            }
//        } else {
//            openFileChooser();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            mImageUri = data.getData();
//            uploadImage();
//        }
//    }
//
//    private void uploadImage() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://burgerr7.000webhostapp.com/")
//                .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//
//        try (InputStream inputStream = getContentResolver().openInputStream(mImageUri)) {
//            if (inputStream != null) {
//                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//                int nRead;
//                byte[] data = new byte[16384];
//                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
//                    buffer.write(data, 0, nRead);
//                }
//                buffer.flush();
//                byte[] byteArray = buffer.toByteArray();
//
//                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), byteArray);
//                MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", getFileName(mImageUri), requestBody);
//
//                Call<ResponseBody> call = apiInterface.uploadImage(filePart,"eede");
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            Toast.makeText(TESTDRIVE.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(TESTDRIVE.this, "Error uploading image", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(TESTDRIVE.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    @SuppressLint("Range")
//    private String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                if (cursor == null) {
//                    cursor.close();
//                }
//            }
//        }        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
}