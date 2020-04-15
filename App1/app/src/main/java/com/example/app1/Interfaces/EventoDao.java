package com.example.app1.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app1.Entidad.Evento;

import java.util.List;
@Dao
public interface EventoDao {
    @Query("SELECT COUNT(*) FROM " + Evento.TABLE_NAME)
    int count(); //metodo

    //seleccionar todo
    @Query("SELECT * FROM "+Evento.TABLE_NAME)
    List<Evento> getAllEventos();


    //insertar
    @Insert
    void aniadir(Evento ... eventos);

    //eliminar por id
    @Query("DELETE FROM " + Evento.TABLE_NAME + " WHERE " + Evento.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    @Query("DELETE FROM " + Evento.TABLE_NAME + " WHERE hora_inicio" + " = :hora" + " AND fecha"+ " = :fecha")
    int deleteByHora(String hora, String fecha);

    //actualizar
    @Update
    int updateEntidad(Evento obj);
    
    //insertar 2
    @Insert
    long insert(Evento usuarios);
}
