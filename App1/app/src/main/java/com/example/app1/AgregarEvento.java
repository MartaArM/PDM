package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class AgregarEvento extends AppCompatActivity {
    private ArrayList<String> fecha;
    public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        fecha = new ArrayList<String>();

        intent = getIntent();

        // Pongo el texto en el nuevo textview
        TextView textView = findViewById(R.id.fecha_evento);
        textView.setText(crear_fecha());
    }

    private String crear_fecha(){
        fecha = intent.getStringArrayListExtra("fecha");
        String dia = fecha.get(0);
        String mes = fecha.get(1);
        String anio = fecha.get(2);

        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }
}
