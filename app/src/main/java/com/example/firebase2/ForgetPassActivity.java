package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetPassActivity extends AppCompatActivity {

    Button btnRestorePass, btnGoLogin;
    TextView edtTxtEmail, textCardAlert;
    CardView cardAlert;
    String email;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        btnRestorePass = findViewById(R.id.btnRestorePass);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        mAuth = FirebaseAuth.getInstance();

        btnRestorePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtTxtEmail.getText().toString();
                if(!email.isEmpty()) {
                    restablecerClave();
                } else {
                    Toast.makeText(getApplicationContext(), "El correo no puede estar vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    protected void restablecerClave() {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Revise su correo electrónico", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    /*
                    textCardAlert.setText("Se ha enviado un correo de restablecimiento");
                    textCardAlert.setBackgroundColor(Color.GREEN);
                    textCardAlert.setTextColor(Color.BLACK);
                     */
                } else {
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error, verifique el correo", Toast.LENGTH_SHORT).show();
                    /*
                    textCardAlert.setText("Ha ocurrido un error, verifique el correo");
                    textCardAlert.setBackgroundColor(Color.RED);
                    textCardAlert.setTextColor(Color.WHITE);
                     */
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}