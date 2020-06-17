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
import com.example.app3.Entidad.Cita;

public class AgregarCita extends AppCompatActivity {

    TextView tvnombre, tvfecha, tvhora, tvdescripcion, tvemail;
    EditText et_hora;
    private AppDatabase db;
    Intent intent;
    String nombre, fecha, hora, descripcion, email;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cita);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        fecha = intent.getStringExtra("fecha");
        hora = intent.getStringExtra("hora");
        descripcion = intent.getStringExtra("descripcion");
        email = intent.getStringExtra("email");
        id = intent.getLongExtra("id", 0);

        llenar_datos();

    }

    private void llenar_datos() {
        tvnombre = findViewById(R.id.et_nombre);
        tvfecha = findViewById(R.id.et_fecha);
        tvhora = findViewById(R.id.et_hora);
        tvdescripcion = findViewById(R.id.et_descripcion);
        tvemail = findViewById(R.id.et_email);

        tvnombre.setText(nombre);
        tvfecha.setText(fecha);
        tvhora.setText(hora);
        tvdescripcion.setText(descripcion);
        tvemail.setText(email);
    }

    public void guardar(View v) {
        et_hora = findViewById(R.id.et_hora);
        String hora_f = et_hora.getText().toString();
        Cita c = new Cita(fecha, nombre, hora, hora_f, email, descripcion);

        db.citaDao().insert(c);

        String contenido = "La cita solicitada para el día " + fecha + " a las " + hora + " ha sido confirmada. " +
                "L@ esperamos." + "\n" + "Un saludo.";
        new MailJob("martilla311@gmail.com", "a951753Z").execute(
                new MailJob.Mail("martilla311@gmail.com", email, "Cita confirmada", contenido)
        );
        db.notificacionDao().deleteById(id);
        mostrarMensajeCerrar("El mensaje de confirmación ha sido enviado.");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarCita.this);

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
