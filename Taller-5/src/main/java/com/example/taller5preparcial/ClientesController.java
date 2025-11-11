package com.example.taller5preparcial;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ClientesController {

    @FXML private VBox clientesPanel;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colDocumento;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCorreo;

    private ObservableList<Cliente> datosClientes;

    @FXML
    public void initialize() {
        colDocumento.setCellValueFactory(cellData -> cellData.getValue().documentoProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());

        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesCliente(newValue)
        );
    }

    public void initData(ObservableList<Cliente> datos) {
        this.datosClientes = datos;
        tablaClientes.setItems(datosClientes);
    }

    private void mostrarDetallesCliente(Cliente cliente) {
        if (cliente != null) {
            txtDocumento.setText(cliente.getDocumento());
            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtCorreo.setText(cliente.getCorreo());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void guardarCliente() {
        if (!txtDocumento.getText().isEmpty() && !txtNombre.getText().isEmpty()) {
            Cliente nuevoCliente = new Cliente(
                    txtDocumento.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText()
            );
            datosClientes.add(nuevoCliente);
            limpiarCampos();
        }
    }

    @FXML
    private void actualizarCliente() {
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            clienteSeleccionado.setDocumento(txtDocumento.getText());
            clienteSeleccionado.setNombre(txtNombre.getText());
            clienteSeleccionado.setTelefono(txtTelefono.getText());
            clienteSeleccionado.setCorreo(txtCorreo.getText());

            tablaClientes.refresh();
            limpiarCampos();
        }
    }

    @FXML
    private void borrarCliente() {
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            datosClientes.remove(clienteSeleccionado);
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtDocumento.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
    }
}