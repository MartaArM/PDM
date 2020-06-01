package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Ver_horario extends AppCompatActivity {

    String nombre_us, usuario, fecha, hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_horario);

        Intent intent = getIntent();
        nombre_us = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");
        fecha = intent.getStringExtra("fecha");
        hora = intent.getStringExtra("hora");


    }
}
