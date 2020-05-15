package com.example.app2.Entidad;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "horario")
    private String horario;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }


    @Ignore
    public Usuario() {
    }

    public Usuario( String nombre_usuario, String clave, String nombre, String horario) {
        this.nombre_usuario = nombre_usuario;
        this.clave = clave;
        this.nombre = nombre;
        this.horario = horario;
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
