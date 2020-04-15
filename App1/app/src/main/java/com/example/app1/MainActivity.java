package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;
import com.example.app1.Entidad.Evento;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendario;
    public String mes, anio, dia;
    private ListView lv;
    private ArrayList<String> array_fecha;
    List<String> your_array_list;
    ArrayAdapter<String> arrayAdapter;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendario = findViewById(R.id.calendarView);
        lv = findViewById(R.id.lvEventos);

        your_array_list = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list);
        lv.setAdapter(arrayAdapter);

        fecha_actual(); // Fecha actual por si no cambio de dia
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                anio = Integer.toString(year);
                // Corregir formato del mes y día
                SimpleDateFormat dmd = new SimpleDateFormat("MM");
                Date m = null;
                try {
                    m = dmd.parse(Integer.toString(month));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dms = new SimpleDateFormat("MM");
                mes = dms.format(m);
                SimpleDateFormat ddd = new SimpleDateFormat("dd");
                Date d = null;
                try {
                     d = ddd.parse(Integer.toString(dayOfMonth));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dds = new SimpleDateFormat("dd");
                dia = dds.format(d);

                your_array_list.clear(); // limpiar array
                your_array_list.addAll(leerEventos()); //leer eventos de esta fecha

                arrayAdapter.notifyDataSetChanged(); // cambiar la lista

            }
        });
         db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
         List<Evento> events = db.eventoDao().getAllEventos();



    }

    private String dameFecha() {
        String fecha = dia + "/" + mes + "/" + anio;
        return fecha;
    }

    public void enviarFecha(View view) {
        array_fecha = new ArrayList<String>();
        array_fecha.add(dia);
        array_fecha.add(mes);
        array_fecha.add(anio);
        // El segundo parámetro es la nueva actividad que va a comenzar
        Intent intent = new Intent(this, AgregarEvento.class);
        intent.putStringArrayListExtra("fecha", array_fecha);
        startActivity(intent);
    }

    public void fecha_actual() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd");
        dia = df.format(c);
        df = new SimpleDateFormat("MM");
        mes = df.format(c);
        df = new SimpleDateFormat("YYYY");
        anio = df.format(c);


    }

    private ArrayList<String> leerEventos() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<String> urls = new ArrayList<String>();
        for (int i =0; i<50; i++) {
            String json = prefs.getString(dameFecha(), null);
            if (json != null) {
                urls.add(json);
            }
        }


        return urls;
    }
}
