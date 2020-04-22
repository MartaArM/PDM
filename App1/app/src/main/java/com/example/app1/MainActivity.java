package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app1.Database.AppDatabase;
import com.example.app1.Entidad.Evento;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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

    TextView prueba;
    private AIService aiService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accessToken = "198a751bfb7d4cdfbf4facae873d5186";
        final AIConfiguration config = new AIConfiguration(accessToken, AIConfiguration.SupportedLanguages.Spanish, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        // Comprobamos permisos para escuchar
        comprobarPermisos();

        calendario = findViewById(R.id.calendarView);
        lv = findViewById(R.id.lvEventos);
        button_bot = findViewById(R.id.ibbot);
        prueba = findViewById(R.id.prueba);

        your_array_list = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list);
        lv.setAdapter(arrayAdapter); // list view donde se van a ver los eventos

        fecha_actual(); // Fecha actual por si no cambio de dia
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

        mySpeech.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matchs = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchs != null) {
                    prueba.setText(matchs.get(0));
                    myBot.speak(matchs.get(0), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        button_bot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: // Cuando aprieto el botón
                        //mySpeech.startListening(speechintent);
                        aiService.startListening();
                        break;
                    case MotionEvent.ACTION_UP: //Cuando lo suelto
                        //mySpeech.stopListening();
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
            /*Intent intent_perm = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package: "+getPackageName()));
            startActivity(intent_perm);
            finish(); */
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
    }

    @Override
    public void onResult(AIResponse result) {
        Result resultado = result.getResult();

        String parametros = "";

        if (resultado.getParameters() != null && !resultado.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : resultado.getParameters().entrySet()){
                parametros += "(" + entry.getKey() + ", " + entry.getValue() + ")";
            }
        }

       // prueba.setText(resultado.getFulfillment().getSpeech());
        myBot.speak(resultado.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
       
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
}
