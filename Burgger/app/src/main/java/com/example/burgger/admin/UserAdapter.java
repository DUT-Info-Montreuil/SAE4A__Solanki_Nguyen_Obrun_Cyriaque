package com.example.burgger.admin;

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

import com.example.burgger.R;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.User;
import com.example.burgger.api.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

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
        Button banUserButton = view.findViewById(R.id.banUserButton);

        User user = getItem(position);

        if (user.isBan()){
            banUserButton.setText("DEBANNIR UTILISATEUR");
        }else {
            banUserButton.setText("BANNIR UTILISATEUR");
        }

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        ImageView photoImageView = view.findViewById(R.id.photoImageView);

        nameTextView.setText(user.getUsername());




        String imageUrl = "https://burgerr7.000webhostapp.com/img/"+user.getUsername()+".png";
        System.out.println(imageUrl);
        imageUrl += "?random=" + new Random().nextInt();
        Picasso.get().load(imageUrl).into(photoImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Appeler invalidate() pour rafraîchir l'image
                photoImageView.invalidate();
            }

            @Override
            public void onError(Exception e) {
                photoImageView.setImageResource(mContext.getResources().getIdentifier("userlogo", "drawable", mContext.getPackageName()));

            }
        });

        dellUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation");
                builder.setMessage("Voulez vous vraiment supprimer l'utilisateur id:"+user.getId_user()+" "+user.getUsername());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
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


        banUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation");
                if (!user.isBan())
                    builder.setMessage("Voulez vous vraiment bannir l'utilisateur id:" + user.getId_user() + " " + user.getUsername());
                else
                    builder.setMessage("Voulez vous vraiment débannir l'utilisateur id:" + user.getId_user() + " " + user.getUsername());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ban;
                        if (user.isBan())
                            ban = 0;
                        else
                            ban = 1;

                        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
                        Call<ResponseBody> call = apiInterface.banUser(user.getId_user(), ban);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                // Mettre à jour l'état de l'utilisateur et le texte du bouton
                                user.setBan(!user.isBan());
                                if (user.isBan()) {
                                    Toast.makeText(mContext,"Utilisateur banni ",Toast.LENGTH_LONG);
                                    banUserButton.setText("Débannir utilisateur");
                                } else {
                                    Toast.makeText(mContext,"Utilisateur débanni ",Toast.LENGTH_LONG);
                                    banUserButton.setText("Bannir utilisateur");
                                }

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
}
