package com.ezcode.ezcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {

    LinearLayout ll_lesson,ll_practice,ll_exercise,ll_examination;
    Activity context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        context=getActivity();
        ll_lesson = (LinearLayout)view.findViewById(R.id.ll_lesson);
        ll_examination = (LinearLayout)view.findViewById(R.id.ll_examination);
        ll_practice = (LinearLayout)view.findViewById(R.id.ll_practice);
        ll_exercise = (LinearLayout)view.findViewById(R.id.ll_exercise);
        ll_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_lesson.class);
                startActivity(intent);
            }
        });
        ll_examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_examination.class);
                startActivity(intent);
            }
        });
        ll_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_practice.class);
                startActivity(intent);
            }
        });
        ll_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_exercise.class);
                startActivity(intent);
            }
        });

        return view;
    }
    public void setView(){

    }
}
