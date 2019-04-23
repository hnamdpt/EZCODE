package com.ezcode.ezcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class activity_exercise extends AppCompatActivity {

    LinearLayout ll_html,ll_css,ll_javascript;
    Button btn_goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ll_html = (LinearLayout)findViewById(R.id.ll_html);
        ll_css = (LinearLayout)findViewById(R.id.ll_css);
        ll_javascript = (LinearLayout)findViewById(R.id.ll_javascript);
        btn_goHome = (Button)findViewById(R.id.btn_goHome);
        ll_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_exercise.this, activity_play_exercise.class);
                intent.putExtra("exercise","HTML");
                startActivity(intent);
                finish();
            }
        });
        ll_css.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_exercise.this, activity_play_exercise.class);
                intent.putExtra("exercise","CSS");
                startActivity(intent);
                finish();
            }
        });
        ll_javascript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_exercise.this, activity_play_exercise.class);
                intent.putExtra("exercise","JAVASCRIPT");
                startActivity(intent);
                finish();
            }
        });
        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_exercise.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
