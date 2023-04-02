package com.example.burgger.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.widget.ListView;

import com.example.burgger.R;
import com.example.burgger.api.ApiInterface;
import com.example.burgger.api.RetrofitClientInstance;
import com.example.burgger.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ManageUserActivity extends AppCompatActivity {

    private UserAdapter userListAdapter;
    private ArrayList<User> users;
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        users = new ArrayList<>();
        userListView = findViewById(R.id.users_list_view);
        getAllUsers();
        userListAdapter = new UserAdapter(this, R.layout.user_list_item, users);
        userListView.setAdapter(userListAdapter);

        SearchView searchView = findViewById(R.id.UserSearch_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().toString().isEmpty()) {
                    getAllUsers();
                } else {
                    searchUser(searchView.getQuery().toString());
                }
                return false;
            }
        });
    }



    public void getAllUsers() {
        users.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getUsers();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userObject = jsonArray.getJSONObject(i);
                        int userid = userObject.getInt("id_user");
                        String User_username = userObject.getString("username");
                        User user = new User();
                        user.setId_user(userid);
                        user.setUsername(User_username);
                        if (userObject.getInt("ban") == 1) {
                            user.setBan(true);
                        } else {
                            user.setBan(false);
                        }
                        users.add(user);
                    }

                    userListAdapter.notifyDataSetChanged();
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

    public void searchUser(String username) {
        users.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.searchUser(username);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    users.clear();

                    if (!jsonObject.isNull("result")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            int userid = userObject.getInt("id_user");
                            String User_username = userObject.getString("username");
                            User user = new User();
                            user.setId_user(userid);
                            user.setUsername(User_username);
                            if (userObject.getInt("ban") == 1) {
                                user.setBan(true);
                            } else {
                                user.setBan(false);
                            }
                            users.add(user);
                        }
                    }

                    userListAdapter.notifyDataSetChanged();
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