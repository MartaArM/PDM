package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;
import com.example.app1.Entidad.Evento;

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
    public AppDatabase db;


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

        et_titulo = findViewById(R.id.ettitulo);
        et_hora_inicio = findViewById(R.id.ethora_inicio);
        et_hora_fin = findViewById(R.id.ethora_fin);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        Evento e = new Evento(fecha_fin, et_titulo.toString(), et_hora_inicio.toString(), et_hora_fin.toString());

        db.eventoDao().aniadir(e);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
