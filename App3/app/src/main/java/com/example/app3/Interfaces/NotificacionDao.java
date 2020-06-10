package com.example.app3.Interfaces;


import androidx.room.*;

import com.example.app3.Entidad.Notificacion;

import java.util.List;

@Dao
public interface NotificacionDao {
    @Query("SELECT COUNT(*) FROM " + Notificacion.TABLE_NAME)
    int count(); //metodo

    //seleccionar todo
    @Query("SELECT * FROM "+ Notificacion.TABLE_NAME)
    List<Notificacion> getAllNotificacions();

    // seleccionar por fecha
    @Query("SELECT * FROM "+Notificacion.TABLE_NAME+" WHERE fecha" +" = :fecha")
    List<Notificacion> getNotificacionFecha(String fecha);

    // seleccionar por fecha, hora y título
    @Query("SELECT * FROM "+Notificacion.TABLE_NAME+" WHERE _id" + " = :id")
    Notificacion getNotificacionId(long id);

    //insertar
    @Insert
    void aniadir(Notificacion... citas);

    //eliminar por id
    @Query("DELETE FROM " + Notificacion.TABLE_NAME + " WHERE " + Notificacion.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    //eliminar todos
    @Query("DELETE FROM " + Notificacion.TABLE_NAME)
    int deleteAll();

    //actualizar
    @Update
    int updateEntidad(Notificacion obj);

    //insertar 2
    @Insert
    long insert(Notificacion usuarios);
}
