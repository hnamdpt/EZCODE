package com.ezcode.ezcode;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView buttonNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonNavigation = (BottomNavigationView)findViewById(R.id.navigation_butttom);
        buttonNavigation.setOnNavigationItemSelectedListener(navListen);
        buttonNavigation.setSelectedItemId(R.id.nav_home);
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
}
