package com.example.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    LinearLayout btnSignOut, btnScanQr, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        btnSignOut = findViewById(R.id.btnSignOut);
        btnScanQr = findViewById(R.id.btnScanQr);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearQr();
            }
        });
    }

    private void guardarRegistro(String URL, String codigo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                /*
                if(!response.isEmpty()) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
                 */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("uid_usuario", mAuth.getUid());
                parametros.put("codigo", codigo);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void escanearQr() {
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("Escanea el código");
        intent.setCameraId(0);
        intent.setOrientationLocked(false);
        intent.setBeepEnabled(false);
        intent.setCaptureActivity(CaptureActivity.class);
        intent.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_LONG).show();
            } else {
                try {
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    guardarRegistro("https://www.sisau.online/api/user/addReport.php", result.getContents());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}