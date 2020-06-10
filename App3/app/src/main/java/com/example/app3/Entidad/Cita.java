package com.example.app3.Entidad;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Cita.TABLE_NAME)
public class Cita {
    public static final String TABLE_NAME = "cita";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "hora_inicio")
    private String hora_inicio;

    @ColumnInfo(name = "hora_fin")
    private String hora_fin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    @Ignore
    public Cita() {
    }

    public Cita(String fecha, String titulo, String hora_inicio, String hora_fin) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public static Cita fromContentValues(ContentValues values) {
        final Cita obj = new Cita();
        if (values.containsKey(COLUMN_ID)) {
            obj.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            obj.setId(1);
        }
        return obj;
    }
}
