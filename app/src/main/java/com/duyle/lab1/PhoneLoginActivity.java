package com.duyle.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private com.duyle.lab1.databinding.ActivityPhoneLoginBinding binding;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationID;

    private String TAG = "zzz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.duyle.lab1.databinding.ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationID = s;
            }
        };

        userClick();
    }

    private void userClick() {
       binding.tvSendOtp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String phone = binding.edtPhone.getText().toString().trim();
               getOtp(phone);
           }
       });

       binding.clickNavLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               String opt = binding.edtOtp.getText().toString().trim();
//               syncOtp(opt);
               startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
           }
       });
    }

    private void getOtp(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+84"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(PhoneLoginActivity.this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void syncOtp(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);
        signInCredential(credential);
    }

    private void signInCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
                }
                else  {
                    Toast.makeText(PhoneLoginActivity.this, "Something went errors", Toast.LENGTH_SHORT).show();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(PhoneLoginActivity.this, "Something went errors", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}