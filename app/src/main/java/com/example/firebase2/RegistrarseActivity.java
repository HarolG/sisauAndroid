package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegistrarseActivity extends AppCompatActivity {

    private TextView txtDoc, txtNombre, txtCelular, txtEmail, txtPass, txtConPass;
    Button btnRegister;
    private String documento, nombre, celular, email, pass, conPass;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    String regexNom = "[a-zA-Z]{1,99}";
    String regexDoc = "\\d{6,10}";
    String regexCelular = "\\d{8,10}";
    String regexEmail = "[\\w._%+-]+@uniminuto+.edu";
    String regexClave = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        txtDoc = findViewById(R.id.txtDoc);
        txtNombre = findViewById(R.id.txtNombre);
        txtCelular = findViewById(R.id.txtCelular);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        txtConPass = findViewById(R.id.txtConPass);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documento = txtDoc.getText().toString();
                nombre = txtNombre.getText().toString();
                celular = txtCelular.getText().toString();
                email = txtEmail.getText().toString();
                pass = txtPass.getText().toString();
                conPass = txtConPass.getText().toString();

                boolean bDocumento = validarCampos(regexCelular, documento, txtDoc);
                boolean bNombre = validarCampos(regexNom, nombre, txtNombre);
                boolean bCelular = validarCampos(regexCelular, celular, txtCelular);
                boolean bEmail = validarCampos(regexEmail, celular, txtEmail);
                boolean bPass = validarCampos(regexClave, pass, txtPass);
                boolean bConPass = validarCampos(regexClave, conPass, txtConPass);


                if(bDocumento && bNombre && bCelular /*&& bEmail*/ && bPass && bConPass) {
                    if(pass.equals(conPass)) {
                        registrarse();
                    } else {
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coiciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Algún campo no cumple con los parámetros", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //boolean bDocumento = validarInputs(regexCelular, documento, edtTextDoc);

    }

    private  void registrarse() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    user = mAuth.getCurrentUser();

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nombre)
                                        .build();

                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            mDatabase = FirebaseDatabase.getInstance().getReference();
                                            mDatabase.setValue("JHola");
                                            Toast.makeText(getApplicationContext(), "Se ha enviado un correo de confirmación", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            mAuth.signOut();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            mAuth.signOut();
                                        }
                                    }
                                });
                            } else {
                                mAuth.signOut();
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validarCampos(String regex, String campo, TextView campoLayout) {
        boolean resultadoVal = Pattern.matches(regex, campo);

        if(resultadoVal) {
            campoLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            campoLayout.setBackgroundColor(Color.argb(10, 255, 3, 3));
        }

        return Pattern.matches(regex, campo);
    }

    private void writeNewUser() {
        User newUser = new User(documento, nombre, celular, email);
        mDatabase.child("users").child(user.getUid()).setValue(newUser);
    }

}