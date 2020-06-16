package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Notificacion;

public class VerNotificacion extends AppCompatActivity {

    Intent intent;
    long id;
    TextView tvnombre, tvfecha, tvhora, tvdescripcion, tvemail;
    private AppDatabase db;
    String nombre, fecha, hora, descripcion, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notificacion);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        tvnombre = findViewById(R.id.tv_nombre);
        tvfecha = findViewById(R.id.tv_fecha);
        tvhora = findViewById(R.id.tv_hora);
        tvdescripcion = findViewById(R.id.tv_descripcion);
        tvemail = findViewById(R.id.tv_email);

        intent = getIntent();
        id = intent.getLongExtra("id", 0);

        llenar_datos();

    }

    private void llenar_datos() {
        Notificacion n = db.notificacionDao().getNotificacionId(id);
        String texto = n.getTexto();

        int index_s = texto.indexOf("Nombre") + 8;
        int index_e = texto.indexOf("Fecha") - 1;
        nombre = texto.substring(index_s, index_e);
        tvnombre.setText(nombre);

        index_s = index_e + 8;
        index_e = texto.indexOf("Hora") - 1;
        fecha = texto.substring(index_s, index_e);
        tvfecha.setText(fecha);

        index_s = index_e + 7;
        index_e = texto.indexOf("Descripcion") - 1;
        hora = texto.substring(index_s, index_e);
        tvhora.setText(hora);

        index_s = index_e + 14;
        index_e = texto.indexOf("Email") - 1;
        descripcion = texto.substring(index_s, index_e);
        tvdescripcion.setText(descripcion);

        index_s = index_e + 8;
        email = texto.substring(index_s);
        tvemail.setText(email);
    }

    public void aprobar(View v) {
        Intent intent = new Intent(this, AgregarCita.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("fecha", fecha);
        intent.putExtra("hora", hora);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("email", email);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void denegar(View v) {
        String contenido = "La cita pedida para el " + fecha + " a las " + hora + " ha sido denegada.";
        new MailJob("martilla311@gmail.com", "a951753Z").execute(
                new MailJob.Mail("martilla311@gmail.com", email, "Cita cancelada", contenido)
        );
        db.notificacionDao().deleteById(id);
        mostrarMensajeCerrar("El mensaje de cancelación ha sido enviado.");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerNotificacion.this);

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
