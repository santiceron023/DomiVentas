package com.example.danni.domiventas;

/**
 * Created by danni on 05/12/2017.
 */

public class UsuarioCliente {
    private String nombre, celular, correo, ubicacion,foto;

    public UsuarioCliente() {
    }

    public UsuarioCliente(String nombre, String celular, String correo, String ubicacion, String foto) {
        this.nombre = nombre;
        this.celular = celular;
        this.correo = correo;
        this.ubicacion = ubicacion;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
