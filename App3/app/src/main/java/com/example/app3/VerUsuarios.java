package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ListView;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Usuario;
import com.example.app3.Entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public class VerUsuarios extends AppCompatActivity {

    ListView lv_us;
    private AppDatabase db;
    ArrayList<String> users;
    MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        
        users = new ArrayList<String>();
        lv_us = findViewById(R.id.listview);

        adapter = new MyCustomAdapter(users,this);
        lv_us.setAdapter(adapter);

        rellenar_lista();
    }

    private void rellenar_lista() {

        List<Usuario> usuarios = db.usuarioDao().getAllUsuarios();
        if (!usuarios.isEmpty()) {
            users.clear(); // limpiar array
            for (Usuario u : usuarios) {
                String us = u.getNombre_usuario();
                users.add(us);
            }
        }
        else {
            users.add("No hay usuarios");
            lv_us.setClickable(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        rellenar_lista();
    }
}


