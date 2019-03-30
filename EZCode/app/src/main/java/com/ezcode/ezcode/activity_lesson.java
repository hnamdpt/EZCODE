package com.ezcode.ezcode;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

public class activity_lesson extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    WebView webViewLesson;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        webViewLesson = (WebView)findViewById(R.id.lesson_webview);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.lesson_html_1:
////                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                            new MessageFragment()).commit();
//                webViewLesson.loadUrl("file:///android_asset/login.html");
//                break;
//            case R.id.nav_profile:
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new ChatFragment()).commit();
//                webViewLesson.loadUrl("file:///android_asset/news.html");
//                break;
//        }
//        switch (getResources().getResourceEntryName(menuItem.getItemId())){
//            case "":
//                break;
//        }
        String lessonName = getResources().getResourceEntryName(menuItem.getItemId());
        webViewLesson.loadUrl("file:///android_asset/"+lessonName+".html");
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
