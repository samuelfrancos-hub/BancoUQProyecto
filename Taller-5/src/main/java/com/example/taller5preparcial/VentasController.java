package com.example.taller5preparcial;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class VentasController {

    @FXML private ComboBox<String> cmbCliente;

    @FXML private ComboBox<String> cmbProducto;

    @FXML private TextField txtCantidad;

    @FXML private Label lblFecha;

    @FXML private Label lblPrecioUnitario;

    @FXML private Label lblSubtotal;

    @FXML private TableView<Venta> tablaVentas;

    @FXML private TableColumn<Venta, LocalDate> colFecha;

    @FXML private TableColumn<Venta, String> colCliente;

    @FXML private TableColumn<Venta, String> colProducto;

    @FXML private TableColumn<Venta, Number> colCantidad;

    @FXML private TableColumn<Venta, Number> colPrecioUnitario;

    @FXML private TableColumn<Venta, Number> colSubtotal;

    private ObservableList<Cliente> datosClientes;

    private ObservableList<Producto> datosProductos;

    private ObservableList<Venta> datosVentas;

    private double precioActual = 0.0;

    @FXML
    public void initialize() {

        lblFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        colFecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

        colCliente.setCellValueFactory(cellData -> cellData.getValue().clienteNombreProperty());

        colProducto.setCellValueFactory(cellData -> cellData.getValue().productoNombreProperty());

        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());

        colPrecioUnitario.setCellValueFactory(cellData -> cellData.getValue().precioUnitarioProperty());

        colSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty());

        colPrecioUnitario.setCellFactory(this::createNumericCellFactory);

        colSubtotal.setCellFactory(this::createNumericCellFactory);

        tablaVentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        cmbProducto.valueProperty().addListener((obs, oldVal, newVal) -> actualizarDetalles());
        txtCantidad.textProperty().addListener((obs, oldVal, newVal) -> actualizarDetalles());
    }

    private TableCell<Venta, Number> createNumericCellFactory(TableColumn<Venta, Number> column) {
        return new TableCell<Venta, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    double valor = item.doubleValue();
                    if (valor == Math.floor(valor) && valor < 1_000_000) {
                        setText(String.format("%.0f", valor));
                    } else {
                        setText(String.format("%,.2f", valor));
                    }
                }
            }
        };
    }

    public void initData(ObservableList<Cliente> clientes, ObservableList<Producto> productos, ObservableList<Venta> ventas) {
        this.datosClientes = clientes;
        this.datosProductos = productos;
        this.datosVentas = ventas;

        cmbCliente.setItems(
                datosClientes.stream()
                        .map(Cliente::getNombre)
                        .collect(Collectors.toCollection(javafx.collections.FXCollections::observableArrayList))
        );

        cmbProducto.setItems(
                datosProductos.stream()
                        .map(Producto::getNombre)
                        .collect(Collectors.toCollection(javafx.collections.FXCollections::observableArrayList))
        );

        tablaVentas.setItems(datosVentas);
    }

    private void actualizarDetalles() {

        String productoSeleccionado = cmbProducto.getValue();

        String cantidadTexto = txtCantidad.getText();

        Optional<Producto> productoOpt = datosProductos.stream()
                .filter(p -> p.getNombre().equals(productoSeleccionado))
                .findFirst();

        if (productoOpt.isPresent()) {
            precioActual = productoOpt.get().getPrecio();
            lblPrecioUnitario.setText(String.format("%,.2f", precioActual));
        } else {
            precioActual = 0.0;
            lblPrecioUnitario.setText("0.00");
        }
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad < 0) cantidad = 0;
        } catch (NumberFormatException e) {
        }

        double subtotal = precioActual * cantidad;
        lblSubtotal.setText(String.format("%,.2f", subtotal));
    }

    @FXML
    private void guardarVenta() {
        String cliente = cmbCliente.getValue();
        String producto = cmbProducto.getValue();
        int cantidad = 0;

        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            System.err.println("Error: Cantidad no válida.");
            return;
        }

        if (cliente != null && producto != null && cantidad > 0) {
            double precioUnitario = datosProductos.stream()
                    .filter(p -> p.getNombre().equals(producto))
                    .mapToDouble(Producto::getPrecio)
                    .findFirst()
                    .orElse(0.0);

            Venta nuevaVenta = new Venta(
                    LocalDate.now(),
                    cliente,
                    producto,
                    cantidad,
                    precioUnitario
            );

            datosVentas.add(nuevaVenta);
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        cmbCliente.getSelectionModel().clearSelection();
        cmbProducto.getSelectionModel().clearSelection();
        txtCantidad.clear();
        lblPrecioUnitario.setText("0.00");
        lblSubtotal.setText("0.00");
    }
}