package com.duyle.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duyle.lab1.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    Context context;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        mAuth = FirebaseAuth.getInstance();

        binding.clickNavReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RegActivity.class));
            }
        });

        binding.clickNavLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFunction();
            }
        });

        binding.clickNavPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PhoneLoginActivity.class));
            }
        });
    }
    private void loginFunction() {
        binding.progressCircular.setVisibility(View.VISIBLE);

        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill the input text", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(context, "Password mush have more than 6 characters", Toast.LENGTH_SHORT).show();
        }
        else  {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Main", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                binding.progressCircular.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                                //updateUI(user);
                                startActivity(new Intent(context, MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                binding.progressCircular.setVisibility(View.GONE);
                                Log.w("Main", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

}