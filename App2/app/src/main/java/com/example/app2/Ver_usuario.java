package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

public class Ver_usuario extends AppCompatActivity {

    String user_n, usuario;
    TextView tvnombre, tvhorae, tvhoras, tvusuario, tvtipo;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");
        usuario = usuario.substring(1);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        mostrarDatos();
    }

    private void mostrarDatos(){

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(usuario);

        tvnombre = findViewById(R.id.tv_nombre);
        tvnombre.setText(us.getNombre());

        tvhorae = findViewById(R.id.tv_hora_e);
        tvhorae.setText(us.getHora_entrada());

        tvhoras = findViewById(R.id.tv_hora_s);
        tvhoras.setText(us.getHora_salida());

        tvusuario = findViewById(R.id.tv_n_usuario);
        tvusuario.setText(usuario);
        
        tvtipo = findViewById(R.id.tv_tipo);

        String tipo = us.getTipo();

        if (tipo.equals("admin")) {
            tvtipo.setText("Administrador");
        }
        else if (tipo.equals("comercial")) {
            tvtipo.setText("Comercial");
        }
        else if (tipo.equals("repartidor")) {
            tvtipo.setText("Repartidor");
        }
        else if (tipo.equals("montador")) {
            tvtipo.setText("Montador");
        }
        else {
            tvtipo.setText("");
        }

    }
}
