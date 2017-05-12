package com.example.gruper.chatgruperexample.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.gruper.chatgruperexample.MainActivity;
import com.example.gruper.chatgruperexample.R;
import com.example.gruper.chatgruperexample.adapter.ChatAdapter;
import com.example.gruper.chatgruperexample.model.ChatMessage;
import com.example.gruper.chatgruperexample.util.CommonMethod;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import static java.security.AccessController.getContext;


/**
 * Created by gruper on 09/05/17.
 */

public class PersonalChatActivity extends Activity {




    private String user1 = "arif";
    private EditText msg_edittext;
    String USER_TWO;
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);


        msg_edittext = (EditText) findViewById(R.id.messageEditText);
        msgListView = (ListView) findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendMessageButton);


//        random = new Random();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
//                "Chats");

//        random = new Random();
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
//                "Chats");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.sendMessageButton:
                        sendTextMessage(v);

//                }
            }
        });

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(PersonalChatActivity.this, chatlist);
        msgListView.setAdapter(chatAdapter);



    }

    public void sendTextMessage(View v) {
        USER_TWO = getIntent().getStringExtra("userName");
        Log.d("USERNAME2","USERNAME2 => "+USER_TWO);
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, USER_TWO,
                    message, "" , true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            Log.d("PESAN ","PESAN : "+chatMessage.body.toString());
            chatMessage.Date = CommonMethod.getCurrentDate();
            chatMessage.Time = CommonMethod.getCurrentTime();
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
//                mMsgRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount());


            MainActivity activity= new MainActivity();



            activity.getmService().xmpp.sendMessage(chatMessage);
        }
    }





}
