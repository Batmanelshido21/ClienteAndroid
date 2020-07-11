package com.example.clientestreaming;

public class CancionRespuesta {

    int id;
    String nombre;
    String genero;
    String duracion;
    String audio;

    public CancionRespuesta(int id, String nombre, String genero, String duracion, String audio) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.audio = audio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }


    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
