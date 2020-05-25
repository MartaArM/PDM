package com.example.app2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Administracion extends AppCompatActivity {

    String user_n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");
    }

    public void agregar_usuario(View v) {
        Intent intent = new Intent(this, Agregar_usuario.class);
        intent.putExtra("name", user_n);
        startActivity(intent);
    }
}
