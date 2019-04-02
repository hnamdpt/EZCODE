package com.ezcode.ezcode;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcode.ezcode.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class activity_login extends AppCompatActivity {

    RelativeLayout rellay1,rellay2;
    TextView tvDescription;
    Button btnLogin,sigup_btn;
    EditText edtEmail,edtPassword;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    String TAG = "LOGIN";
    LoginButton loginButton;

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
        edtEmail = (EditText)findViewById(R.id.login_email);
        edtPassword = (EditText)findViewById(R.id.login_password);
        tvDescription = (TextView)findViewById(R.id.tv_description);
        loginButton = (LoginButton)findViewById(R.id.btn_loginfb);
        sigup_btn = (Button)findViewById(R.id.sigup_btn);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        sigup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_sigup.class);
                startActivity(intent);
            }
        });
//        printKeyHash();
        handler.postDelayed(runnable,2000);

    }

    private void signIn() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_login.this,"loi cmnr",Toast.LENGTH_LONG).show();
                        Log.e("ERROR_EDMT",e.getMessage());

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String email = authResult.getUser().getEmail();
                Toast.makeText(activity_login.this,"LOGIN WITH EMAIL: "+email,Toast.LENGTH_LONG).show();


                FirebaseUser user = mAuth.getCurrentUser();

                boolean isNewUser = authResult.getAdditionalUserInfo().isNewUser();
                if(isNewUser){
                    String _displayName = user.getDisplayName();
                    String _email = user.getEmail();
                    String _avatarUrl = user.getPhotoUrl()+"?type=large";
                    int _point = 0;
                    User newUser = new User(_displayName,_email,_avatarUrl,_point);
                    mData.child("User").child(user.getUid()).setValue(newUser);

                }
                Intent intent = new Intent(activity_login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    void login(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(activity_login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity_login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
    private void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.ezcode.loginfacebookfirebase", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest messageDigest =MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
           if(currentUser!=null){
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {// khi an nut back tren android
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
