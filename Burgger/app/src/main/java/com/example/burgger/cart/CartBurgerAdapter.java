package com.example.burgger.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burgger.R;
import com.example.burgger.RetrofitClientInstance;
import com.example.burgger.object.Burger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CartBurgerAdapter extends ArrayAdapter<Burger> {

    private Context mContext;
    private int mResource;

    private int id_user;

    private  TextView totalTextView, aucuneCommande;

    private ArrayList<Burger> cart;

    public CartBurgerAdapter(Context context, int resource, List<Burger> burgers, int id_user, TextView totalTextView, ArrayList<Burger> cart, TextView aucuneCommande) {
        super(context, resource, burgers);
        mContext = context;
        mResource = resource;
        this.id_user=id_user;
        this.totalTextView=totalTextView;
        this.cart=cart;
        this.aucuneCommande = aucuneCommande;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Burger burger = getItem(position);

        Button addbtn = view.findViewById(R.id.addtqteButton);
        Button rmbtn = view.findViewById(R.id.rmqteButton);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        ImageView photoImageView = view.findViewById(R.id.photoImageView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        nameTextView.setText(burger.getBurgerNamme());
        priceTextView.setText(""+burger.getPrice()* burger.getQuantity());
        photoImageView.setImageResource(mContext.getResources().getIdentifier(burger.getPhoto(), "drawable", mContext.getPackageName()));
        quantityTextView.setText("qté: "+burger.getQuantity());



        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartBurgerAdapter.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(CartBurgerAdapter.ApiInterface.class);
                Call<ResponseBody> call = apiInterface.addQte(id_user,burger.getId_burger());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("ajout");
                        burger.setQuantity(burger.getQuantity()+1);
                        quantityTextView.setText("qté: "+burger.getQuantity());
                        priceTextView.setText(""+burger.getPrice()* burger.getQuantity());
                        refreshTotal();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        rmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartBurgerAdapter.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(CartBurgerAdapter.ApiInterface.class);
                Call<ResponseBody> call = apiInterface.rmQte(id_user,burger.getId_burger());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("supp");
                        burger.setQuantity(burger.getQuantity()-1);
                        quantityTextView.setText("qté: "+burger.getQuantity());
                        priceTextView.setText(""+burger.getPrice()* burger.getQuantity());

                        refreshTotal();
                        if (burger.getQuantity()<1){
                            remove(burger);

                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });



        return view;
    }

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("addQte.php")
        Call<ResponseBody> addQte(
                @Field("id_user") int id_user,
                @Field("id_burger") int id_burger
        );


        @FormUrlEncoded
        @POST("rmQte.php")
        Call<ResponseBody> rmQte(
                @Field("id_user") int id_user,
                @Field("id_burger") int id_burger
        );
    }

    private void refreshTotal(){
        double total=0;
        for (Burger b: cart){
            total += b.getQuantity()*b.getPrice();
        }
        if(total == 0){
            totalTextView.setText("Total : ");
            aucuneCommande.setText("Aucune commande");
        }else
            totalTextView.setText("Total : "+total + " €");


    }
}
