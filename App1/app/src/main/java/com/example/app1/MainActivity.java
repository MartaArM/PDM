package com.example.app1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.app1.Database.AppDatabase;
import com.example.app1.Entidad.Evento;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {
    private CalendarView calendario;
    private ListView lv;
    private ImageButton button_bot;
    public String mes, anio, dia;
    private ArrayList<String> array_fecha;
    List<String> your_array_list;
    ArrayAdapter<String> arrayAdapter;
    public AppDatabase db;
    SpeechRecognizer mySpeech;
    TextToSpeech myBot;
    Intent speechintent;
    String accessToken;

    private AIService aiService;

    String fecha_a = "";
    String titulo_a = "";
    String hora_a = "";
    String hora_b = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración de dialogflow
        accessToken = "198a751bfb7d4cdfbf4facae873d5186";
        final AIConfiguration config = new AIConfiguration(accessToken, AIConfiguration.SupportedLanguages.Spanish, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        // Comprobamos permisos para escuchar
        comprobarPermisos();

        calendario = findViewById(R.id.calendarView);
        lv = findViewById(R.id.lvEventos);
        button_bot = findViewById(R.id.ibbot);

        your_array_list = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list);
        lv.setAdapter(arrayAdapter); // list view donde se van a ver los eventos

        fecha_actual(); // Fecha actual por si no cambio de dia
        // Conectar con base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        // Método de cambio de fecha en calendarview
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
                String fecha_aux = dia + "/" + mes + "/" + anio;

                List<Evento> events = db.eventoDao().getEventoFecha(fecha_aux);
                your_array_list.clear(); // limpiar array
                for(Evento e : events) {
                    String ev = e.getHora_inicio() + "-" + e.getHora_fin() + "  " + e.getTitulo();
                    your_array_list.add(ev);
                }

                arrayAdapter.notifyDataSetChanged(); // cambiar la lista

            }
        });
        // En que evento del dia marcado estoy señalando
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Object item = parent.getItemAtPosition(position);
                 //System.out.println("ITEM: "+ item.toString());
                verEvento(item);
            }
        });

        myBot = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    myBot.setLanguage(Locale.getDefault());
                }
            }
        });

        mySpeech = SpeechRecognizer.createSpeechRecognizer(this);
        speechintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // Apretar el botón de escuchar (bot)
        button_bot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: // Cuando aprieto el botón
                        aiService.startListening();
                        break;
                    case MotionEvent.ACTION_UP: //Cuando lo suelto
                        aiService.stopListening();
                        break;
                }
                return false;
            }
        });


    }

    private String dameFecha() {
        String fecha = dia + "/" + mes + "/" + anio;
        return fecha;
    }
    // Se envía la fecha a la actividad de añadir
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
    // Abre la actividad para ver el evento, para poder eliminarlo o editarlo
    public void verEvento(Object item) {
        String evento = item.toString();
        String hora_inicio = evento.substring(0, 5);
        String hora_fin = evento.substring(6, 11);
        String titulo = evento.substring(13);
        ArrayList valores = new ArrayList<String>();
        valores.add(dameFecha());
        valores.add(hora_inicio);
        valores.add(hora_fin);
        valores.add(titulo);

        Intent intent = new Intent(this, VerEvento.class);
        intent.putStringArrayListExtra("valores", valores);
        startActivity(intent);
    }
    // Por si el usuario no cambia de fecha
    public void fecha_actual() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd");
        dia = df.format(c);
        df = new SimpleDateFormat("MM");
        mes = df.format(c);
        df = new SimpleDateFormat("YYYY");
        anio = df.format(c);
    }

    private void comprobarPermisos() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
    }

    @Override
    public void onResult(AIResponse result) {
        Result resultado = result.getResult();
        String action = resultado.getAction();
        // Si el usuario decide agrgar evento
        if (action.equals("QuestionAddEvent.QuestionAddEvent-yes")) {
            // No hay eventos
            if (db.eventoDao().getEventoFechayHora(fecha_a, hora_a).isEmpty()) {
                Evento ev_a = new Evento(fecha_a, titulo_a, hora_a, hora_b);
                db.eventoDao().aniadir(ev_a);
                myBot.speak("Evento añadido", TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else { // Hay eventos
                Evento ev_a = new Evento(fecha_a, titulo_a, hora_a, hora_b);
                db.eventoDao().aniadir(ev_a);
                myBot.speak("Se ha añadido el evento, pero ya había una cita en esa fecha. Puede cambiarlo si desea.", TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(action.equals("addevent-action")) {
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                fecha_a = "";
                titulo_a = "";
                hora_a = "";
                hora_b = "";
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("date")) {
                        fecha_a= entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("any")) {
                        titulo_a = entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("time")) {
                        if (!entry.getValue().toString().isEmpty()) {
                            hora_a = entry.getValue().toString();
                            hora_a = hora_a.substring(2, 7);
                        }
                    } else if (entry.getKey().equals("time2")) {
                        hora_b = entry.getValue().toString();
                        hora_b = hora_b.substring(1, 6);
                    }
                }
            }
            // Arreglamos formato de fecha
            String d = fecha_a.substring(8);
            String m = fecha_a.substring(5, 7);
            Integer mes_i = Integer.parseInt(m);
            mes_i-=1;

            if (mes_i < 10) {
                m = "0" + mes_i.toString();
            }
            else {
                m = mes_i.toString();
            }
            String a = fecha_a.substring(0, 4);
            fecha_a = d + "/" + m + "/" + a;

            if (hora_b.isEmpty() || hora_b == "") {
                String sDate1="31/12/1998"+hora_a;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm");
                Date dt = new Date();
                try {
                    dt = sdf.parse(sDate1);
                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }
                hora_b = sumarMinutos(dt);
            }

            String msg = "¿Desea agregar el evento " + titulo_a + " el día " + d + " de " + mes(m) + " a las " + hora_a + "?";

            if (!resultado.getFulfillment().getSpeech().isEmpty()){
                myBot.speak(resultado.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                myBot.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // El usuario quiere borrar
        else if (action.equals("delete-action")) {
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("date")) {
                        fecha_a= entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("any")) {
                        titulo_a = entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("time")) {
                        hora_a = entry.getValue().toString();
                        hora_a = hora_a.substring(1, 6);
                    }
                }
            }
            // Arreglamos formato de fecha
            String d = fecha_a.substring(8);
            String m = fecha_a.substring(5, 7);
            Integer mes_i = Integer.parseInt(m);
            mes_i-=1;

            if (mes_i < 10) {
                m = "0" + mes_i.toString();
            }
            else {
                m = mes_i.toString();
            }
            String a = fecha_a.substring(0, 4);
            fecha_a = d + "/" + m + "/" + a;

            myBot.speak(resultado.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
            if (resultado.getFulfillment().getSpeech().contains("desea")){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // Si el usuario confirma que quiere eliminar
        else if (action.equals("QuestionDeleteEvent.QuestionDeleteEvent-yes")) {

            if (db.eventoDao().getEventoFechaHoraTitulo(fecha_a, hora_a, titulo_a).isEmpty()) {
                myBot.speak("No se ha encontrado ningún evento con esas características.", TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                db.eventoDao().deleteByHora(fecha_a, hora_a, titulo_a);
                myBot.speak("Evento eliminado", TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // Si el usuario quiere editar
        else if (action.equals("editevent-action")) {
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("date")) {
                        fecha_a= entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("any")) {
                        titulo_a = entry.getValue().toString().replace("\"", "");
                    } else if (entry.getKey().equals("time")) {
                        hora_a = entry.getValue().toString();
                        hora_a = hora_a.substring(1, 6);
                    }
                }
            }
            // Si el usuario no dice mes, se pone el mes actual
            String d = fecha_a.substring(8);
            String m = fecha_a.substring(5, 7);
            Integer mes_i = Integer.parseInt(m);
            mes_i-=1;

            if (mes_i < 10) {
                m = "0" + mes_i.toString();
            }
            else {
                m = mes_i.toString();
            }
            String a = fecha_a.substring(0, 4);
            fecha_a = d + "/" + m + "/" + a;

            myBot.speak(resultado.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
            if (resultado.getFulfillment().getSpeech().contains("desea")){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // Cuando el usuario decide que quiere editar el título
        else if (action.equals("QuestionEditTitle.QuestionEditTitle-custom")) {
            String titulo_edit = "";
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("any")) {
                        titulo_edit = entry.getValue().toString().replace("\"", "");
                    }
                }
                if (db.eventoDao().getEventoFechaHoraTitulo(fecha_a, hora_a, titulo_a).isEmpty()) {
                    myBot.speak("No hay un evento con esas características.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    db.eventoDao().actualizarTitulo(titulo_edit, fecha_a, hora_a, titulo_a);
                    myBot.speak("Evento actualizado.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Elimina el contexto para poder empezar una conversación de nuevo
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            aiService.resetContexts();
        }
        // El usuario decide editar la hora de inicio
        else if (action.equals("QuestionEditStartHour.QuestionEditStartHour-custom")) {
            String hora_ini = "";
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("time")) {
                        hora_ini= entry.getValue().toString();
                        hora_ini = hora_ini.substring(1, 6);
                    }
                }

                if (db.eventoDao().getEventoFechaHoraTitulo(fecha_a, hora_a, titulo_a).isEmpty()) {
                    myBot.speak("No hay un evento con esas características.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    db.eventoDao().actualizarHoraIni(hora_ini, fecha_a, hora_a, titulo_a);
                    myBot.speak("Evento actualizado.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // El usuario decide editar la hora de fin
        else if (action.equals("QuestionEditEndHour.QuestionEditEndHour-custom")) {
            String hora_fin = "";
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("time")) {
                        hora_fin= entry.getValue().toString();
                        hora_fin = hora_fin.substring(1, 6);
                    }
                }

                if (db.eventoDao().getEventoFechaHoraTitulo(fecha_a, hora_a, titulo_a).isEmpty()) {
                    myBot.speak("No hay un evento con esas características.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    db.eventoDao().actualizarHoraFin(hora_fin, fecha_a, hora_a, titulo_a);
                    myBot.speak("Evento actualizado.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // El usuario decide editar la fecha
        else if (action.equals("QuestionEditDate.QuestionEditDate-custom")) {
            String fecha_act = "";
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("date")) {
                        fecha_act= entry.getValue().toString().replace("\"", "");
                    }
                }
                String d = fecha_act.substring(8);
                String m = fecha_act.substring(5, 7);
                Integer mes_i = Integer.parseInt(m);
                mes_i-=1;

                if (mes_i < 10) {
                    m = "0" + mes_i.toString();
                }
                else {
                    m = mes_i.toString();
                }
                String a = fecha_act.substring(0, 4);
                fecha_act = d + "/" + m + "/" + a;
                if (db.eventoDao().getEventoFechaHoraTitulo(fecha_a, hora_a, titulo_a).isEmpty()) {
                    myBot.speak("No hay un evento con esas características.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    db.eventoDao().actualizarFecha(fecha_act, fecha_a, hora_a, titulo_a);
                    myBot.speak("Evento actualizado.", TextToSpeech.QUEUE_FLUSH, null, null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Ver eventos
        else if (action.equals("ViewEvents-action")) {
            String fecha_v = "";
            String hora_v = "";
            if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
                //Coger los valores de los parametros
                for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()) {
                    if (entry.getKey().equals("date")) {
                        fecha_v = entry.getValue().toString().replace("\"", "");
                    }
                    else if (entry.getKey().equals("time")){
                        hora_v= entry.getValue().toString();
                        hora_v = hora_v.substring(1, 6);
                    }
                }

                String d = fecha_v.substring(8);
                String m = fecha_v.substring(5, 7);
                Integer mes_i = Integer.parseInt(m);
                mes_i-=1;

                if (mes_i < 10) {
                    m = "0" + mes_i.toString();
                }
                else {
                    m = mes_i.toString();
                }
                String a = fecha_v.substring(0, 4);
                fecha_v = d + "/" + m + "/" + a;

                if (hora_v.isEmpty()) {
                    List<Evento> events = db.eventoDao().getEventoFecha(fecha_v);
                    if (events.isEmpty()) {
                        myBot.speak("No tienes ningún evento", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    else {
                        for (Evento e : events) {
                            String ev = "De " + e.getHora_inicio() + "a " + e.getHora_fin() + "tienes " + e.getTitulo();
                            myBot.speak(ev, TextToSpeech.QUEUE_FLUSH, null, null);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    List<Evento> events = db.eventoDao().getEventoFechayHora(fecha_v, hora_v);
                    if (events.isEmpty()) {
                        myBot.speak("No tienes ningún evento", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    else {
                        for (Evento e : events) {
                            String ev = "De " + e.getHora_inicio() + "a " + e.getHora_fin() + "tienes " + e.getTitulo();
                            myBot.speak(ev, TextToSpeech.QUEUE_FLUSH, null, null);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        else if (action.equals("QuestionAddEvent.QuestionAddEvent-no") ||
                action.equals("QuestionDeleteEvent.QuestionDeleteEvent-no") ||
                action.equals("QuestionEditEvent.QuestionEditEvent-no") ||
                action.equals("Cancel") ||
                action.equals("QuestionEditEvent.QuestionEditEvent-yes") ||
                action.equals("EditTitle") ||
                action.equals("EditStartHour") ||
                action.equals("EditEndHour") ||
                action.equals("EditDate")) {
            myBot.speak(resultado.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    // Convertir mes en cadena a mes en número
    private String mes(String mes) {
        String mes_num = "";
        if (mes.equals("00")) {
            mes_num = "enero";
        }
        else if (mes.equals("01")) {
            mes_num = "febrero";
        }
        else if (mes.equals("02")) {
            mes_num = "marzo";
        }
        else if (mes.equals("03")) {
            mes_num = "abril";
        }
        else if (mes.equals("04")) {
            mes_num = "mayo";
        }
        else if (mes.equals("05")) {
            mes_num = "junio";
        }
        else if (mes.equals("06")) {
            mes_num = "julio";
        }
        else if (mes.equals("07")) {
            mes_num = "agosto";
        }
        else if (mes.equals("08")) {
            mes_num = "septiembre";
        }
        else if (mes.equals("9")) {
            mes_num = "octubre";
        }
        else if (mes.equals("10")) {
            mes_num = "noviembre";
        }
        else if (mes.equals("11")) {
            mes_num = "diciembre";
        }
        return mes_num;
    }

    // Si el usuario no dice la hora final, le sumo 15 minutos a la hora inicial
    private String sumarMinutos(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 15);

        return calendar.getTime().toString().substring(11, 16);
    }


}
