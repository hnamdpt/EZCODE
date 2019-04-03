package com.ezcode.ezcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Button logout,btn_save;
    Activity context;
    CircleImageView profile_image;
    DatabaseReference mData;
    EditText email,edtDisplayName;
    TextView displayName;
    TextView userPoint;
    private static final int SELECT_IMAGE =100;
    Uri imageUri;
    User user;
    FirebaseAuth mAuth;
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
        btn_save = (Button)view.findViewById(R.id.btn_save_profile);
        mAuth= FirebaseAuth.getInstance();
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
                     user = dataSnapshot.getValue(User.class);
                    //Toast.makeText(context,user.getAvatarUrl(),Toast.LENGTH_LONG).show();
                    Picasso.with(context).load(user.getAvatarUrl()).into(profile_image);
                    email.setText(user.getEmail());
                    displayName.setText(user.getDisplayName());
                    edtDisplayName.setText(user.getDisplayName());
                    userPoint.setText("Point: "+ user.getPoint());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        // edit profile
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalary();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FirebaseUser usercurrent = mAuth.getCurrentUser();
                user.setDisplayName(edtDisplayName.getText().toString());
                String email =user.getEmail();
                int userPoint = user.getPoint();
                if(imageUri!=null){
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photo");
                    final StorageReference imageFilePath = mStorage.child(imageUri.getLastPathSegment());
                    imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    user.setAvatarUrl(downloadUrl.toString());
                                    Toast.makeText(context,user.getAvatarUrl(),Toast.LENGTH_LONG).show();
                                    FirebaseUser usercurrent = mAuth.getCurrentUser();
                                    mData.child("User").child(usercurrent.getUid()).setValue(user);

                                }
                            });
                        }
                    });
                }else{
                    mData.child("User").child(usercurrent.getUid()).setValue(user);
                }


                Toast.makeText(context,"Success! User information has been saved",Toast.LENGTH_LONG).show();

            }
        });
        return view;


    }

    private void openGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    profile_image.setImageURI(imageUri);
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                        userAvatar.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
