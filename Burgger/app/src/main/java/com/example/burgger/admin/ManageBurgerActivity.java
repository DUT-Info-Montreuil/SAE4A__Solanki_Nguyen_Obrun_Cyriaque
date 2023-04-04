package com.example.burgger.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.burgger.BurgerDetailActivity;
import com.example.burgger.MainActivity;
import com.example.burgger.R;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.home.BurgerAdapter;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.Burger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBurgerActivity extends AppCompatActivity {
    private ArrayList<Burger> burgers;
    private EditBurgerAdapter burgerListAdapter;


    private ListView burgersListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_burger);
        burgersListView = findViewById(R.id.burger_list_view);
        burgers=new ArrayList<>();
        initializeBurger();
        burgerListAdapter.notifyDataSetChanged();

    }


    public void getAllBurgers(){
        burgers.clear();
        HomeActivity.ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(HomeActivity.ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getBurgers();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject burgerObject = jsonArray.getJSONObject(i);
                        int burgerId = burgerObject.getInt("id_burger");
                        String burgerName = burgerObject.getString("burgername");
                        Double burgerPrice = burgerObject.getDouble("price");
                        String burgerPhoto = burgerObject.getString("photo");
                        String burgerDescription = burgerObject.getString("description");
                        Double reduction = burgerObject.getDouble("COALESCE(reduction, 0)");
                        if(reduction == 0){
                            burgers.add(new Burger(burgerId,0,burgerName,burgerPrice,burgerPhoto,burgerDescription));
                        }else{
                            burgers.add(new Burger(burgerId,reduction,burgerName,burgerPrice,burgerPhoto,burgerDescription));
                        }

                    }
                    burgerListAdapter.notifyDataSetChanged();

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


    private  void initializeBurger(){

        getAllBurgers();
        System.out.println(burgers);
        burgerListAdapter = new EditBurgerAdapter(this,R.layout.burger_list_item_admin,burgers,ManageBurgerActivity.this);
        burgersListView.setAdapter(burgerListAdapter);

    }


}