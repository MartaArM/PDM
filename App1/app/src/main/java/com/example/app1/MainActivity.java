package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String fecha = "";
    private CalendarView calendario;
    public String mes, anio, dia;
    private ListView lv;
    private ArrayList<String> array_fecha;
    private static final int MY_PERMISSION = 0;
    ArrayList<String> list = new ArrayList<String>();


    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    public static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    public static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendario = (CalendarView) findViewById(R.id.calendarView);
        lv = (ListView) findViewById(R.id.lvEventos);

        List<String> your_array_list = new ArrayList<String>();

        /*your_array_list.add("foo");
        your_array_list.add("bar");*/




        //Cojo la fecha actual por si no cambio de día
        fecha_actual();
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                anio = Integer.toString(year);
                mes = Integer.toString(month + 1);
                dia = Integer.toString(dayOfMonth);
            }
        });

// retrieve preference
        your_array_list = leerEventos();
        System.out.println("SIZE: " + your_array_list.size());

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String>
                arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);
        lv.setAdapter(arrayAdapter);
    }

    public void enviarFecha(View view) {
        array_fecha = new ArrayList<String>();
        array_fecha.add(dia);
        array_fecha.add(mes);
        array_fecha.add(anio);
        // El segundo parámetro es la nueva actividad que va a comenzar
        Intent intent = new Intent(this, AgregarEvento.class);
        //intent.putExtra(ANIO, anio);
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
        String json = prefs.getString("fechas", null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}
