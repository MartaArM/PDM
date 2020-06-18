package com.example.app3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Cita;
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
    String user;
    Button btn;

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
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, mis_citas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 120;
                params.width = 1000;

                view.setLayoutParams(params);

                return view;
            }
        };
        lv.setAdapter(arrayAdapter);
        mycal.setDateSelected(CalendarDay.today(), true);
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

                rellenar_lista();
            }
        });

        puntos();

        Intent i = getIntent();
        user = i.getStringExtra("user");

        if (user != null){
            btn = findViewById(R.id.button);
            btn.setVisibility(View.INVISIBLE);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (user != null) {
                    verCita(item);
                }
            }
        });
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
        if (user != null) {
            intent.putExtra("user", user);
        }
        startActivity(intent);
    }

    public void accesoAdministrador(View v) {
        Intent intent = new Intent(this, LoginAdministrador.class);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        puntos();
        rellenar_lista();
    }

    private void puntos() {
        List<Cita> citas = db.citaDao().getAllCitas();
        CalendarDay cd;
        final ArrayList<CalendarDay> cs = new ArrayList<>();
        for(Cita c : citas) {
            String date = c.getFecha();
            String d = date.substring(0, 2);
            String m = date.substring(3, 5);
            String a = date.substring(6);

            cd = CalendarDay.from(Integer.parseInt(a), Integer.parseInt(m), Integer.parseInt(d));
            cs.add(cd);
        }
        mycal.addDecorator(new CurrentDayDecorator(Color.BLUE, cs));
    }

    private void rellenar_lista() {
        List<Cita> citas = db.citaDao().getCitaFecha(fecha);
        mis_citas.clear(); // limpiar array
        for(Cita c : citas) {
            String cita = "";
            if (user != null) {
                cita = c.getHora_inicio() + "-" + c.getHora_fin() + ": " +
                        c.getTitulo() + "\n" + c.getDescripcion() + "\n\n" + "ID " + c.getId();
            }
            else {
                cita = c.getHora_inicio() + "-" + c.getHora_fin() + " Ocupado";
            }
            mis_citas.add(cita);
        }

        arrayAdapter.notifyDataSetChanged(); // cambiar la lista
    }

    private void verCita(Object item) {
        String cita_t = item.toString();
        int index = cita_t.indexOf("ID");

        String id = cita_t.substring(index+3);
        long ide = Long.parseLong(id);

        Intent intent = new Intent(this, VerCita.class);
        intent.putExtra("id", ide);
        startActivity(intent);
    }
}
