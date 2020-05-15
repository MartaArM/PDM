package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

public class MainActivity extends AppCompatActivity {

    TextView mensaje;
    EditText nombre_usuario, clave;
    AppDatabase db;
    private Intent intent;
    public String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mensaje de error
        mensaje = findViewById(R.id.tvmensaje);

        intent = getIntent();
        msg = intent.getStringExtra("msg");
        if (msg != null) {
            mensaje.setText(msg);
        }
        else {
            mensaje.setVisibility(View.GONE);
        }

        msg = "";
        //Conexión a base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public void comprobarDatos(View view) {
        nombre_usuario = findViewById(R.id.etusuario);
        clave = findViewById(R.id.etclave);

        String nombre_us = nombre_usuario.getText().toString();
        String pass = clave.getText().toString();

        Usuario user = db.usuarioDao().getUsuarioNombreUsuario(nombre_us);

        // El nombre de usuario no existe
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("msg", "No existe el usuario");
            startActivity(intent);
            finish();
        }
        else {
            String clave_aux = user.getClave();
            if (pass.equals(clave_aux)) {
                Intent intent = new Intent(this, lugar.class);
                //intent.putStringArrayListExtra("fecha", array_fecha);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("msg", "La clave no esa correcta");
                startActivity(intent);
                finish();
            }
        }

    }
}
