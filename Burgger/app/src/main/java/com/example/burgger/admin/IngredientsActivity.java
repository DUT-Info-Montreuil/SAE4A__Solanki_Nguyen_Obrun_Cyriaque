package com.example.burgger.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.burgger.BurgerDetailFragment;
import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.home.HomeActivity;
import com.example.burgger.object.Burger;
import com.example.burgger.object.Ingredient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsActivity extends AppCompatActivity {

    private List<Ingredient> allIngredients;
    private ListView ListViewIngredient;
    private AdminIngredientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ListViewIngredient = findViewById(R.id.AllIngredients);
        allIngredients = new ArrayList<>();
        getAllIngredients();
        adapter = new AdminIngredientAdapter(this,R.layout.activity_ingredient_adapter2, allIngredients);
        ListViewIngredient.setAdapter(adapter);

        ListViewIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le burger sélectionné
                Intent returnIntent = new Intent();
                Ingredient selectedIngredient= allIngredients.get(position);
                System.out.println(selectedIngredient);
                returnIntent.putExtra("selectedIngredient", selectedIngredient);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });



    }


    public void getAllIngredients(){
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAllIngredients();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject burgerObject = jsonArray.getJSONObject(i);
                        String nameIngr = burgerObject.getString("ingrname");
                        allIngredients.add(new Ingredient(nameIngr));
                    }
                    adapter.notifyDataSetChanged();
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


}