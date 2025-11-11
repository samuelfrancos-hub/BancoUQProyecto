package com.example.taller5preparcial;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ProductosController {

    @FXML private VBox productosPanel;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Number> colPrecio; // Usar Number para DoubleProperty

    private ObservableList<Producto> datosProductos;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty());
        colPrecio.setCellFactory(tc -> new TableCell<Producto, Number>() {
            @Override
            protected void updateItem(Number precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    double valor = precio.doubleValue();
                    if (valor == Math.floor(valor)) {
                        setText(String.format("%.0f", valor));
                    } else {
                        setText(String.valueOf(valor));
                    }
                }
            }
        });

        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesProducto(newValue)
        );
    }

    public void initData(ObservableList<Producto> datos) {
        this.datosProductos = datos;
        tablaProductos.setItems(datosProductos);
    }

    private void mostrarDetallesProducto(Producto producto) {
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());

            if (!nombre.isEmpty()) {
                Producto nuevoProducto = new Producto(nombre, precio);
                datosProductos.add(nuevoProducto);
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El precio debe ser un número válido.");
        }
    }

    @FXML
    private void actualizarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            try {
                productoSeleccionado.setNombre(txtNombre.getText());
                productoSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));

                tablaProductos.refresh();
                limpiarCampos();
            } catch (NumberFormatException e) {
                System.err.println("Error: El precio debe ser un número válido para actualizar.");
            }
        }
    }

    @FXML
    private void borrarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            datosProductos.remove(productoSeleccionado);
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtPrecio.clear();
    }
}