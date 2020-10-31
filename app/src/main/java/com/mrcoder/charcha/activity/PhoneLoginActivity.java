package com.mrcoder.charcha.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mrcoder.charcha.R;

public class PhoneLoginActivity extends AppCompatActivity {

    EditText phoneLogin_editPhoneNumber, phoneLogin_editVerificationCode;
    Button phoneLogin_btnSendCode, phoneLogin_btnVerifyCode;

    String strPhone, strCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        init();

        sendVerificationCode();

    }

    private void init() {
        phoneLogin_editPhoneNumber = findViewById(R.id.phoneLogin_editInputPhone);
        phoneLogin_editVerificationCode = findViewById(R.id.phoneLogin_editVerificationCode);
        phoneLogin_btnSendCode = findViewById(R.id.phoneLogin_btnSendCode);
        phoneLogin_btnVerifyCode = findViewById(R.id.phoneLogin_btnVerifyCode);
    }

    private void sendVerificationCode() {
        phoneLogin_btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPhone = phoneLogin_editPhoneNumber.getText().toString();
                if (!strPhone.isEmpty()) {
                    phoneLogin_btnSendCode.setVisibility(View.INVISIBLE);
                    phoneLogin_editPhoneNumber.setVisibility(View.INVISIBLE);

                    phoneLogin_btnVerifyCode.setVisibility(View.VISIBLE);
                    phoneLogin_editVerificationCode.setVisibility(View.VISIBLE);
                }else {
                    phoneLogin_editPhoneNumber.setError("Enter phone number");
                }
            }
        });
    }
}