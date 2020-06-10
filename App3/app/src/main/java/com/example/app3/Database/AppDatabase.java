package com.example.app3.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.app3.Entidad.Cita;
import com.example.app3.Entidad.Notificacion;
import com.example.app3.Interfaces.*;

@Database(entities = {Cita.class, Notificacion.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressWarnings("WeakerAccess")
    public abstract CitaDao citaDao(); //que permisos va tener listar, eliminar, update, insertar
    public abstract NotificacionDao notificacionDao();
    private static AppDatabase sInstance; //variable.

}
