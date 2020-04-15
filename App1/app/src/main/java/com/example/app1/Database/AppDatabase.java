package com.example.app1.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.app1.Entidad.Evento;
import com.example.app1.Interfaces.EventoDao;

@Database(entities = {Evento.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressWarnings("WeakerAccess")
    public abstract EventoDao eventoDao(); //que permisos va tener listar, eliminar, update, insertar
    private static AppDatabase sInstance; //variable.
}
