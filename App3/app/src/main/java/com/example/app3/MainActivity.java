package com.example.app3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import static com.prolificinteractive.materialcalendarview.CalendarDay.from;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView mycal;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mycal = findViewById(R.id.calendarView);
        CalendarDay c = CalendarDay.today();
        final ArrayList<CalendarDay> cs = new ArrayList<>();
        cs.add(c);
        mycal.addDecorator(new CurrentDayDecorator(Color.RED, cs));
    }
}
