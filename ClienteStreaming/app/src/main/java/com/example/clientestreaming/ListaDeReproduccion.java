package com.example.clientestreaming;

public class ListaDeReproduccion {

    private String Nombre;
    private int Cuenta_id;

    public ListaDeReproduccion(String nombre, int cuenta_id) {
        this.Nombre = nombre;
        this.Cuenta_id = cuenta_id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public int getCuenta_id() {
        return Cuenta_id;
    }

    public void setCuenta_id(int cuenta_id) {
        this.Cuenta_id = cuenta_id;
    }
}
