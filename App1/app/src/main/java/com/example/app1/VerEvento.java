package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;

import java.util.ArrayList;

public class VerEvento extends AppCompatActivity {
    ArrayList<String> valores;
    private Intent intent;
    private TextView tvFecha, tvTitulo, tvHoraI, tvHoraF;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);

        intent = getIntent();
        valores = intent.getStringArrayListExtra("valores");

        ponerFecha();
        ponerTitulo();
        ponerHoraInicio();
        ponerHoraFin();
    }

    public void ponerFecha() {
        tvFecha = findViewById(R.id.tvfecha);
        tvFecha.setText(valores.get(0));
    }

    public void ponerTitulo() {
        tvTitulo = findViewById(R.id.tvtitulo);
        tvTitulo.setText(valores.get(3));
    }

    public void ponerHoraInicio() {
        tvHoraI = findViewById(R.id.tvhorainicio);
        tvHoraI.setText(valores.get(1));
    }

    public void ponerHoraFin() {
        tvHoraF = findViewById(R.id.tvhorafin);
        tvHoraF.setText(valores.get(2));
    }
    public void eliminarEvento(View view){
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        db.eventoDao().deleteByHora(valores.get(0), valores.get(1));
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void editarEvento(View view) {
        Intent intent = new Intent(this, EditarEvento.class);
        intent.putStringArrayListExtra("valores", valores);
        startActivity(intent);
    }
}
