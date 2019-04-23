package com.ezcode.ezcode;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.internal.IdTokenListener;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;

import java.util.ArrayList;

public class activity_setting extends AppCompatActivity {
    Spinner spnLanguage;
    ArrayList listLanguage;
    ImageButton btnCall,btnCallDown;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spnLanguage = (Spinner)findViewById(R.id.spn_language);
        btnCall = (ImageButton) findViewById(R.id.btnCall);
        android.content.Context context = this.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SinchClient sinchClient = Sinch.getSinchClientBuilder().context(getBaseContext())
                        .applicationKey("1c34610e-d92f-4d64-b9a4-50cd6e8c1c37")
                        .applicationSecret("DgGY4io0nUOpHaJiSQz9uA==")
                        .environmentHost("clientapi.sinch.com")
                        .userId("131714")
                        .build();
                sinchClient.setSupportCalling(true);
                sinchClient.start();
                CallClient callClient = sinchClient.getCallClient();
                callClient.callPhoneNumber("+84921870314");
            }
        });

    }
}
