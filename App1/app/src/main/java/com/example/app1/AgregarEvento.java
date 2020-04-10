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
import org.json.JSONException;
import org.json.JSONObject;

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
    public int n = 0;


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

        et_titulo = findViewById(R.id.ettitulo);
        et_hora_inicio = findViewById(R.id.ethora_inicio);
        et_hora_fin = findViewById(R.id.ethora_fin);

        JSONObject obj = new JSONObject();
        try {
            obj.put("titulo", et_titulo.getText().toString());
            obj.put("hora_inicio", et_hora_inicio.getText().toString());
            obj.put("hora_fin", et_hora_fin.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //editor.putString(fecha_fin + "_" + et_hora_inicio.getText().toString(), obj.toString());
        editor.putString(fecha_fin + n, obj.toString());
        n = n+1;
        editor.commit();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
