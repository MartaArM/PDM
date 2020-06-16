package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app3.Database.AppDatabase;
import com.example.app3.Entidad.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class VerNotificaciones extends AppCompatActivity {

    private List<String> notificaciones;
    private ArrayAdapter<String> arrayAdapter;
    private AppDatabase db;
    private ListView lv_not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notificaciones);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        notificaciones = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notificaciones);
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, notificaciones){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 350;
                params.width = 1000;

                view.setLayoutParams(params);

                return view;
            }
        };
        lv_not = findViewById(R.id.lv_notificaciones);
        lv_not.setAdapter(arrayAdapter);

        rellenar_lista();

        lv_not.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                verNotificacion(item);
            }
        });
    }

    private void rellenar_lista() {

        List<Notificacion> notifications = db.notificacionDao().getAllNotificaciones();
        if (!notifications.isEmpty()) {
            notificaciones.clear(); // limpiar array
            for (Notificacion n : notifications) {
                String not = n.getFecha() + " - " + n.getHora() +
                        "\n" + n.getTexto() + "\n\n" + "ID " + n.getId();
                notificaciones.add(not);
            }
        }
        else {
            notificaciones.add("No hay notificaciones");
            lv_not.setClickable(false);
        }

        arrayAdapter.notifyDataSetChanged(); // cambiar la lista
    }

    private void verNotificacion(Object item) {
        String not = item.toString();
        int index = not.indexOf("ID");

        String id = not.substring(index+3);
    }
}
