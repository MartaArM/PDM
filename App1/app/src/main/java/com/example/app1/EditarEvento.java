package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;
import com.example.app1.Entidad.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditarEvento extends AppCompatActivity {
    private TextView tvFecha;
    private EditText etTitulo, etHoraInicio, etHoraFin, etDescripcion;
    ArrayList<String> valores;
    private Intent intent;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        intent = getIntent();
        valores = intent.getStringArrayListExtra("valores");

        try {
            ponerFecha();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ponerTitulo();
        ponerHoraInicio();
        ponerHoraFin();
        ponerDescripcion();
    }

    public void ponerFecha()  throws ParseException {
        tvFecha = findViewById(R.id.tvedfecha);
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


        String fecha_v = diaSemana + ", " + dia_v + " de " + mes_v;
        fecha_v = fecha_v.substring(0, 1).toUpperCase() + fecha_v.substring(1);

        tvFecha.setText(fecha_v);
    }

    public void ponerTitulo() {
        etTitulo = findViewById(R.id.ettitulo2);
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

    public void ponerDescripcion() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        List<Evento> events = db.eventoDao().getEventoFechaHoraTitulo(valores.get(0), valores.get(1), valores.get(3));
        String descripcion = "";

        for(Evento e : events) {
            descripcion = e.getDescripcion();
        }

        etDescripcion = findViewById(R.id.et_descripcion);
        etDescripcion.setText(descripcion);
    }

    public void actualizarEvento(View view) {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        etTitulo = findViewById(R.id.ettitulo2);
        String titulo = etTitulo.getText().toString();
        etHoraInicio = findViewById(R.id.ethorainicio);
        String hora_ini = etHoraInicio.getText().toString();
        etHoraFin = findViewById(R.id.ethorafin);
        String hora_fin = etHoraFin.getText().toString();
        etDescripcion = findViewById(R.id.et_descripcion);
        String descripcion = etDescripcion.getText().toString();

        db.eventoDao().actualizarEvento(valores.get(0), titulo, hora_ini, hora_fin, descripcion, valores.get(0), valores.get(1), valores.get(3));
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
