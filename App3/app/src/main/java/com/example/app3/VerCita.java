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
import com.example.app3.Entidad.Cita;

public class VerCita extends AppCompatActivity {

    long id;
    TextView tvnombre, tvfecha, tvhora, tvhoraf, tvdescripcion, tvemail;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cita);

        Intent i = getIntent();
        id = i.getLongExtra("id", 0);

        tvnombre = findViewById(R.id.tv_nombre);
        tvfecha = findViewById(R.id.tv_fecha);
        tvhora = findViewById(R.id.tv_hora);
        tvhoraf = findViewById(R.id.tv_hora_f);
        tvdescripcion = findViewById(R.id.tv_descripcion);
        tvemail = findViewById(R.id.tv_email);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        rellenar_datos();

    }

    private void rellenar_datos() {
        Cita c = db.citaDao().getCitaId(id);
        tvnombre.setText(c.getTitulo());
        tvfecha.setText(c.getFecha());
        tvhora.setText(c.getHora_inicio());
        tvhoraf.setText(c.getHora_fin());
        tvdescripcion.setText(c.getDescripcion());
        tvemail.setText(c.getEmail());
    }

    public void eliminarCita(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerCita.this);

        builder.setMessage("¿Desea eliminar la cita?");

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.citaDao().deleteById(id);
                dialog.dismiss();
                mostrarMensajeCerrar("La cita ha sido eliminada.");
            }
        });



        builder.show();
    }

    public void editarCita(View v){
        
    }
    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerCita.this);

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
