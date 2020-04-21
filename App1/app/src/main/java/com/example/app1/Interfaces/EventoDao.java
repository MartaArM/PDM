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

    // seleccionar por fecha
    @Query("SELECT * FROM "+Evento.TABLE_NAME+" WHERE fecha" +" = :fecha")
    List<Evento> getEventoFecha(String fecha);

    // seleccionar por titulo
    @Query("SELECT * FROM "+Evento.TABLE_NAME+" WHERE titulo" +" = :titulo")
    List<Evento> getEventoTitulo(String titulo);

    //insertar
    @Insert
    void aniadir(Evento ... eventos);

    //eliminar por id
    @Query("DELETE FROM " + Evento.TABLE_NAME + " WHERE " + Evento.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    //eliminar por fecha y hora
    @Query("DELETE FROM " + Evento.TABLE_NAME + " WHERE fecha" + " = :fecha" + " AND hora_inicio"+ " = :hora")
    int deleteByHora(String fecha, String hora);

    //eliminar todos
    @Query("DELETE FROM " + Evento.TABLE_NAME)
    int deleteAll();

    //actualizar
    @Update
    int updateEntidad(Evento obj);

    //actualizar evento
    @Query("UPDATE " + Evento.TABLE_NAME + " SET titulo" + " = :titulo" + ", hora_inicio" + " = :hora_ini_n" +
            ", hora_fin" + " = :hora_f" + " WHERE fecha" + " = :fecha" + " AND hora_inicio" + " = :hora_ini")
    int actualizarEvento(String titulo, String hora_ini_n, String hora_f, String fecha, String hora_ini);

    //insertar 2
    @Insert
    long insert(Evento usuarios);
}
