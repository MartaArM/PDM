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

import java.util.Calendar;
import java.util.Date;

public class motivo_tarde extends AppCompatActivity {

    Intent i;
    String user_n;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivo_tarde);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
    }

    public void motivo(View view){
        String motivo = "";
        switch (view.getId()) {
            case R.id.medico:
                motivo = "medico";
                break;
            case R.id.motivos_sindicales:
                motivo = "sindicato";
                break;
            case R.id.motivos_familiares:
                motivo = "familia";
                break;
            case R.id.trafico:
                motivo = "trafico";
                break;
            case R.id.otros:
                motivo = "otros";
                break;
        }

        i = getIntent();
        user_n = i.getStringExtra("name");

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);
        Horas h = new Horas(Long.toString(us.getId()), dia_actual(), hora_actual(), motivo,
                "fichaje_entrada", "");
        db.horasDao().insert(h);

        mostrarMensajeCerrar("El fichaje ha sido correcto.");
    }

    private String dia_actual() {
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        mes+=1;
        int anio = c.get(Calendar.YEAR);

        String fecha = "";
        String m = mes + "";
        String d = dia + "";
        if (mes < 10) {
            m = "0" + mes;
        }
        if (dia < 10) {
            d = "0" + dia;
        }
        fecha = d + "/" + m + "/" + anio;
        return fecha;
    }

    private String hora_actual() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        String h, m;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            h = "0" + hour;
        }
        else {
            h = hour + "";
        }

        int min = calendar.get(Calendar.MINUTE);

        if (min < 10) {
            m = "0" + min;
        }
        else {
            m = min + "";
        }

        String hora = h + ":" + m;
        return hora;
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(motivo_tarde.this);

        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }
}
