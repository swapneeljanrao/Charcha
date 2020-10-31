package com.mrcoder.charcha.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mrcoder.charcha.MainActivity;
import com.mrcoder.charcha.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private TextView mAlreadyHaveAccount;
    private Button mbtnRegister;
    private EditText mRegisterEditEmail, mRegisterEditPassword;
    private ImageView mImageRegisterImageView;
    //    private ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    private DatabaseReference reference;

    String strEmail, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
//        mProgressDialog = new ProgressDialog(this);

        mAlreadyHaveAccount = findViewById(R.id.register_already_have_account);
        mbtnRegister = findViewById(R.id.register_button);
        mRegisterEditEmail = findViewById(R.id.register_email);
        mRegisterEditPassword = findViewById(R.id.register_password);
        mImageRegisterImageView = findViewById(R.id.register_image);
//        mProgressDialog = findViewById(R.id.register_progressBar);

        mAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                sendUserToMainActivity();
            }
        });

        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserWithEmail();
            }
        });
    }

    private void registerUserWithEmail() {
        strEmail = mRegisterEditEmail.getText().toString();
        strPassword = mRegisterEditPassword.getText().toString();
        // if (validInputs()) {
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String currentUserId = mAuth.getCurrentUser().getUid();
                    reference.child("Charcha_AllUsers").child(currentUserId).setValue("");
                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Registration failed \n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        //}
    }

    private boolean validInputs() {
        boolean isValid = false;
        strEmail = mRegisterEditEmail.getText().toString();
        strPassword = mRegisterEditPassword.getText().toString();
        if (strEmail.isEmpty()) {
            isValid = false;
            mRegisterEditEmail.setError("Enter email");
        } else {
            if (strPassword.isEmpty()) {
                isValid = false;
                mRegisterEditPassword.setError("enter password");
            }
        }
        return isValid;
    }

    private void sendUserToMainActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}