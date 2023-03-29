package com.example.manageyourstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userEditText, passwordEditText;
    private Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        userEditText = (TextInputEditText) findViewById(R.id.userEditText);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String email = userEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            userEditText.setError("Inserire Email!");
            userEditText.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEditText.setError("Email Non Valida!");
            userEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {

            passwordEditText.setError("Inserire Password!");
            passwordEditText.requestFocus();

            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("La Password deve contenere un minimo di 6 caratteri" );
            passwordEditText.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login fallito, controlla le credenziali e riprova",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}