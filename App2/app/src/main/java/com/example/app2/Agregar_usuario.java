package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

public class Agregar_usuario extends AppCompatActivity {

    String user_n;
    EditText et_usuario, et_clave, et_nombre, et_hora_e, et_hora_s;
    String usuario, clave, nombre, hora_e, hora_s;
    AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
    }

    public void guardar(View v) {
        et_usuario = findViewById(R.id.et_usuario);
        et_clave = findViewById(R.id.etclave);
        et_nombre = findViewById(R.id.et_nombre);
        et_hora_e = findViewById(R.id.et_hora_e);
        et_hora_s = findViewById(R.id.et_hora_s);

        usuario = et_usuario.getText().toString();
        clave = et_clave.getText().toString();
        nombre = et_nombre.getText().toString();
        hora_e = et_hora_e.getText().toString();
        hora_s = et_hora_s.getText().toString();

        if (usuarioExiste(usuario)) {
            mostrarMensaje("El nombre de usuario ya existe");
        }
        else {
            Usuario us = new Usuario(usuario, clave, nombre, hora_e, hora_s, "");
            db.usuarioDao().insert(us);

            mostrarMensaje("Usuario añadido con exito.");

            Intent intent = new Intent(this, Administracion.class);
            intent.putExtra("name", user_n);
            startActivity(intent);
            finish();
        }

    }

    private boolean usuarioExiste(String nombre) {
        boolean existe = false;

        if (db.usuarioDao().getUsuarioNombreUsuario(nombre) != null) {
            existe = true;
        }

        return existe;
    }

    private void mostrarMensaje(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(Agregar_usuario.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
