package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Notificacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Pedir_cita extends AppCompatActivity {
    public Intent intent;
    AppDatabase db;
    private String fecha;
    private TextView tvtitulo, error1, error2, error3;
    private EditText etTitulo, etHora, etDescripcion, etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_cita);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        intent = getIntent();
        fecha = intent.getStringExtra("fecha");

        tvtitulo = findViewById(R.id.tv_titulo);
        try {
            tvtitulo.setText(poner_fecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        error1 = findViewById(R.id.error1);
        error2 = findViewById(R.id.error2);
        error3 = findViewById(R.id.error3);

        error1.setText("");
        error2.setText("");
        error3.setText("");

    }

    private String poner_fecha() throws ParseException {
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(fecha);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String diaSemana = sdf.format(date1);

        sdf = new SimpleDateFormat("dd");
        String dia_v = sdf.format(date1);

        sdf = new SimpleDateFormat("MMMM");
        String mes_v = sdf.format(date1);

        sdf = new SimpleDateFormat("yyyy");
        String anio_v = sdf.format(date1);


        String fecha_v = diaSemana + ", " + dia_v + " de " + mes_v + ", " + anio_v;
        fecha_v = fecha_v.substring(0, 1).toUpperCase() + fecha_v.substring(1);
        return fecha_v;
    }

    public void enviarNotificiacion(View v) {
        etTitulo = findViewById(R.id.et_titulo);
        etHora = findViewById(R.id.et_hora);
        etEmail = findViewById(R.id.et_email);

        boolean guardar = false;

        String titulo = etTitulo.getText().toString();
        if (titulo.isEmpty() || titulo == "") {
            error1.setText("El nombre es obligatorio");
            guardar = false;
        }
        else {
            guardar = true;
        }

        String hora = etHora.getText().toString();
        if (hora.isEmpty() || hora == "") {
            error2.setText("La hora es obligatoria");
            guardar = false;
        }
        else {
            guardar = true;
        }

        etDescripcion = findViewById(R.id.et_descripcion);
        String descripcion = etDescripcion.getText().toString();

        String email = etEmail.getText().toString();
        if (email.isEmpty() || email == "") {
            error3.setText("El email es obligatorio");
            guardar = false;
        }
        else {
            guardar = true;
        }

        if (guardar) {
            String texto = titulo + "\n" + "Fecha: " + fecha + "\n" + "Hora: " + hora + "\n" + "Descripcion: " + descripcion
                    + "\n" + "Email: " + email;

            Notificacion n = new Notificacion(dia_actual(), hora_actual(), texto);

            db.notificacionDao().aniadir(n);
            mostrarMensajeCerrar("La solicitud se ha hecho con éxito. Le llegará un correo confirmando " +
                    "o denegando la cita.");
        }
    }

    private String dia_actual() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String f = df.format(c);
        return f;

    }

    private String hora_actual() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("HH:MM");
        String f = df.format(c);
        return f;

    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(Pedir_cita.this);

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
