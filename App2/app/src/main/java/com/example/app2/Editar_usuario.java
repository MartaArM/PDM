package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

public class Editar_usuario extends AppCompatActivity {

    EditText et_usuario, et_clave, et_nombre, et_hora_e, et_hora_s;
    String user_n, usuario;
    AppDatabase db;
    RadioGroup rg;
    Intent i;
    String tipo, usuario1, clave, nombre, hora_e, hora_s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");

        i = new Intent(this, Ver_usuario.class);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        rellenarTexto();

    }

    private void rellenarTexto(){
        et_usuario = findViewById(R.id.et_usuario);
        et_nombre = findViewById(R.id.et_nombre);
        et_hora_e = findViewById(R.id.et_hora_e);
        et_hora_s = findViewById(R.id.et_hora_s);
        rg = findViewById(R.id.rg_opcion);
        RadioButton radio1 = null;

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(usuario);

        et_usuario.setText(usuario);
        et_nombre.setText(us.getNombre());
        et_hora_e.setText(us.getHora_entrada());
        et_hora_s.setText(us.getHora_salida());

        if (us.getTipo().equals("admin")) {
            radio1 = findViewById(R.id.rb_admin);
        }
        else if (us.getTipo().equals("comercial")) {
            radio1 = findViewById(R.id.rb_comercial);
        }
        else if (us.getTipo().equals("montador")) {
            radio1 = findViewById(R.id.rb_montador);
        }
        else if (us.getTipo().equals("repartidor")) {
            radio1 = findViewById(R.id.rb_repartidor);
        }

        radio1.setChecked(true);

    }

    public void guardar(View v) {
        et_usuario = findViewById(R.id.et_usuario);
        et_clave = findViewById(R.id.etclave);
        et_nombre = findViewById(R.id.et_nombre);
        et_hora_e = findViewById(R.id.et_hora_e);
        et_hora_s = findViewById(R.id.et_hora_s);

        rg = findViewById(R.id.rg_opcion);
        int rb_selected = rg.getCheckedRadioButtonId();

        if (rb_selected == R.id.rb_admin) {
            tipo = "admin";
        } else if (rb_selected == R.id.rb_comercial) {
            tipo = "comercial";
        } else if (rb_selected == R.id.rb_montador) {
            tipo = "montador";
        } else if (rb_selected == R.id.rb_repartidor) {
            tipo = "repartidor";
        }

        usuario1 = et_usuario.getText().toString();
        clave = et_clave.getText().toString();
        nombre = et_nombre.getText().toString();
        hora_e = et_hora_e.getText().toString();
        hora_s = et_hora_s.getText().toString();

        db.usuarioDao().actualizarUsuario(usuario1, clave, nombre, hora_e, hora_s, usuario);

        mostrarMensajeCerrar("Usuario editado con exito.");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(Editar_usuario.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                i.putExtra("name", user_n);
                i.putExtra("user", usuario1);
                startActivity(i);
                finish();
            }
        });

        builder.show();
    }
}
