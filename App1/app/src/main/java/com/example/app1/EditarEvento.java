package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;

import java.util.ArrayList;

public class EditarEvento extends AppCompatActivity {
    private TextView tvFecha;
    private EditText etTitulo, etHoraInicio, etHoraFin;
    ArrayList<String> valores;
    private Intent intent;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        intent = getIntent();
        valores = intent.getStringArrayListExtra("valores");

        ponerFecha();
        ponerTitulo();
        ponerHoraInicio();
        ponerHoraFin();


    }

    public void ponerFecha() {
        tvFecha = findViewById(R.id.tvedfecha);
        tvFecha.setText(valores.get(0));
    }

    public void ponerTitulo() {
        etTitulo = findViewById(R.id.ettitulo);
        etTitulo.setText(valores.get(3));
    }

    public void ponerHoraInicio() {
        etHoraInicio = findViewById(R.id.ethorainicio);
        etHoraInicio.setText(valores.get(1));
    }

    public void ponerHoraFin() {
        etHoraFin = findViewById(R.id.ethorafin);
        etHoraFin.setText(valores.get(2));
    }

    public void actualizarEvento(View view) {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        etTitulo = findViewById(R.id.ettitulo);
        String titulo = etTitulo.getText().toString();
        etHoraInicio = findViewById(R.id.ethorainicio);
        String hora_ini = etHoraInicio.getText().toString();
        etHoraFin = findViewById(R.id.ethorafin);
        String hora_fin = etHoraFin.getText().toString();

        db.eventoDao().actualizarEvento(valores.get(0), titulo, hora_ini, hora_fin, valores.get(0), valores.get(1), valores.get(3));
        ArrayList<String> valores_aux = new ArrayList<>();
        valores_aux.add(valores.get(0));
        valores_aux.add(hora_ini);
        valores_aux.add(hora_fin);
        valores_aux.add(titulo);

        //Actualizar la vista
        Intent intent = new Intent(EditarEvento.this, VerEvento.class);
        intent.putStringArrayListExtra("valores", valores_aux);
        finish();
        startActivity(intent);

    }
}
