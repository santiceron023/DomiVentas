package com.example.danni.domiventas;

/**
 * Created by danni on 26/11/2017.
 */

public class NuevaPromo {
        private String foto, marca, nombre,precio_ahora,precio_antes,tamaño;

    public NuevaPromo() {
    }

    public NuevaPromo(String foto, String marca, String nombre, String precio_ahora, String precio_antes, String tamaño) {
        this.foto = foto;
        this.marca = marca;
        this.nombre = nombre;
        this.precio_ahora = precio_ahora;
        this.precio_antes = precio_antes;
        this.tamaño = tamaño;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio_ahora() {
        return precio_ahora;
    }

    public void setPrecio_ahora(String precio_ahora) {
        this.precio_ahora = precio_ahora;
    }

    public String getPrecio_antes() {
        return precio_antes;
    }

    public void setPrecio_antes(String precio_antes) {
        this.precio_antes = precio_antes;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }
}
