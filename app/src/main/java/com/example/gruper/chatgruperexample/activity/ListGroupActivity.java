package com.example.gruper.chatgruperexample.activity;

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
import com.example.gruper.chatgruperexample.adapter.ListGroupAdapter;
import com.example.gruper.chatgruperexample.databinding.ListGroupActivityBinding;
import com.example.gruper.chatgruperexample.model.ChatRoom;
import com.example.gruper.chatgruperexample.model.GroupRespon;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gruper on 09/05/17.
 */

public class ListGroupActivity extends AppCompatActivity {

    ListGroupActivityBinding binding;
//    List<ChatRoom> chatRooms = null;
    List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
    ListGroupAdapter adapter;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.list_group_activity);






        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        requestQueue = Volley.newRequestQueue(ListGroupActivity.this);
        try {
            final StringRequest strReq = new StringRequest(Request.Method.GET,"http://128.199.134.212:9090/plugins/restapi/v1/chatrooms", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("DATAGROUP 1","DATAGROUP 1 : "+response);

                    GroupRespon groupRespon = new Gson().fromJson(response, GroupRespon.class);
                    Log.d("DATAGROUP 2","DATAGROUP 2 : "+groupRespon.toString());
                    chatRooms.clear();
                    chatRooms = new ArrayList<>();
                    chatRooms.addAll(0, groupRespon.getChatRoom());
                    binding.recyclerListGrup.setHasFixedSize(true);
                    binding.recyclerListGrup.setLayoutManager(new LinearLayoutManager(ListGroupActivity.this));
                    adapter = new ListGroupAdapter(ListGroupActivity.this, chatRooms);
                    binding.recyclerListGrup.setAdapter(adapter);


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

//    private void makeJsonObjectRequest() {
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                "http://128.199.134.212:9090/plugins/restapi/v1/chatrooms", null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                                    try {
//                        JSONObject jsonObject = new JSONObject("response");
//                        JSONArray jsonArray = jsonObject.getJSONArray("charoom");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            String name = object.getString("roomName");
//                            Log.d("DATAGROUP 2","DATAGROUP 2 : "+name.toString());
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
////                Log.d(TAG, response.toString());
//
//                try {
//                    // Parsing json object response
//                    // response will be a json object
//                    String name = response.getString("name");
//                    String email = response.getString("email");
////                    JSONObject phone = response.getJSONObject("phone");
////                    String home = phone.getString("home");
////                    String mobile = phone.getString("mobile");
//
////                    jsonResponse = "";
////                    jsonResponse += "Name: " + name + "\n\n";
////                    jsonResponse += "Email: " + email + "\n\n";
////                    jsonResponse += "Home: " + home + "\n\n";
////                    jsonResponse += "Mobile: " + mobile + "\n\n";
//
//                    chatRooms = new ArrayList<>();
////                    chatRooms.addAll(0, groupRespon.getChatRooms());
//                    chatRooms.addAll((Collection<? extends ChatRoom>) response);
//
//                    binding.recyclerListUser.setHasFixedSize(true);
//                    binding.recyclerListUser.setLayoutManager(new LinearLayoutManager(ListGroupActivity.this));
//
//                    adapter = new ListGroupAdapter(ListGroupActivity.this, chatRooms);
//
////                    txtResponse.setText(jsonResponse);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//
//            }
//        });
//
//        // Adding request to request queue
//        requestQueue.add(jsonObjReq);
//
//    }

}
