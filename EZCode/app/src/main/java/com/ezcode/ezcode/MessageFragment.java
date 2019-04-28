package com.ezcode.ezcode;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
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
    SinchClient sinchClient;
    Dialog dialog;
    Call call;

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

        sinchClient = Sinch.getSinchClientBuilder()
                .context(getContext())
                .userId(user.getEmail())
                .applicationKey("5446f6a7-2b1b-4b64-ae84-090fe2df3d9f")
                .applicationSecret("bV/MVPbYsUu/Zgnc5GPVug==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener() {

        });
        sinchClient.start();
        listViewMessage.setAdapter(chatAdapter);
//        try {
//            mSocket = IO.socket("http://192.168.0.106:3000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        mSocket.connect();
        listViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"da click :"+ MainActivity.arrChat.get(position).getUserEmail(),Toast.LENGTH_LONG).show();
                callUser(MainActivity.arrChat.get(position));
            }
        });
        mSocket = MainActivity.mSocket;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMessage.getText().toString().trim().length()>0){
                    Chat chat = new Chat();
                    chat.setUserAvatar(user.getAvatarUrl());
                    chat.setUserName(user.getDisplayName());
                    chat.setChatContent(editMessage.getText().toString());
                    chat.setUserEmail(user.getEmail());
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
    public void callUser(Chat userCalled){
        if(call==null){
            call=sinchClient.getCallClient().callUser(userCalled.getUserEmail());
            call.addCallListener(new SinchCallListener());
            openCallerDialog(call,userCalled);
        }
    }
    private void openCallerDialog(Call call, Chat userCalled) {
       dialog=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.call_away_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Button btnCall = dialog.findViewById(R.id.btnCancelCall);
        TextView txtName = dialog.findViewById(R.id.txtCalledName);
        txtName.setText(userCalled.getUserName());
        CircleImageView circleAvatar = dialog.findViewById(R.id.ImgAvatar);
        Picasso.with(getContext()).load(userCalled.getUserAvatar()).into(circleAvatar);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.hangup();
                dialog.dismiss();
            }
        });
        dialog.show();

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

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, final Call inComingcall) {
            call = inComingcall;

            String emailUserCalled = callClient.getCall(call.getCallId()).getRemoteUserId();
            call.addCallListener(new SinchCallListener());

           dialog=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.call_incoming_dialog);
            dialog.setCanceledOnTouchOutside(false);
            Button cancelBtn1 = dialog.findViewById(R.id.btnCancelCall);
            Button cancelBtn2 = dialog.findViewById(R.id.btnCancelCall2);
            Button acceptBtn = dialog.findViewById(R.id.btnAcceptCall);
            CircleImageView circleAvatar = dialog.findViewById(R.id.ImgAvatar);
            TextView txtName = dialog.findViewById(R.id.txtCalledName);
            for(int i=0;i<MainActivity.arrChat.size();i++){
                if(MainActivity.arrChat.get(i).getUserEmail().equals(emailUserCalled)){
                    Chat userCalled=new Chat();
                    userCalled = MainActivity.arrChat.get(i);
                   // Toast.makeText(getContext(),"co user trung",Toast.LENGTH_LONG).show();
                    txtName.setText(userCalled.getUserName());
                    Picasso.with(getContext()).load(userCalled.getUserAvatar()).into(circleAvatar);
                    break;
                }
            }
            cancelBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inComingcall.hangup();
                    dialog.dismiss();
                }
            });
            cancelBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.hangup();
                    dialog.dismiss();
                }
            });

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                //    Toast.makeText(getContext(),"Đã kết nối",Toast.LENGTH_LONG).show();
                    cancelBtn1.setVisibility(View.INVISIBLE);
                    cancelBtn2.setVisibility(View.VISIBLE);
                    acceptBtn.setVisibility(View.INVISIBLE);
                }
            });
            dialog.show();
        }
    }
    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(getContext(),"Đang đổ chuông...",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getContext(),"Bắt đầu cuộc trò chuyện",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEnded(Call endedCall) {
            Toast.makeText(getContext(),"Cuộc gọi kết thúc",Toast.LENGTH_LONG).show();
            call = null;
            endedCall.hangup();
            dialog.dismiss();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }
     long countTime(){
        long time = System.nanoTime();
        return time;
    }

}
