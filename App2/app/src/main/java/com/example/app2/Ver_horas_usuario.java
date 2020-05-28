package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Horas;
import com.example.app2.Entidad.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Ver_horas_usuario extends AppCompatActivity {

    String user_n, usuario;
    AppDatabase db;
    TextView titulo;
    ListView lvHorario;
    List<String> horario;
    ArrayAdapter<String> arrayAdapter;
    Usuario us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_horas_usuario);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        us = db.usuarioDao().getUsuarioNombreUsuario(usuario);
        titulo = findViewById(R.id.tv_titulo);
        titulo.setText(us.getNombre());

        llenarHorario();

    }

    private void llenarHorario(){
        lvHorario = findViewById(R.id.lv_horario);
        horario = new ArrayList<String>();
        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios) {
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, horario){
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

        lvHorario.setAdapter(arrayAdapter);

        List<Horas> horas = db.horasDao().getHoras(us.getId());
        for(Horas hs : horas) {
            String hora = hs.getDia() + " - " + hs.getHora();
            if (hs.getMotivo_tarde() != "") {
                hora = hora + "\n" + "Motivo tarde: " + hs.getMotivo_tarde();
            }
            if (hs.getAccion().equals("fichaje_entrada")) {
                hora = hora + "\n" + "Acción: fichaje de entrada";
            }
            else if (hs.getAccion().equals("fichaje_salida")) {
                hora = hora + "\n" + "Acción: fichaje de salida";
            }
            else if (hs.getAccion().equals("visita_cliente")) {
                hora = hora + "\n" + "Acción: visita a cliente";
            }
            else if (hs.getAccion().equals("fichaje_salida")) {
                hora = hora + "\n" + "Acción: fichaje de salida";
            }
            else {
                hora = hora + "\n" + "Acción: " + hs.getAccion();
            }

            if (hs.getLocalizacion() != ""){
                hora = hora + "\n" + "Localización: " + hs.getLocalizacion();
            }
            horario.add(hora);
        }
    }
}
