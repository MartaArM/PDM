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

    @Query("SELECT * FROM "+Horas.TABLE_NAME+" WHERE _id" +" = :id")
    Horas getHorasId(long id);

    // Seleccionar por id, dia y hora
    @Query("SELECT * FROM "+Horas.TABLE_NAME+" WHERE id_usuario" +" = :id" + " AND hora" + " = :hora"
            + " AND dia" + " = :dia")
    Horas getHorasIdDiaHora(long id, String hora, String dia);

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

    @Query("UPDATE " + Horas.TABLE_NAME + " SET dia" + " = :dia" + ", hora" + " = :hora" +
            ", motivo_tarde" + " = :motivo" + ", accion" + " = :accion" + ", localizacion" + " = :localizacion"
            + " WHERE _id" + " = :id")
    int actualizarHoras(String dia, String hora, String motivo, String accion, String localizacion,
                        long id);

    //insertar 2
    @Insert
    long insert(Horas usuarios);
}
