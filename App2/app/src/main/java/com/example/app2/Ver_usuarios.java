package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Ver_usuarios extends AppCompatActivity {

    ListView lv_usuarios;
    AppDatabase db;
    String user_n;
    List<String> usuarios;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        mostrarUsuarios();
    }

    private void mostrarUsuarios(){
        lv_usuarios = findViewById(R.id.lv_usuarios);
        usuarios = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios);

        lv_usuarios.setAdapter(arrayAdapter);
        List<Usuario> users = db.usuarioDao().getAllUsuarios();
        for(Usuario us : users) {
            String user = us.getNombre();
            usuarios.add(user);
        }
    }
}
