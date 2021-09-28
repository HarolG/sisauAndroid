package com.example.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class reportesActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableDynamic tableDynamic;
    private String[] encabezado;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        //tableLayout = findViewById(R.id.tableReportes);
        tableDynamic = new TableDynamic(tableLayout, getApplicationContext());
        encabezado = new String[]{"Documento", "Nombre"};
        mAuth = FirebaseAuth.getInstance();
        consultarActividadActual();
    }

    private void consultarActividadActual () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sisau.online/api/user/getActivityActual.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
        private TableLayout tableRegister;
        TableDynamic tableDynamic = new TableDynamic(tableRegister, getApplicationContext());

         try {
                tableRegister.setVisibility(View.VISIBLE);
                //getRegistrosBd("https://crocodilian-quart.000webhostapp.com/api/user.php");
                tableRegister.removeAllViews();
                tableDynamic.addHeader(headers);
                tableDynamic.addData(getRegistrosTabla("https://crocodilian-quart.000webhostapp.com/api/user.php"));
             } catch (Exception e) {
                   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
               }
     */
}