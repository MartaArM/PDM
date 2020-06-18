package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Usuario;

public class EditarUsuario extends AppCompatActivity {

    EditText etusuario, etclave;
    private AppDatabase db;
    String user, clave;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Intent i = getIntent();
        user = i.getStringExtra("user");

        etusuario = findViewById(R.id.et_usuario);
        etclave = findViewById(R.id.et_clave);

        etusuario.setText(user);

        Usuario us_aux = db.usuarioDao().getUsuarioNombreUsuario(user);
        clave = us_aux.getClave();

        etclave.setText(clave);
    }
    
    public void guardar(View v) {
        String usuario = etusuario.getText().toString();
        String clave = etclave.getText().toString();

        if (usuario.isEmpty() || usuario == "") {
            mostrarMensaje("El nombre de usuario no puede estar vacío");
        }

        if (clave.isEmpty() || clave == ""){
            mostrarMensaje("La clave no puede estar vacía");
        }

        if (!usuario.equals(user)) {
            Usuario us_aux = db.usuarioDao().getUsuarioNombreUsuario(usuario);
            if (us_aux == null) {
                db.usuarioDao().actualizarUsuarioNombre(usuario, clave, user);
                mostrarMensajeCerrar("El usuario ha sido editado con éxito.");
            } else {
                mostrarMensaje("El nombre de usuario ya existe");
            }
        }
        else {
            db.usuarioDao().actualizarUsuarioNombre(usuario, clave, user);
            mostrarMensajeCerrar("El usuario ha sido editado con éxito.");
        }





    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarUsuario.this);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarUsuario.this);

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
