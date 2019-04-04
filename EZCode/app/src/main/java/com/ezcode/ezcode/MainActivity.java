package com.ezcode.ezcode;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ezcode.ezcode.model.Chat;
import com.ezcode.ezcode.model.ChatAdapter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView buttonNavigation;
    public static ArrayList<Chat> arrChat;
    public  static Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrChat = new ArrayList<Chat>();
        try {
            mSocket = IO.socket("https://hnamdpt-ezcode.herokuapp.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        buttonNavigation = (BottomNavigationView)findViewById(R.id.navigation_butttom);
        buttonNavigation.setOnNavigationItemSelectedListener(navListen);
        buttonNavigation.setSelectedItemId(R.id.nav_home);
        mSocket.on("server-send-chat",onReciveChat);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_content,new HomeFragment()).commit();
    }
    private Emitter.Listener onReciveChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(MainActivity.this == null)
                return;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String chatJson = (String)object.getString("noidung");
                        Gson gson = new Gson();
                        Chat chat = gson.fromJson(chatJson,Chat.class);
                        MainActivity.arrChat.add(chat);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener navListen = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_message:
                    selectedFragment = new MessageFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_content,selectedFragment).commit();
            return true;
        }
    };
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this,activity_login.class);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
