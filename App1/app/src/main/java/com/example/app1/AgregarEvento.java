package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AgregarEvento extends AppCompatActivity {
    private ArrayList<String> fecha;
    public Intent intent;
    private TextView textView;
    private EditText et_titulo, et_hora_inicio, et_hora_fin;
    public ArrayList<String> fechas_prueba = new ArrayList<String>();
    private String fecha_fin = "";


// retrieve preference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        fecha = new ArrayList<String>();


        intent = getIntent();

        // Pongo el texto en el nuevo textview
        textView = findViewById(R.id.fecha_evento);
        textView.setText(crear_fecha());

        fecha_fin = textView.getText().toString();
        fechas_prueba.add(fecha_fin);
    }

    private String crear_fecha(){
        fecha = intent.getStringArrayListExtra("fecha");
        String dia = fecha.get(0);
        String mes = fecha.get(1);
        String anio = fecha.get(2);

        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }


    public void agregarEvento(View view) throws ParseException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(fecha_fin, fecha_fin);
        /*JSONArray a = new JSONArray();
        for (int i = 0; i < fechas_prueba.size(); i++) {
            a.put(fechas_prueba.get(i));
        }
        if (!fechas_prueba.isEmpty()) {
            editor.putString("fechas", a.toString());
        } else {
            editor.putString("fechas", null);
        }
        */
        editor.commit();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
