package com.example.app2.Entidad;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Horas.TABLE_NAME)
public class Horas {
    public static final String TABLE_NAME = "Horas";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = "id_usuario")
    private String id_usuario;

    @ColumnInfo(name = "dia")
    private String dia;

    @ColumnInfo(name = "hora")
    private String hora;

    @ColumnInfo(name = "motivo_tarde")
    private String motivo_tarde;

    @ColumnInfo(name = "accion")
    private String accion;

    @ColumnInfo(name = "localizacion")
    private String localizacion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo_tarde() {
        return motivo_tarde;
    }

    public void setMotivo_tarde(String motivo_tarde) {
        this.motivo_tarde = motivo_tarde;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Horas() {
    }

    public Horas( String id_usuario, String dia, String hora,
                  String motivo_tarde, String accion, String localizacion) {
        this.id_usuario = id_usuario;
        this.dia = dia;
        this.hora = hora;
        this.motivo_tarde = motivo_tarde;
        this.accion = accion;
        this.localizacion = localizacion;
    }

    public static Horas fromContentValues(ContentValues values) {
        final Horas obj = new Horas();
        if (values.containsKey(COLUMN_ID)) {
            obj.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            obj.setId(1);
        }
        return obj;
    }
}
