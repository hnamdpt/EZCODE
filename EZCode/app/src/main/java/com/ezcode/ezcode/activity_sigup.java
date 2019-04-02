package com.ezcode.ezcode;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ezcode.ezcode.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_sigup extends AppCompatActivity {

    EditText signup_email,signup_displayName,signup_password;
    Button signup_btn;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    Button loginNow_btn;
    RelativeLayout reallay1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);
        setView();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
    }

    private void Signup() {
        String email =signup_email.getText().toString();
        String password = signup_password.getText().toString();
        if(signup_displayName.getText().toString().length()>6&&signup_displayName.getText().toString().length()<30){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        User newUser = new User();
                        newUser.setDisplayName(signup_displayName.getText().toString());
                        newUser.setEmail(user.getEmail());
                        newUser.setAvatarUrl("https://firebasestorage.googleapis.com/v0/b/ezcode.appspot.com/o/user_photo%2Fuser_defult_photo.png?alt=media&token=bb45e9da-11e7-4394-bba5-cd9e317b30df");
                        newUser.setPoint(0);
                        mData.child("User").child(user.getUid()).setValue(newUser);
                        showMessage("Dang ky thanh cong");
                        loginNow_btn.setVisibility(View.VISIBLE);
                        reallay1.setVisibility(View.GONE);
                        loginNow_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity_sigup.this, MainActivity.class);
                                startActivity(intent);
                            };
                        });

                    }else{
                        showMessage("Đăng ký thất bại");
                    }

                }
            });
        }else{
            showMessage("The diplay name must have length between 6-30 chacter ");
        }


    }

    private void addUserToDataBase(FirebaseUser user) {

    }

    void setView(){
        signup_email = (EditText)findViewById(R.id.sigup_email);
        signup_password = (EditText)findViewById(R.id.signup_password);
        signup_displayName = (EditText)findViewById(R.id.sigup_displayname);
        signup_btn = (Button)findViewById(R.id.sigup_button);
        reallay1 = (RelativeLayout)findViewById(R.id.reallay1);
        loginNow_btn = (Button)findViewById(R.id.login_now_btn);
    }
    void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
