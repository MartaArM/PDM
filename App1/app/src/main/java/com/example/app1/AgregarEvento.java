package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgregarEvento extends AppCompatActivity {
    private ArrayList<String> fecha;
    public Intent intent;
    private TextView textView;
    private EditText et_titulo, et_hora_inicio, et_hora_fin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        fecha = new ArrayList<String>();

        intent = getIntent();

        // Pongo el texto en el nuevo textview
        textView = findViewById(R.id.fecha_evento);
        textView.setText(crear_fecha());
    }

    private String crear_fecha(){
        fecha = intent.getStringArrayListExtra("fecha");
        String dia = fecha.get(0);
        String mes = fecha.get(1);
        String anio = fecha.get(2);

        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }


    public void agregarEvento(View view) throws ParseException {
        et_titulo = findViewById(R.id.ettitulo);
        String titulo = et_titulo.getText().toString();

        et_hora_inicio = findViewById(R.id.ethora_inicio);
        String hora_inicio = et_hora_inicio.getText().toString();

        et_hora_fin = findViewById(R.id.ethora_fin);
        String hora_fin = et_hora_fin.getText().toString();

        String fecha_inicio = crear_fecha() + " " + hora_inicio;
        String fecha_fin = crear_fecha() + " " + hora_fin;

        long tiempo_inicio = convertir_fecha(fecha_inicio);
        long tiempo_fin = convertir_fecha(fecha_fin);
        /*
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, titulo);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startDateMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                endDateMillis);
        intent.putExtra(Events.ALL_DAY, false);// periodicity
        intent.putExtra(Events.DESCRIPTION,strDescription));*/
    }

    private long convertir_fecha(String fecha) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYY HH:mm");
        try {
            Date mDate = sdf.parse(fecha);
            timeInMilliseconds = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
}
