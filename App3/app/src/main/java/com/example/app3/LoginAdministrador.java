package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Usuario;

public class LoginAdministrador extends AppCompatActivity {

    private AppDatabase db;
    private EditText enombre, eclave;
    private TextView error_us, error_cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_administrador);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        error_us = findViewById(R.id.error_usuario);
        error_cl = findViewById(R.id.error_clave);

        error_us.setText("");
        error_cl.setText("");

    }

    public void acceder(View v) {
        enombre = findViewById(R.id.et_usuario);
        eclave = findViewById(R.id.et_clave);

        String user = enombre.getText().toString();

        Usuario u = db.usuarioDao().getUsuarioNombreUsuario(user);

        if (u == null) {
            error_us.setText("El nombre de usuario no existe");
        }
        else {
            String clave = eclave.getText().toString();
            if (!clave.equals(u.getClave()) ) {
                error_cl.setText("La clave no es correcta.");
            }
            else {
                Intent intent = new Intent(this, Administracion.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }

    }
}
