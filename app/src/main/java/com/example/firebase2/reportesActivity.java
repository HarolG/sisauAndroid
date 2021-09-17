package com.example.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class reportesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
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