package com.example.app2.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app2.Entidad.Usuario;

import java.util.List;
@Dao
public interface UsuarioDao {
    @Query("SELECT COUNT(*) FROM " + Usuario.TABLE_NAME)
    int count(); //metodo

    //seleccionar todo
    @Query("SELECT * FROM "+Usuario.TABLE_NAME)
    List<Usuario> getAllUsuarios();

    // seleccionar por nombre de usuario
    @Query("SELECT * FROM "+Usuario.TABLE_NAME+" WHERE nombre_usuario" +" = :nombre_u")
    Usuario getUsuarioNombreUsuario(String nombre_u);

    //insertar
    @Insert
    void aniadir(Usuario ... Usuarios);

    //eliminar por id
    @Query("DELETE FROM " + Usuario.TABLE_NAME + " WHERE " + Usuario.COLUMN_ID + " = :ide")
    int deleteById(long ide);

    //eliminar por nombre de usuario
    @Query("DELETE FROM " + Usuario.TABLE_NAME + " WHERE nombre_usuario" + " = :nombre_u")
    int deleteByNombre(String nombre_u);

    //eliminar todos
    @Query("DELETE FROM " + Usuario.TABLE_NAME)
    int deleteAll();

    //actualizar
    @Update
    int updateEntidad(Usuario obj);

    //actualizar Usuario
    @Query("UPDATE " + Usuario.TABLE_NAME + " SET nombre_usuario" + " = :nombre_usuario" + ", clave" + " = :clave"
            + ", nombre" + " = :nombre" +", horario" + " = :horario" +
            " WHERE nombre_usuario" + " = :nombre_u")
    int actualizarUsuario(String nombre_usuario, String clave, String nombre, String horario,
                          String nombre_u);

    //insertar 2
    @Insert
    long insert(Usuario usuarios);
}
