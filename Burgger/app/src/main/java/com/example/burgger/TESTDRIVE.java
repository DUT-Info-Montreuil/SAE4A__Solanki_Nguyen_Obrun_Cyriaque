package com.example.burgger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public class TESTDRIVE extends AppCompatActivity {

    private ImageView img;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testdrive);
        openFileChooser();
    }
    // Ouvrir la galerie pour sélectionner une photo
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Récupérer l'URI de l'image sélectionnée
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            uploadImage(); // Appeler la méthode pour envoyer la photo à l'API
        }
    }

    private void uploadImage() {
        // Créer une instance de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://burgerr7.000webhostapp.com/")
                .build();

        // Créer une instance de l'interface de l'API
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        // Obtenir le chemin réel de l'URI de l'image sélectionnée
        String filePath = getRealPathFromURI(mImageUri);

        // Créer une instance de la classe File à partir du chemin de l'image
        File file = new File(filePath);

        // Créer une instance de la classe RequestBody à partir du fichier
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Créer une instance de la classe MultipartBody.Part à partir de la RequestBody
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        // Envoyer la photo à l'API
        Call<ResponseBody> call = apiInterface.uploadImage(filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // La photo a été envoyée avec succès
                } else {
                    // Il y a eu une erreur lors de l'envoi de la photo
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface ApiInterface {
        @Multipart
        @POST("upload-picture.php")
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }
}



