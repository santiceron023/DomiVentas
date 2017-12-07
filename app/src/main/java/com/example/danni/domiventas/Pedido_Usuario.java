package com.example.danni.domiventas;

/**
 * Created by danni on 03/12/2017.
 */

public class Pedido_Usuario {
    private String Nombre;
    private String Cantidad;
    private String Total;
    private String Pedido;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public Pedido_Usuario() {

    }

    public String getPedido() {
        return Pedido;
    }

    public void setPedido(String pedido) {
        Pedido = pedido;
    }

    public Pedido_Usuario(String nombre, String cantidad, String total, String pedido) {

        Nombre = nombre;
        Cantidad = cantidad;
        Total = total;
        Pedido = pedido;
    }
}
