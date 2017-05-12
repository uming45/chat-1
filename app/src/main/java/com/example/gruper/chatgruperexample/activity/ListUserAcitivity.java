package com.example.gruper.chatgruperexample.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gruper.chatgruperexample.R;
import com.example.gruper.chatgruperexample.adapter.ListUserAdapter;

import com.example.gruper.chatgruperexample.databinding.ListUserActivityBinding;
import com.example.gruper.chatgruperexample.model.User;
import com.example.gruper.chatgruperexample.model.UserResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gruper on 09/05/17.
 */

public class ListUserAcitivity extends AppCompatActivity {

    ListUserActivityBinding binding;
    List<User> users = new ArrayList<User>();
    ListUserAdapter adapter;
    RequestQueue requestQueue;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.list_user_activity);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        getUsrList();

        requestQueue = Volley.newRequestQueue(ListUserAcitivity.this);
        try {
            StringRequest strReq = new StringRequest(Request.Method.GET,"http://128.199.134.212:9090/plugins/restapi/v1/users", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                      System.out.println("Response : "+response);
                    UserResponse userResponse = new Gson().fromJson(response, UserResponse.class);
                    Log.d("DATAGROUP 1","DATAGROUP 1 : "+userResponse.toString());
                    users.clear();
                    users = new ArrayList<>();
                    users.addAll(0, userResponse.getUser());
                    binding.recyclerListUser.setHasFixedSize(true);
                    binding.recyclerListUser.setLayoutManager(new LinearLayoutManager(ListUserAcitivity.this));

                    adapter = new ListUserAdapter(ListUserAcitivity.this, users);
                    binding.recyclerListUser.setAdapter(adapter);
                    adapter.SetOnItemClickListener(new ListUserAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            User dataUser = adapter.getAllItem().get(position);
//                            Toast.makeText(PersonalChatActivity.this, dataUser.getUsername(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ListUserAcitivity.this, PersonalChatActivity.class);
                            intent.putExtra("userName", dataUser.getUsername());
                            startActivity(intent);
                        }
                    });


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "uCARfE9CXzlvL63i");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            requestQueue.add(strReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
//    public void setFragment(Fragment fragment, FragmentManager fm, int content){
//        FragmentManager fragmentManager = fm;
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(content, fragment);
//        fragmentTransaction.commit();
//    }

    public void getUsrList(){
        requestQueue = Volley.newRequestQueue(ListUserAcitivity.this);
        try {
            StringRequest strReq = new StringRequest(Request.Method.GET,"http://128.199.134.212:9090/plugins/restapi/v1/users", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  System.out.println("Response : "+response);
                    UserResponse userResponse = new Gson().fromJson(response, UserResponse.class);
                    users.clear();
                    users = new ArrayList<>();
                    users.addAll(0, userResponse.getUser());
                    binding.recyclerListUser.setHasFixedSize(true);
                    binding.recyclerListUser.setLayoutManager(new LinearLayoutManager(ListUserAcitivity.this));

                    adapter = new ListUserAdapter(ListUserAcitivity.this, users);
                    binding.recyclerListUser.setAdapter(adapter);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "uCARfE9CXzlvL63i");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            requestQueue.add(strReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
