package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import static com.prolificinteractive.materialcalendarview.CalendarDay.from;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView mycal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mycal = findViewById(R.id.calendarView);
        String sDate1="06/06/2020";
        Date date1 = null;
        try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CalendarDay c = CalendarDay.from(2020, 6, 6);
        Collection<CalendarDay> cs = new ArrayList<>();
        cs.add(c);
        mycal.addDecorators(new CurrentDayDecorator(180, cs));

    }
}
