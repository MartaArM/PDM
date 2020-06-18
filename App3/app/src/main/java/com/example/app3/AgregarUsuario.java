package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Usuario;

public class AgregarUsuario extends AppCompatActivity {

    EditText etusuario, etclave;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

    }

    public void guardar(View v){
        etusuario = findViewById(R.id.et_usuario);
        etclave = findViewById(R.id.et_clave);

        String usuario = etusuario.getText().toString();
        String clave = etclave.getText().toString();

        Usuario us_aux = db.usuarioDao().getUsuarioNombreUsuario(usuario);

        if (usuario.isEmpty() || usuario == "") {
            mostrarMensaje("El nombre de usuario no puede estar vacío");
        }

        if (clave.isEmpty() || clave == ""){
            mostrarMensaje("La clave no puede estar vacía");
        }


        if (us_aux == null) {
            Usuario us = new Usuario(usuario, clave);
            db.usuarioDao().insert(us);
            mostrarMensajeCerrar("El usuario ha sido añadido con éxito.");
        }
        else {
            mostrarMensaje("El nombre de usuario ya existe");
        }
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarUsuario.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    private void mostrarMensaje(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarUsuario.this);

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
