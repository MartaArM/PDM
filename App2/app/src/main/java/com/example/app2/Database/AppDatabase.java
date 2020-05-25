package com.example.app2.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.app2.Entidad.Usuario;
import com.example.app2.Interfaces.HorasDao;
import com.example.app2.Interfaces.UsuarioDao;
import com.example.app2.Entidad.Horas;

@Database(entities = {Usuario.class, Horas.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressWarnings("WeakerAccess")
    public abstract UsuarioDao usuarioDao(); //que permisos va tener listar, eliminar, update, insertar
    public abstract HorasDao horasDao();
    private static AppDatabase sInstance; //variable.


}
