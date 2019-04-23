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
import com.ezcode.ezcode.model.User;
import com.facebook.login.LoginManager;
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

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView buttonNavigation;
    public static ArrayList<Chat> arrChat;
    public  static Socket mSocket;
    public static User user;
    public static boolean isFirstConnect= true;
    FirebaseAuth mAuth;
    DatabaseReference mData;
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
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();

        if(mSocket.connected()){
            mSocket.disconnect();
        }else{
            mSocket.connect();
        }
        buttonNavigation = (BottomNavigationView)findViewById(R.id.navigation_butttom);
        buttonNavigation.setOnNavigationItemSelectedListener(navListen);
        buttonNavigation.setSelectedItemId(R.id.nav_home);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            mData.child("User/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_content,new HomeFragment()).commit();
    }


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
            mSocket.disconnect();
            startActivity(intent);
            finish();
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
