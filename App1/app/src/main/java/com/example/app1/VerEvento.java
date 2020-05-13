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
import com.example.app1.Entidad.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VerEvento extends AppCompatActivity {
    ArrayList<String> valores;
    private Intent intent;
    private TextView tvFecha, tvTitulo, tvDescripcion, textView;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);

        intent = getIntent();
        valores = intent.getStringArrayListExtra("valores");

        try {
            ponerFecha();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ponerTitulo();
        ponerDescripcion();
    }

    public void ponerFecha() throws ParseException {
        tvFecha = findViewById(R.id.tvfecha);
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(valores.get(0));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.MONTH, 1);
        date1 = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String diaSemana = sdf.format(date1);

        sdf = new SimpleDateFormat("dd");
        String dia_v = sdf.format(date1);

        sdf = new SimpleDateFormat("MMMM");
        String mes_v = sdf.format(date1);

        sdf = new SimpleDateFormat("yyyy");
        String anio_v = sdf.format(date1);


        String fecha_v = diaSemana + ", " + dia_v + " de " + mes_v + ", " + valores.get(1) + "-"
                + valores.get(2);
        tvFecha.setText(fecha_v);
    }

    public void ponerTitulo() {
        tvTitulo = findViewById(R.id.tvtitulo);
        tvTitulo.setText(valores.get(3));
    }

    public void ponerDescripcion() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        List<Evento> events = db.eventoDao().getEventoFechaHoraTitulo(valores.get(0), valores.get(1), valores.get(3));
        String descripcion = "";

        for(Evento e : events) {
            descripcion = e.getDescripcion();
        }

        tvDescripcion = findViewById(R.id.tvdescripcion);

        if (descripcion.isEmpty()) {
            textView = findViewById(R.id.textView);
            textView.setVisibility(View.GONE);
            tvDescripcion.setVisibility(View.GONE);
        }
        else {
            tvDescripcion.setText(descripcion);
            valores.add(descripcion);
        }
    }


    public void eliminarEvento(View view){
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        db.eventoDao().deleteByHora(valores.get(0), valores.get(1), valores.get(3));
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void editarEvento(View view) {
        Intent intent = new Intent(this, EditarEvento.class);
        intent.putStringArrayListExtra("valores", valores);
        startActivity(intent);
    }
}
