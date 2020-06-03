package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Horas;

public class Editar_horario extends AppCompatActivity {

    EditText et_accion, et_fecha, et_hora, et_motivo, et_localizacion;
    String nombre_us, usuario;
    String guardar_accion;
    String f, h, m, l;
    long id_hora;
    AppDatabase db;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_horario);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Intent intent = getIntent();
        nombre_us = intent.getStringExtra("name");
        usuario = intent.getStringExtra("user");
        id_hora = intent.getLongExtra("id_hora", 0);

        i = new Intent(this, Ver_horario.class);

        mostrar_datos();

    }
    
    private void mostrar_datos(){
        et_accion = findViewById(R.id.et_accion);
        et_fecha = findViewById(R.id.et_fecha);
        et_hora = findViewById(R.id.et_hora);
        et_motivo = findViewById(R.id.et_motivo);
        et_localizacion = findViewById(R.id.et_localizacion);

        Horas hs = db.horasDao().getHorasId(id_hora);

        String accion = "";

        if (hs.getAccion().equals("fichaje_entrada")) {
            accion ="Fichaje de entrada";
        } else if (hs.getAccion().equals("fichaje_salida")) {
            accion ="Fichaje de salida";
        } else if (hs.getAccion().equals("visita_cliente")) {
            accion ="Visita a cliente";
        } else if (hs.getAccion().equals("montaje")) {
            accion ="Montaje";
        } else if (hs.getAccion().equals("reparacion")) {
            accion ="Reparación";
        } else if (hs.getAccion().equals("reparto")) {
            accion ="Reparto";
        } else if (hs.getAccion().equals("oficina")) {
            accion ="Oficina";
        }

        et_accion.setText(accion);
        guardar_accion = hs.getAccion();

        et_fecha.setText(hs.getDia());
        et_hora.setText(hs.getHora());
        et_motivo.setText(hs.getMotivo_tarde());
        et_localizacion.setText(hs.getLocalizacion());

    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        et_accion = findViewById(R.id.et_accion);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fichaje_entrada:
                        et_accion.setText("Fichaje de entrada");
                        guardar_accion = "fichaje_entrada";
                        return true;
                    case R.id.fichaje_salida:
                        et_accion.setText("Fichaje de salida");
                        guardar_accion = "fichaje_salida";
                        return true;
                    case R.id.visita_cliente:
                        et_accion.setText("Visita a cliente");
                        guardar_accion = "visita_cliente";
                        return true;
                    case R.id.montaje:
                        et_accion.setText("Montaje");
                        guardar_accion = "montaje";
                        return true;
                    case R.id.reparacion:
                        et_accion.setText("Reparación");
                        guardar_accion = "reparacion";
                        return true;
                    case R.id.reparto:
                        et_accion.setText("Reparto");
                        guardar_accion = "reparto";
                        return true;
                    case R.id.oficina:
                        et_accion.setText("Oficina");
                        guardar_accion = "oficina";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.actions);
        popup.show();
    }

    public void guardar(View v){
        f = et_fecha.getText().toString();
        h = et_hora.getText().toString();
        m = et_motivo.getText().toString();
        l = et_localizacion.getText().toString();

        db.horasDao().actualizarHoras(f, h, m, guardar_accion, l, id_hora);
        mostrarMensajeCerrar("El fichaje se ha actualizado con éxito.");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(Editar_horario.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                i.putExtra("name", nombre_us);
                i.putExtra("user", usuario);
                i.putExtra("fecha", f);
                i.putExtra("hora", h);
                startActivity(i);
                finish();
            }
        });

        builder.show();
    }
}
