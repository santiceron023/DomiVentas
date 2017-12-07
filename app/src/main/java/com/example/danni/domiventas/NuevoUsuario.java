package com.example.danni.domiventas;

/**
 * Created by danni on 05/11/2017.
 */

public class NuevoUsuario {

    private String email, nombre, propietario, direccion,foto;
    private String celular, tipo, costoEnvio, pedidoMin, tiempoEnvio;

    public NuevoUsuario() {
    }

    public NuevoUsuario(String email, String nombre, String propietario, String direccion, String foto, String celular, String tipo, String costoEnvio, String pedidoMin, String tiempoEnvio) {
        this.email = email;
        this.nombre = nombre;
        this.propietario = propietario;
        this.direccion = direccion;
        this.foto = foto;
        this.celular = celular;
        this.tipo = tipo;
        this.costoEnvio = costoEnvio;
        this.pedidoMin = pedidoMin;
        this.tiempoEnvio = tiempoEnvio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getPedidoMin() {
        return pedidoMin;
    }

    public void setPedidoMin(String pedidoMin) {
        this.pedidoMin = pedidoMin;
    }

    public String getTiempoEnvio() {
        return tiempoEnvio;
    }

    public void setTiempoEnvio(String tiempoEnvio) {
        this.tiempoEnvio = tiempoEnvio;
    }
}
