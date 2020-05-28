package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
    SearchView sv_busqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        mostrarUsuarios();

        lv_usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                verUsuario(item);
            }
        });

        sv_busqueda = findViewById(R.id.busqueda);
        // Para buscar un usuario por nombre o nombre de usuario
        sv_busqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usuarios.clear();

                List<Usuario> users = db.usuarioDao().getAllUsuarios();
                for(Usuario us : users) {
                    if (us.getNombre().contains(newText) || us.getNombre_usuario().contains(newText)) {
                        String user = us.getNombre() + "\n" + us.getNombre_usuario();
                        usuarios.add(user);
                    }
                }

                arrayAdapter.notifyDataSetChanged(); // cambiar la lista
                return false;
            }
        });
    }

    private void mostrarUsuarios(){
        lv_usuarios = findViewById(R.id.lv_usuarios);
        usuarios = new ArrayList<String>();
        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios) {
            arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, usuarios){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    // Get the current item from ListView
                    View view = super.getView(position,convertView,parent);

                    // Get the Layout Parameters for ListView Current Item View
                    ViewGroup.LayoutParams params = view.getLayoutParams();

                    // Set the height of the Item View
                    params.height = 160;

                    view.setLayoutParams(params);

                    return view;
                }
            };

        lv_usuarios.setAdapter(arrayAdapter);
        List<Usuario> users = db.usuarioDao().getAllUsuarios();
        for(Usuario us : users) {
            String user = us.getNombre() + "\n" + us.getNombre_usuario();
            usuarios.add(user);
        }
    }

    private void verUsuario(Object item) {
        String us = item.toString();

        int concat = us.indexOf("\n");
        String n_us = us.substring(concat);

        Intent intent = new Intent(this, Ver_usuario.class);
        intent.putExtra("name", user_n);
        intent.putExtra("user", n_us);
        startActivity(intent);
    }

}
