package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Cita;

public class EditarCita extends AppCompatActivity {

    EditText etnombre, etfecha, ethora, ethoraf, etdescripcion, etemail;
    private AppDatabase db;
    long id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cita);

        Intent i = getIntent();
        id = i.getLongExtra("id", 0);

        etnombre = findViewById(R.id.et_nombre);
        etfecha = findViewById(R.id.et_fecha);
        ethora = findViewById(R.id.et_hora);
        ethoraf = findViewById(R.id.et_hora_f);
        etdescripcion = findViewById(R.id.et_descripcion);
        etemail = findViewById(R.id.et_email);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        rellenar_datos();
    }

    private void rellenar_datos() {
        Cita c = db.citaDao().getCitaId(id);
        etnombre.setText(c.getTitulo());
        etfecha.setText(c.getFecha());
        ethora.setText(c.getHora_inicio());
        ethoraf.setText(c.getHora_fin());
        etdescripcion.setText(c.getDescripcion());
        etemail.setText(c.getEmail());
    }

    public void guardar(View v) {
        String nombre = etnombre.getText().toString();
        String fecha = etfecha.getText().toString();
        String hora = ethora.getText().toString();
        String horaf = ethoraf.getText().toString();
        String descripcion = etdescripcion.getText().toString();
        String email = etemail.getText().toString();

        db.citaDao().actualizarCita(nombre, fecha, hora, horaf, descripcion, email, id);
        mostrarMensajeCerrar("La cita ha sido actualizada con éxito");
    }

    private void mostrarMensajeCerrar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarCita.this);

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
