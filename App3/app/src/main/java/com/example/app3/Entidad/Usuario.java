package com.example.app3.Entidad;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Usuario.TABLE_NAME)
public class Usuario {
    public static final String TABLE_NAME = "Usuario";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = "nombre_usuario")
    private String nombre_usuario;

    @ColumnInfo(name = "clave")
    private String clave;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }


    public Usuario() {
    }

    public Usuario(String nombre_usuario, String clave) {
        this.nombre_usuario = nombre_usuario;
        this.clave = clave;
    }

    public static Usuario fromContentValues(ContentValues values) {
        final Usuario obj = new Usuario();
        if (values.containsKey(COLUMN_ID)) {
            obj.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            obj.setId(1);
        }
        return obj;
    }
}
