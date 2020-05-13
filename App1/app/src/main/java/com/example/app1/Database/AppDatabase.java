package com.example.app1.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.app1.Entidad.Evento;
import com.example.app1.Interfaces.EventoDao;

@Database(entities = {Evento.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressWarnings("WeakerAccess")
    public abstract EventoDao eventoDao(); //que permisos va tener listar, eliminar, update, insertar
    private static AppDatabase sInstance; //variable.

    static final public Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };


}
