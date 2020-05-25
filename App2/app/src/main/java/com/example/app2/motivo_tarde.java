package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

    TextView t;
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
}
