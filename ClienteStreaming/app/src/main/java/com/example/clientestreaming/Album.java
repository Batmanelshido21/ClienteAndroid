package com.example.clientestreaming;

public class Album {

    private Integer id;
    private String nombre;
    private String fecha;
    private String descripcion;
    private String imagen;
    private Integer ArtistaId;

    public Album(Integer id, String nombre, String fecha, String descripcion, String imagen, Integer artistaId) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.ArtistaId = artistaId;
    }

    public Integer getArtistaId() {
        return ArtistaId;
    }

    public void setArtistaId(Integer artistaId) {
        this.ArtistaId = artistaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
