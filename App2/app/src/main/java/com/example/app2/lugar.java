package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Horas;
import com.example.app2.Entidad.Usuario;

import java.util.Calendar;
import java.util.Date;

public class lugar extends AppCompatActivity {

    String user_n;
    AppDatabase db;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        i = new Intent(this, Administracion.class);
    }

    public void lugar(View view){
        String lugar = "";
        switch (view.getId()) {
            case R.id.bvisita_cliente:
                lugar = "visita_cliente";
                break;
            case R.id.bmontaje:
                lugar = "montaje";
                break;
            case R.id.b_arreglo:
                lugar = "reparacion";
                break;
            case R.id.b_reparto:
                lugar = "reparto";
                break;
            case R.id.boficina:
                lugar = "oficina";
                break;
        }

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);
        Horas h = new Horas(Long.toString(us.getId()), dia_actual(), hora_actual(), "",
                lugar, "");
        db.horasDao().insert(h);

        mostrarMensajeCerrar("Opción insertada con éxito.");
    }

    private String dia_actual() {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int anio = c.get(Calendar.YEAR);

        String fecha = "";
        if (mes < 10) {
            fecha = dia + "/" + "0" + mes + "/" + anio;
        }
        else {
            fecha = dia + "/" + mes + "/" + anio;
        }

        return fecha;
    }

    private String hora_actual() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String hora = hour + ":" + min;
        return hora;
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(lugar.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                i.putExtra("name", user_n);
                startActivity(i);
                finish();
            }
        });

        builder.show();
    }
}
