package com.example.app3;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView mycal;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mycal = findViewById(R.id.calendarView);

        mycal.setSelectionColor(Color.parseColor("#80b3ff"));
        /*CalendarDay c = CalendarDay.from(2020, 6, 6);
        CalendarDay c1 = CalendarDay.today();
        final ArrayList<CalendarDay> cs = new ArrayList<>();
        cs.add(c);
        cs.add(c1);
        mycal.addDecorator(new CurrentDayDecorator(Color.RED, cs));*/
    }
}
