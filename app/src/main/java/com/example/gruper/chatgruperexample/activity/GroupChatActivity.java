package com.example.gruper.chatgruperexample.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.gruper.chatgruperexample.R;
import com.example.gruper.chatgruperexample.databinding.ActivityGroupChatBinding;

/**
 * Created by gruper on 09/05/17.
 */

public class GroupChatActivity extends Activity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_chat);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
