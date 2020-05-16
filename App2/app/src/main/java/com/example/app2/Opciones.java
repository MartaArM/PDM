package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

public class Opciones extends AppCompatActivity {
    AppDatabase db;
    Button b_settings;
    ImageView iv_settings;
    String user_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);
        if (!us.getTipo().equals("admin")) {
            b_settings = findViewById(R.id.bajustes);
            iv_settings = findViewById(R.id.imageView8);

            b_settings.setVisibility(View.GONE);
            iv_settings.setVisibility(View.GONE);
        }

    }

    public void visitas(View view) {
        Intent intent = new Intent(this, lugar.class);
        intent.putExtra("name", user_n);
        startActivity(intent);
    }
}
