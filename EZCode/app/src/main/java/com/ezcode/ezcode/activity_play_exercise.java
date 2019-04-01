package com.ezcode.ezcode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcode.ezcode.model.Test;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_play_exercise extends AppCompatActivity {

    DatabaseReference mData;
    TextView tv_question,tv_answerA,tv_answerB,tv_answerC,tv_answerD,tv_titleQuestion,tv_coutQuestion;
    ArrayList<Test> listTest;
    int countQuestion = 0;
    int score=0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countQuestion++;
            int x=countQuestion+1;


            if(countQuestion<listTest.size()){
                setTest(countQuestion);
                tv_titleQuestion.setText("Question "+x);
                tv_coutQuestion.setText(x+"/"+listTest.size());
            }
            if(countQuestion>=listTest.size()){
                Toast.makeText(activity_play_exercise.this,"Correct: "+ score+" / "+listTest.size(),Toast.LENGTH_LONG).show();
            }
            if(x<listTest.size()){

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_exercise);
        Intent intent = getIntent();
        setView();
        listTest = new ArrayList<Test>();
        String exercise = intent.getStringExtra("exercise");
        mData = FirebaseDatabase.getInstance().getReference("/Category");
        handlerClickAnswer();
        mData.child(exercise).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(activity_play_exercise.this,"da vao",Toast.LENGTH_SHORT).show();
                Test test =dataSnapshot.getValue(Test.class);
//                tv_question.setText(test.getQuestion());
                    listTest.add(test);

//                Toast.makeText(activity_play_exercise.this,test.getQuestion(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(activity_play_exercise.this,listTest.get(0).getQuestion(),Toast.LENGTH_SHORT).show();
                setTest(0);

                tv_coutQuestion.setText("1/"+listTest.size());
                tv_titleQuestion.setText("Question 1");
                handlerClickAnswer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void setView(){
        tv_question = (TextView)findViewById(R.id.tv_question);
        tv_titleQuestion = (TextView)findViewById(R.id.tv_title_question);
        tv_coutQuestion = (TextView)findViewById(R.id.tv_countQuestion);
        tv_answerA = (TextView)findViewById(R.id.tv_answerA);
        tv_answerB = (TextView)findViewById(R.id.tv_answerB);
        tv_answerC = (TextView)findViewById(R.id.tv_answerC);
        tv_answerD = (TextView)findViewById(R.id.tv_answerD);
    }

    public void setTest(int i){
        tv_question.setText(listTest.get(i).getQuestion());
        tv_answerA.setText(listTest.get(i).getAnswerA());
        tv_answerB.setText(listTest.get(i).getAnswerB());
        tv_answerC.setText(listTest.get(i).getAnswerC());
        tv_answerD.setText(listTest.get(i).getAnswerD());
        tv_answerA.setBackgroundResource(R.drawable.ex2);
        tv_answerB.setBackgroundResource(R.drawable.ex3);
        tv_answerC.setBackgroundResource(R.drawable.ex3);
        tv_answerD.setBackgroundResource(R.drawable.ex4);

    }

    void handlerClickAnswer(){
            tv_answerA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(countQuestion<listTest.size()){
                       if(listTest.get(countQuestion).getCorrect().equals("AnswerA")){
                           score++;
                           tv_answerA.setBackgroundResource(R.drawable.ex2);


                       }else{
                           tv_answerA.setBackgroundResource(R.drawable.ex2_wrong);
                       }
                       handler.postDelayed(runnable,100);
                   }
                }
            });
        tv_answerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countQuestion<listTest.size()){
                    if(listTest.get(countQuestion).getCorrect().equals("AnswerB")){
                        score++;
                        tv_answerB.setBackgroundResource(R.drawable.ex3_correct);


                    }else{
                        tv_answerB.setBackgroundResource(R.drawable.ex3_wrong);
                    }
                    handler.postDelayed(runnable,100);
                }

            }
        });
        tv_answerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countQuestion<listTest.size()){
                    if(listTest.get(countQuestion).getCorrect().equals("AnswerC")){
                        score++;
                        tv_answerC.setBackgroundResource(R.drawable.ex3_correct);


                    }else{
                        tv_answerC.setBackgroundResource(R.drawable.ex3_wrong);
                    }
                    handler.postDelayed(runnable,100);
                }

            }
        });
        tv_answerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countQuestion<listTest.size()){
                    if(listTest.get(countQuestion).getCorrect().equals("AnswerD")){
                        score++;
                        tv_answerD.setBackgroundResource(R.drawable.ex4_correct);


                    }else{
                        tv_answerD.setBackgroundResource(R.drawable.ex4_wrong);
                    }
                    handler.postDelayed(runnable,100);
                }

            }
        });
    }
}
