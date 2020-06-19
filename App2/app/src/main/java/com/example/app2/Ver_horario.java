package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Horas;
import com.example.app2.Entidad.Usuario;

import java.util.List;

public class Ver_horario extends AppCompatActivity {

    String nombre_us, usuario, fecha, hora;
    String accion, motivo, localizacion;
    TextView tv_accion, tv_fecha, tv_hora, tv_motivo, tv_localizacion;
    AppDatabase db;
    long id_hora;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_horario);

        Intent intent = getIntent();
        nombre_us = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");
        fecha = intent.getStringExtra("fecha");
        hora = intent.getStringExtra("hora");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        mostrarDatos();

        i = new Intent(this, Ver_horas_usuario.class);
    }

    private void mostrarDatos(){
        tv_accion = findViewById(R.id.tv_accion);
        tv_fecha = findViewById(R.id.tv_fecha);
        tv_hora = findViewById(R.id.tv_hora);
        tv_motivo = findViewById(R.id.tv_motivo);
        tv_localizacion = findViewById(R.id.tv_localizacion);

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(usuario);
        long id = us.getId();


        Horas hs = db.horasDao().getHorasIdDiaHora(id, hora, fecha);
        //Horas hs = db.horasDao().getHorasDia(fecha).get(0);

        if (hs.getAccion().equals("fichaje_entrada")) {
            accion ="Fichaje de entrada";
        } else if (hs.getAccion().equals("fichaje_salida")) {
            accion ="Fichaje de salida";
        } else if (hs.getAccion().equals("visita_cliente")) {
            accion ="Visita a cliente";
        } else {
            accion =hs.getAccion();
        }

        motivo = hs.getMotivo_tarde();
        localizacion = hs.getLocalizacion();

        tv_accion.setText(accion);
        tv_fecha.setText(fecha);
        tv_hora.setText(hora);

        if (motivo != "" && !motivo.isEmpty()) {
            tv_motivo.setText(motivo);
        }
        else {
            findViewById(R.id.textView22).setVisibility(View.GONE);
            tv_motivo.setVisibility(View.GONE);
        }

        if (localizacion != "" && !localizacion.isEmpty()) {
            tv_localizacion.setText(localizacion);
        }
        else {
            findViewById(R.id.textView23).setVisibility(View.GONE);
            tv_localizacion.setVisibility(View.GONE);
        }

        id_hora = hs.getId();
    }

    public void editar_horario(View v){
        Intent intent = new Intent(this, Editar_horario.class);
        intent.putExtra("name", nombre_us);
        intent.putExtra("user", usuario);
        intent.putExtra("id_hora", id_hora);
        startActivity(intent);
        finish();
    }

    public void eliminar_horario(View v){
        db.horasDao().deleteById(id_hora);
        mostrarMensajeCerrar("El fichaje ha sido eliminado");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(Ver_horario.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*i.putExtra("name", usuario);
                i.putExtra("user", nombre_us);
                startActivity(i);*/
                finish();
            }
        });

        builder.show();
    }
}
