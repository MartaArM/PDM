package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app2.Database.AppDatabase;
import com.example.app2.Entidad.Horas;
import com.example.app2.Entidad.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Opciones extends AppCompatActivity {
    AppDatabase db;
    Button b_settings;
    ImageView iv_settings;
    String user_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        Intent intent = getIntent();
        user_n = intent.getStringExtra("name");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);
        if (!us.getTipo().equals("admin")) {
            b_settings = findViewById(R.id.bajustes);
            iv_settings = findViewById(R.id.imageView8);

            b_settings.setVisibility(View.GONE);
            iv_settings.setVisibility(View.GONE);
        }

    }

    // Botón salida
    public void visitas(View view) {
        Intent intent = new Intent(this, lugar.class);
        intent.putExtra("name", user_n);
        startActivity(intent);
    }

    public void fichar_entrada(View view) {
        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);
        String hora_entrada = us.getHora_entrada();

        String hora_entrada_1 = sumarMinutos(hora_entrada);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String hora = hour + ":" + min;

        boolean comp = compararHoras(hora_entrada_1, hora);
        if (comp){ // Llega a su hora
            //public Horas( String id_usuario, String dia, String hora_entrada, String hora_salida,
                    //String motivo_tarde, String accion, String localizacion)
            Horas h = new Horas(Long.toString(us.getId()), dia_actual(), hora, "",
                    "fichaje_entrada", "");
            db.horasDao().insert(h);

            AlertDialog.Builder builder = new AlertDialog.Builder(Opciones.this);

            builder.setMessage("Fichaje correcto.");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
        else {
            Intent intent = new Intent(this, motivo_tarde.class);
            intent.putExtra("name", user_n);
            startActivity(intent);
        }

    }

    public void fichar_salida(View view) {
        Usuario us = db.usuarioDao().getUsuarioNombreUsuario(user_n);

        String hora = hora_actual();

        Horas h = new Horas(Long.toString(us.getId()), dia_actual(), hora, "",
                "fichaje_salida", "");
        db.horasDao().insert(h);

        AlertDialog.Builder builder = new AlertDialog.Builder(Opciones.this);

        builder.setMessage("Fichaje correcto.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    // Suma 15 minutos a la hora de entrada
    private String sumarMinutos(String hora){
        String sDate1="31/12/1998"+hora;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm");
        Date dt = new Date();

        try {
            dt = sdf.parse(sDate1);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MINUTE, 15);

        return calendar.getTime().toString().substring(11, 16);
    }

    //Verdad si llega a su hora, falso si llega tarde
    private boolean compararHoras(String h1, String h2) {
        String sDate1="31/12/1998"+ h1; // hora de entrada
        String sDate2="31/12/1998"+h2; // hora actual

        boolean fichaje = false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm");
        Date dt1 = new Date();
        Date dt2 = new Date();

        try {
            dt1 = sdf.parse(sDate1);
            dt2 = sdf.parse(sDate2);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

        if(dt1.compareTo(dt2) > 0) { //dt2 mayor que dt1
            fichaje = true;
        } else if(dt1.compareTo(dt2) <= 0) { // dt2 menor o igual que dt1
            fichaje = false;
        }
        return fichaje;
    }

    // Sacar día actual para fichar
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

    // Sacar la hora actual para fichaje
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

    // Botón de administración
    public void administracion(View v) {
        Intent intent = new Intent(this, Administracion.class);
        intent.putExtra("name", user_n);
        startActivity(intent);
    }
}
