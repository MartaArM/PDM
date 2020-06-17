package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.app3.Database.AppDatabase;

public class Administracion extends AppCompatActivity {

    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);

        Intent i = getIntent();
        user = i.getStringExtra("user");
    }

    public void ver_notificaciones(View v) {
        Intent intent = new Intent(this, VerNotificaciones.class);
        startActivity(intent);
    }

    public void ver_calendario(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}
