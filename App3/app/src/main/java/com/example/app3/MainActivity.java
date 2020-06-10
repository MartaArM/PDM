package com.example.app3;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView mycal;
    String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mycal = findViewById(R.id.calendarView);

        mycal.setSelectionColor(Color.parseColor("#80b3ff"));
        fecha = dia_actual();

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
}
