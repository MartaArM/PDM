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

public class AgregarEvento extends AppCompatActivity {
    private ArrayList<String> fecha;
    public Intent intent;
    private TextView textView;
    private EditText et_titulo, et_hora_inicio, et_hora_fin, et_descripcion;
    public ArrayList<String> fechas_prueba = new ArrayList<String>();
    private String fecha_fin = "";
    public AppDatabase db;


// retrieve preference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        fecha = new ArrayList<String>();


        intent = getIntent();

        // Pongo el texto en el nuevo textview
        textView = findViewById(R.id.fecha_evento);
        try {
            textView.setText(poner_fecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fecha_fin = crear_fecha_ok();

    }

    private String poner_fecha() throws ParseException {
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(crear_fecha());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
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
        return fecha_v;
    }
    private String crear_fecha(){
        fecha = intent.getStringArrayListExtra("fecha");
        String dia = fecha.get(0);
        String mes = fecha.get(1);
        Integer mes_i = Integer.parseInt(mes);
        //mes_i+=1;

        if (mes_i < 10) {
            mes = "0" + mes_i.toString();
        }
        else {
            mes = mes_i.toString();
        }
        String anio = fecha.get(2);
        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }

    private String crear_fecha_ok(){
        fecha = intent.getStringArrayListExtra("fecha");
        String dia = fecha.get(0);
        String mes = fecha.get(1);
        Integer mes_i = Integer.parseInt(mes);
        //mes_i+=1;

        if (mes_i < 10) {
            mes = "0" + mes_i.toString();
        }
        else {
            mes = mes_i.toString();
        }
        String anio = fecha.get(2);
        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }


    public void agregarEvento(View view) throws ParseException {

        et_titulo = findViewById(R.id.ettitulo);
        et_hora_inicio = findViewById(R.id.ethorainicio);
        et_hora_fin = findViewById(R.id.ethorafin);
        et_descripcion = findViewById(R.id.et_descripcion);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        Evento e = new Evento(fecha_fin, et_titulo.getText().toString(), et_hora_inicio.getText().toString(),
                et_hora_fin.getText().toString(), et_descripcion.getText().toString());

        db.eventoDao().aniadir(e);

        /*Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }
}
