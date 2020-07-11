package com.example.clientestreaming;

public class Cancion {

    private int id;
    private String nombre;
    private String genero;
    private String duracion;
    private String audio;
    private int ListaDeReproduccionId;
    private int idAlbum;

    public Cancion(int id, String nombre, String genero, String duracion, String audio, int ListaDeReproduccionId, int idAlbum) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.audio = audio;
        this.ListaDeReproduccionId = ListaDeReproduccionId;
        this.idAlbum = idAlbum;
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

    public int getListaDeReproduccion() {
        return ListaDeReproduccionId;
    }

    public void setListaDeReproduccion(int listaDeReproduccion) {
        this.ListaDeReproduccionId = listaDeReproduccion;
    }

    public int getAlbum_id() {
        return idAlbum;
    }

    public void setAlbum_id(int album_id) {
        idAlbum = album_id;
    }
}
