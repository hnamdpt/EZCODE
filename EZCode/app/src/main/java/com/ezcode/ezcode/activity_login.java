package com.ezcode.ezcode;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class activity_login extends AppCompatActivity {

    RelativeLayout rellay1,rellay2;
    TextView tvDescription;
    Button btnLogin;
    Handler handler = new Handler();
    Runnable runnable= new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
//          btnLogin = (Button) findViewById(R.id.btn_login);
            tvDescription.setVisibility(View.INVISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout)findViewById(R.id.reallay1);
        rellay2 = (RelativeLayout)findViewById(R.id.reallay2);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvDescription = (TextView)findViewById(R.id.tv_description);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, MainActivity.class);
                startActivity(intent);

            }
        });
        handler.postDelayed(runnable,2000);
    }
}
