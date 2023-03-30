package com.example.burgger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class UserAdapter extends ArrayAdapter<User> {
private  Context mContext;
private  int mResource;


    public UserAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        mContext = context;
        mResource = resource;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Button dellUserButton = view.findViewById(R.id.dellUserButton);

        User user = getItem(position);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        ImageView photoImageView = view.findViewById(R.id.photoImageView);

        nameTextView.setText(user.getUsername());
        photoImageView.setImageResource(mContext.getResources().getIdentifier("userlogo", "drawable", mContext.getPackageName()));

        dellUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation");
                builder.setMessage("Voulez vous vraiment supprimer l'utilisateur id:"+user.getId_user()+" "+user.getUsername());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserAdapter.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(UserAdapter.ApiInterface.class);
                        Call<ResponseBody> call = apiInterface.dellUser(user.getId_user());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(mContext, "Utilisateur supprimer", Toast.LENGTH_LONG).show();
                                remove(user);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        return view;

    }

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("dellUser.php")
        Call<ResponseBody> dellUser(
                @Field("id_user") int id_user
        );
    }
}
