package com.example.navertestbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private AppCompatImageButton btn_send;
    private EditText msg_send;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatItem> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList);

        btn_send = findViewById(R.id.btn_send);
        msg_send = findViewById(R.id.msg_send);

        final RecyclerView recyclerView = findViewById(R.id.chat_list);
        final LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);



        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String msg = msg_send.getText().toString();
        String time = format.format(now);

        try {
            String openResp = new Constants.ChatBot(getApplicationContext()).execute((String) null).get();//AsyncTask 시작시킴
            //String sendResp = new Constants.ChatBot(getApplicationContext()).execute("너는 뭐해").get();
            if(openResp != null ){//|| sendResp != null) {
                String openMsg = Constants.ext_from_openMsg(openResp);
                //sendResp = Constants.ext_from_sendMsg(sendResp);
                chatList.add(new ChatItem("챗봇", openMsg, time, Constants.LEFT_MSG));
                Log.v("chatList","[left] msg: "+openMsg);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String msg = msg_send.getText().toString();
                String time = format.format(now);

                chatList.add(0,new ChatItem(null, msg, time, Constants.RIGHT_MSG));
                chatAdapter.notifyDataSetChanged();
                msg_send.setText("");

                try {
                    String sendResp = new Constants.ChatBot(getApplicationContext()).execute(msg).get();
                    if(sendResp != null) {
                        String sendMsg = Constants.ext_from_openMsg(sendResp);
                        if(sendMsg == null) sendMsg = Constants.ext_from_sendMsg(sendResp);
                        chatList.add(0,new ChatItem("챗봇", sendMsg, time, Constants.LEFT_MSG));
                        chatAdapter.notifyDataSetChanged();
                        Log.v("chatList","[left] msg: "+sendMsg);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("chatList","[right] msg: "+msg);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("onResume","true");
    }
}