package com.ezcode.ezcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_result_exercise extends AppCompatActivity {

    TextView tv_result,tv_point;
    Button btn_tryAgain,btn_gohome;
    DatabaseReference mData;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_exercise);
        tv_result =(TextView)findViewById(R.id.tv_result);
        tv_point = (TextView)findViewById(R.id.tv_point);
        btn_tryAgain = (Button)findViewById(R.id.btn_tryagain);
        btn_gohome = (Button)findViewById(R.id.btn_gohome);
        Intent intent = getIntent();
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        int total = intent.getExtras().getInt("total");
        int correct = intent.getExtras().getInt("correct");
        MainActivity.user.setPoint(MainActivity.user.getPoint()+correct);
        tv_result.setText("Correct: "+correct+"/"+total);
        tv_point.setText("+"+correct);
       if(mAuth.getCurrentUser()!=null){
            mData.child("User").child(mAuth.getCurrentUser().getUid()).setValue(MainActivity.user);
        }
        btn_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_result_exercise.this,activity_exercise.class);
                startActivity(intent);
            }
        });
        btn_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_result_exercise.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
