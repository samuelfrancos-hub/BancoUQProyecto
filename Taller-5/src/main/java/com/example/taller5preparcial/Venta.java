package com.example.taller5preparcial;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Venta {
    private final ObjectProperty<LocalDate> fecha;
    private final StringProperty clienteNombre;
    private final StringProperty productoNombre;
    private final IntegerProperty cantidad;
    private final DoubleProperty precioUnitario;
    private final DoubleProperty subtotal;

    public Venta(LocalDate fecha, String clienteNombre, String productoNombre, int cantidad, double precioUnitario) {
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.clienteNombre = new SimpleStringProperty(clienteNombre);
        this.productoNombre = new SimpleStringProperty(productoNombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
        this.subtotal = new SimpleDoubleProperty(cantidad * precioUnitario);
    }

    public ObjectProperty<LocalDate> fechaProperty() { return fecha; }
    public StringProperty clienteNombreProperty() { return clienteNombre; }
    public StringProperty productoNombreProperty() { return productoNombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioUnitarioProperty() { return precioUnitario; }
    public DoubleProperty subtotalProperty() { return subtotal; }

    public LocalDate getFecha() { return fecha.get(); }
}