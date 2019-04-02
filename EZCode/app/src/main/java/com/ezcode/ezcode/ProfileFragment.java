package com.ezcode.ezcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcode.ezcode.model.User;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Button logout;
    Activity context;
    CircleImageView profile_image;
    DatabaseReference mData;
    EditText email,edtDisplayName;
    TextView displayName;
    TextView userPoint;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile,container,false);
        context = getActivity();
        mData = FirebaseDatabase.getInstance().getReference();
        profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        logout = (Button)view.findViewById(R.id.btn_logout);
        email = (EditText)view.findViewById(R.id.profile_email);
        edtDisplayName = (EditText)view.findViewById(R.id.edit_display_name);
        displayName = (TextView)view.findViewById(R.id.profile_displayName);
        userPoint = (TextView)view.findViewById(R.id.user_point);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                 Intent intent = new Intent(context,activity_login.class);
                startActivity(intent);
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            mData.child("User/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(context,dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                    User user = dataSnapshot.getValue(User.class);
                    Toast.makeText(context,user.getAvatarUrl(),Toast.LENGTH_LONG).show();
                    Picasso.with(context).load(user.getAvatarUrl()).into(profile_image);
                    email.setText(user.getEmail());
                    displayName.setText(user.getDisplayName());
                    edtDisplayName.setText(user.getDisplayName());
                    userPoint.setText(user.getPoint()+"");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return view;


    }
}
