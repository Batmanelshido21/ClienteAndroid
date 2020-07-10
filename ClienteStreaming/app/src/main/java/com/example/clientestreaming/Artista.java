package com.example.clientestreaming;

public class Artista {
    private Integer id;
    private String nombreArtistico;
    private String descripcion;
    private String imagen;

    public Artista(Integer id, String nombreArtistico, String descripcion, String imagen) {
        this.id = id;
        this.nombreArtistico = nombreArtistico;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreArtistico() {
        return nombreArtistico;
    }

    public void setNombreArtistico(String nombreArtistico) {
        this.nombreArtistico = nombreArtistico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
