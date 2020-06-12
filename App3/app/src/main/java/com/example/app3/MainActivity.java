package com.example.app3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView mycal;
    String fecha;
    List<String> mis_citas;
    ArrayAdapter<String> arrayAdapter;
    private ListView lv;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mycal = findViewById(R.id.calendarView);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        mycal.setSelectionColor(Color.parseColor("#80b3ff"));
        fecha = dia_actual();
        lv = findViewById(R.id.lv_citas);
        mis_citas = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mis_citas);
        lv.setAdapter(arrayAdapter);
        // Evento para coger la fecha al pulsarla en el calendario
        mycal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int dia, mes;
                String d, m, a;

                dia = date.getDay();
                if (dia < 10) {
                    d = "0" + dia;
                }
                else {
                    d = dia + "";
                }

                mes = date.getMonth();
                if (mes < 10) {
                    m = "0" + mes;
                }
                else {
                    m = mes + "";
                }

                a = date.getYear() + "";

                fecha = d + "/" + m + "/" + a;
            }
        });
        /*CalendarDay c = CalendarDay.from(2020, 6, 6);
        CalendarDay c1 = CalendarDay.today();
        final ArrayList<CalendarDay> cs = new ArrayList<>();
        cs.add(c);
        cs.add(c1);
        mycal.addDecorator(new CurrentDayDecorator(Color.RED, cs));*/
    }

    private String dia_actual() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String f = df.format(c);
        return f;

    }

    public void pedir_cita(View v) {
        Intent intent = new Intent(this, Pedir_cita.class);
        intent.putExtra("fecha", fecha);
        startActivity(intent);
    }

    public void accesoAdministrador(View v) {
        Intent intent = new Intent(this, LoginAdministrador.class);
        startActivity(intent);
    }
}
