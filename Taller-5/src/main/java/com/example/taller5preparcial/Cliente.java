package com.example.taller5preparcial;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {
    private final StringProperty documento;
    private final StringProperty nombre;
    private final StringProperty telefono;
    private final StringProperty correo;

    public Cliente(String documento, String nombre, String telefono, String correo) {
        this.documento = new SimpleStringProperty(documento);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.correo = new SimpleStringProperty(correo);
    }

    public StringProperty documentoProperty() { return documento; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty correoProperty() { return correo; }

    public void setDocumento(String documento) { this.documento.set(documento); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
    public void setCorreo(String correo) { this.correo.set(correo); }

    public String getDocumento() { return documento.get(); }
    public String getNombre() { return nombre.get(); }
    public String getTelefono() { return telefono.get(); }
    public String getCorreo() { return correo.get(); }
}