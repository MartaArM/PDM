package com.example.app3.Interfaces;


import androidx.room.*;

import com.example.app3.Entidad.Cita;

import java.util.List;

@Dao
public interface CitaDao {
    @Query("SELECT COUNT(*) FROM " + Cita.TABLE_NAME)
    int count(); //metodo

    //seleccionar todo
    @Query("SELECT * FROM "+ Cita.TABLE_NAME)
    List<Cita> getAllCitas();

    // seleccionar por fecha
    @Query("SELECT * FROM "+Cita.TABLE_NAME+" WHERE fecha" +" = :fecha")
    List<Cita> getCitaFecha(String fecha);

    // seleccionar por titulo
    @Query("SELECT * FROM "+Cita.TABLE_NAME+" WHERE titulo" +" = :titulo")
    List<Cita> getCitaTitulo(String titulo);

    // seleccionar por fecha y hora
    @Query("SELECT * FROM "+Cita.TABLE_NAME+" WHERE fecha" +" = :fecha" + " and hora_inicio" + " = :hora")
    List<Cita> getCitaFechayHora(String fecha, String hora);

    // seleccionar por fecha, hora y título
    @Query("SELECT * FROM "+Cita.TABLE_NAME+" WHERE fecha" +" = :fecha" + " and hora_inicio" + " = :hora" + " and titulo" + " = :titulo")
    List<Cita> getCitaFechaHoraTitulo(String fecha, String hora, String titulo);

    // seleccionar por fecha, hora y título
    @Query("SELECT * FROM "+Cita.TABLE_NAME+" WHERE _id" + " = :id")
    Cita getCitaId(long id);

    //insertar
    @Insert
    void aniadir(Cita... citas);

    //eliminar por id
    @Query("DELETE FROM " + Cita.TABLE_NAME + " WHERE " + Cita.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    //eliminar por fecha, hora y titulo
    @Query("DELETE FROM " + Cita.TABLE_NAME + " WHERE fecha" + " = :fecha" + " AND hora_inicio"+ " = :hora" + " AND titulo"+ " = :titulo")
    int deleteByHora(String fecha, String hora, String titulo);

    //eliminar todos
    @Query("DELETE FROM " + Cita.TABLE_NAME)
    int deleteAll();

    //actualizar
    @Update
    int updateEntidad(Cita obj);

    //actualizar cita
    @Query("UPDATE " + Cita.TABLE_NAME + " SET titulo" + " = :titulo" + ", fecha" + " = :fecha" +
            ", hora_inicio" + " = :hora_ini_n" +", hora_fin" + " = :hora_f" +", descripcion" + " = :descripcion"
            +", email" + " = :email"+ " WHERE _id" + " = :id")
    int actualizarCita(String titulo, String fecha, String hora_ini_n,  String hora_f, String descripcion,
                       String email, long id);

    // Actualizar solo título
    @Query("UPDATE " + Cita.TABLE_NAME + " SET titulo" + " = :titulo" + " WHERE _id" +
            " = :id")
    int actualizarTitulo(String titulo, long id);

    // Actualizar solo hora de inicio
    @Query("UPDATE " + Cita.TABLE_NAME + " SET hora_inicio" + " = :hora" + " WHERE _id" +
            " = :id")
    int actualizarHoraIni(String hora, long id);

    // Actualizar solo hora de fin
    @Query("UPDATE " + Cita.TABLE_NAME + " SET hora_fin" + " = :hora" + " WHERE _id" +
            " = :id")
    int actualizarHoraFin(String hora, long id);

    // Actualizar solo fecha
    @Query("UPDATE " + Cita.TABLE_NAME + " SET fecha" + " = :fecha" + " WHERE _id" +
            " = :id")
    int actualizarFecha(String fecha, long id);

    //insertar 2
    @Insert
    long insert(Cita usuarios);
}
