package com.example.burgger;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import org.json.JSONArray;
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

public class ValidationDialogFragment  extends DialogFragment {





    public interface ApiInterface {

        @FormUrlEncoded
        @POST("majCommandePret.php")
        Call<ResponseBody> majPret(@Field("idCommande") int idCommande);

    }

    public void majCommandePrete(int idCommande){
        CuisineActivity.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(CuisineActivity.ApiInterface.class);
        Call<ResponseBody> call = apiInterface.majPret(idCommande);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("commande prête");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Validation")
                .setItems(new CharSequence[]{"OUI", "NON"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Bundle args = getArguments();
                            int idCommande = args.getInt("ouiouioui");
                            majCommandePrete(idCommande);
                            System.out.println("commande " + idCommande + " prete");
                            getActivity().finish();
                            Intent intent = new Intent(requireActivity(), CuisineActivity.class);
                            startActivity(intent);

                        } else {
                            System.out.println("pas valider");

                        }
                    }
                });
        return builder.create();
    }
}
