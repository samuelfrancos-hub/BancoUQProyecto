package com.example.taller5preparcial;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Producto {
    private final StringProperty nombre;
    private final DoubleProperty precio;

    public Producto(String nombre, double precio) {
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleDoubleProperty(precio);
    }
    public StringProperty nombreProperty() {
        return nombre;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public String getNombre() {
        return nombre.get();
    }

    public double getPrecio() {
        return precio.get();
    }
}
