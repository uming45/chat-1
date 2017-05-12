package com.example.gruper.chatgruperexample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.gruper.chatgruperexample.activity.GroupChatActivity;
import com.example.gruper.chatgruperexample.activity.ListGroupActivity;
import com.example.gruper.chatgruperexample.activity.ListUserAcitivity;
//import com.example.gruper.chatgruperexample.activity.PersonalChatActivity;
//import com.example.gruper.chatgruperexample.databinding.ActivityMainBinding;
import com.example.gruper.chatgruperexample.util.LocalBinder;

//import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

//    ActivityMainBinding binding;
    private MyService mService;
    private static final String TAG = "MainActivity";
    private boolean mBounded;
    static MainActivity mainActivity;

    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<MyService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(R.layout.activity_main);


        doBindService();
//        mainActivity = this;




        Button btnGroupChat = (Button) findViewById(R.id.btnGroupChat);

        btnGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListGroupActivity.class);
                startActivity(i);

            }
        });

        Button btnPersonalChat = (Button) findViewById(R.id.btnPersonalChat);
        btnPersonalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListUserAcitivity.class);
                startActivity(i);

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


    void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public MyService getmService() {
        return mService;
    }
}
