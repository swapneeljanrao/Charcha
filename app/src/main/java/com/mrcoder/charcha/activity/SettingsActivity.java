package com.mrcoder.charcha.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mrcoder.charcha.MainActivity;
import com.mrcoder.charcha.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    EditText mEditStatus, mEditUsername;
    CircleImageView mImageUserImage;
    Button mBtnUpdateSetting;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    private String currentUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        mEditUsername = findViewById(R.id.settings_username);
        mEditStatus = findViewById(R.id.settings_status);
        mImageUserImage = findViewById(R.id.settings_profileImage);
        mBtnUpdateSetting = findViewById(R.id.settings_btnUpdate);

        mEditUsername.setEnabled(false);

        mBtnUpdateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        retriveUserInfo();
    }

    private void updateSettings() {
        String setUsername = mEditUsername.getText().toString();
        String setStatus = mEditStatus.getText().toString();
        if (TextUtils.isEmpty(setUsername)) {
            Toast.makeText(this, "enter username", Toast.LENGTH_SHORT).show();
            mEditUsername.setError("enter username");
        }
        if (TextUtils.isEmpty(setStatus)) {
            mEditStatus.setError("enter status");
            Toast.makeText(this, "enter status", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUID);
            profileMap.put("name", setUsername);
            profileMap.put("status", setStatus);
            reference.child("Charcha_AllUsers").child(currentUID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserToMainActivity();
                        Toast.makeText(SettingsActivity.this, "Profile updated succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void retriveUserInfo() {
        reference.child("Charcha_AllUsers").child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
                    String strgetUserName = dataSnapshot.child("name").getValue().toString();
                    String strgetstatus = dataSnapshot.child("status").getValue().toString();
                    String strProfileImageUrl = dataSnapshot.child("image").getValue().toString();

                    mEditUsername.setText(strgetUserName);
                    mEditStatus.setText(strgetstatus);
//                    mImageUserImage.setImageURI(strProfileImageUrl);
                } else if (dataSnapshot.exists() && (dataSnapshot.hasChild("name"))) {
                    String strgetUserName = dataSnapshot.child("name").getValue().toString();
                    String strgetstatus = dataSnapshot.child("status").getValue().toString();

                    mEditUsername.setText(strgetUserName);
                    mEditStatus.setText(strgetstatus);
//                    mImageUserImage.setImageURI(strProfileImageUrl);
                } else {
                    mEditUsername.setEnabled(true);
                    Toast.makeText(SettingsActivity.this, "Update your profile info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();

    }
}