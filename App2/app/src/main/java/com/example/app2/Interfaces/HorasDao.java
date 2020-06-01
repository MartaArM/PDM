package com.example.app2.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app2.Entidad.Horas;

import java.util.List;
@Dao
public interface HorasDao {
    @Query("SELECT COUNT(*) FROM " + Horas.TABLE_NAME)
    int count(); //metodo

    //seleccionar todo
    @Query("SELECT * FROM "+Horas.TABLE_NAME)
    List<Horas> getAllHorass();

    // seleccionar por id de usuario
    @Query("SELECT * FROM "+Horas.TABLE_NAME+" WHERE id_usuario" +" = :id")
    List<Horas> getHoras(long id);

    // Seleccionar por día
    @Query("SELECT * FROM "+Horas.TABLE_NAME+" WHERE dia" +" = :dia")
    List<Horas> getHorasDia(String dia);

    // Seleccionar por id, dia y hora
    @Query("SELECT * FROM "+Horas.TABLE_NAME+" WHERE dia" +" = :dia" + " AND hora" + " = :hora"
            + " AND id_usuario" + " = :id")
    List<Horas> getHorasIdDiaHora(String id, String hora, String dia);

    //insertar
    @Insert
    void aniadir(Horas ... Horass);

    //eliminar por id
    @Query("DELETE FROM " + Horas.TABLE_NAME + " WHERE " + Horas.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    //eliminar por id de usuario
    @Query("DELETE FROM " + Horas.TABLE_NAME + " WHERE id_usuario" + " = :id")
    int deleteByIdUser(String id);

    //eliminar todos
    @Query("DELETE FROM " + Horas.TABLE_NAME)
    int deleteAll();

    //actualizar
    @Update
    int updateEntidad(Horas obj);

    //actualizar Horas
    @Query("UPDATE " + Horas.TABLE_NAME + " SET dia" + " = :dia" + ", hora" + " = :hora"
            + " WHERE id_usuario" + " = :id_usuario")
    int actualizarHoras(String dia, String hora, String id_usuario);

    //insertar 2
    @Insert
    long insert(Horas usuarios);
}
