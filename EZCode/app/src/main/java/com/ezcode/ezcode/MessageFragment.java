package com.ezcode.ezcode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ezcode.ezcode.model.Chat;
import com.ezcode.ezcode.model.ChatAdapter;
import com.ezcode.ezcode.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import org.json.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MessageFragment extends Fragment {
    private Socket mSocket;
    Button btnSend;
    ListView listViewMessage;
    EditText editMessage;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    User user;
    ArrayList<Chat> arrChat;
    ChatAdapter chatAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        btnSend = (Button)view.findViewById(R.id.btn_send_message);
        listViewMessage = (ListView)view.findViewById(R.id.list_message);
        editMessage = (EditText)view.findViewById(R.id.edit_message);
        mAuth= FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        user = MainActivity.user;
        chatAdapter = new ChatAdapter(getContext(),R.layout.chat_item_custom,MainActivity.arrChat);
        listViewMessage.setAdapter(chatAdapter);
//        try {
//            mSocket = IO.socket("http://192.168.0.106:3000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        mSocket.connect();
        mSocket = MainActivity.mSocket;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMessage.getText().toString().trim().length()>0){
                    Chat chat = new Chat();
                    chat.setUserAvatar(user.getAvatarUrl());
                    chat.setUserName(user.getDisplayName());
                    chat.setChatContent(editMessage.getText().toString());
                    Gson gson = new Gson();
                    String chatJson = gson.toJson(chat);
                    MainActivity.mSocket.emit("client-send-chat",chatJson);
                    editMessage.setText("");
                }
            }
        });
        MainActivity.mSocket.on("server-send-chat",onReciveChat);
        return view;
    }
    private Emitter.Listener onReciveChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String chatJson = (String)object.getString("noidung");
                        Gson gson = new Gson();
                        Chat chat = gson.fromJson(chatJson,Chat.class);
                        MainActivity.arrChat.add(chat);
                        chatAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
//    public void setUser(){
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
//            mData.child("User/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                Toast.makeText(context,dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
//                    user = dataSnapshot.getValue(User.class);
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }
    private void scrollMyListViewToBottom() {
        listViewMessage.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listViewMessage.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }
}
