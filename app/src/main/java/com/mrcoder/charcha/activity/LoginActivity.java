package com.mrcoder.charcha.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mrcoder.charcha.MainActivity;
import com.mrcoder.charcha.R;

public class LoginActivity extends AppCompatActivity {

    private TextView mNoAccountLoginHere, mForgotPassword;
    private Button mbtnLogin, mbtnPhoneLogin;
    private EditText mEditEmail, mEditPassword;
    private ImageView mImageLoginImageView;
    private ScrollView mScrollViewLogin;

    FirebaseAuth mAuth;

    String strEmail, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mNoAccountLoginHere = findViewById(R.id.no_account_loginHere);
        mForgotPassword = findViewById(R.id.login_forgotPassword);
        mbtnLogin = findViewById(R.id.login_button);
        mbtnPhoneLogin = findViewById(R.id.phone_login_button);
        mEditEmail = findViewById(R.id.login_email);
        mEditPassword = findViewById(R.id.login_password);
        mImageLoginImageView = findViewById(R.id.login_image);
        mScrollViewLogin = findViewById(R.id.scrollviewLogin);
        mScrollViewLogin.fullScroll(View.FOCUS_UP);

        mNoAccountLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                finish();
            }
        });

        mbtnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPhoneLoginActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mScrollViewLogin.fullScroll(View.FOCUS_UP);
    }

    private void login() {
        strEmail = mEditEmail.getText().toString();
        strPassword = mEditPassword.getText().toString();

        if (strEmail.isEmpty()) {
            mEditEmail.setError("Enter email");
        } else if (strPassword.isEmpty()) {
            mEditPassword.setError("Enter password");
        } else {
            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Loged In", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Login Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    private void sendUserToPhoneLoginActivity() {
        Intent phoneLoginActivityIntent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
//        phoneLoginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(phoneLoginActivityIntent);
    }
}