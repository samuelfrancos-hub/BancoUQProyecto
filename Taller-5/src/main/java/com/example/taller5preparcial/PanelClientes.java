package com.example.taller5preparcial;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PanelClientes extends VBox {

    private final ObservableList<Cliente> datosClientes;
    private TableView<Cliente> tablaClientes;
    private TextField txtDocumento;
    private TextField txtNombre;
    private TextField txtTelefono;
    private TextField txtCorreo;

    public PanelClientes(ObservableList<Cliente> datosClientes) {
        this.datosClientes = datosClientes;
        this.setPadding(new Insets(10));
        this.setSpacing(20);

        HBox topContainer = crearFormularioYBotones();
        tablaClientes = crearTablaClientes();
        conectarSeleccionTabla();

        VBox.setVgrow(tablaClientes, Priority.ALWAYS);

        this.getChildren().addAll(topContainer, tablaClientes);
    }

    private HBox crearFormularioYBotones() {
        HBox topContainer = new HBox(30);
        topContainer.setAlignment(Pos.TOP_LEFT);

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        txtDocumento = new TextField();
        txtNombre = new TextField();
        txtTelefono = new TextField();
        txtCorreo = new TextField();

        formulario.add(new Label("Documento:"), 0, 0);
        formulario.add(txtDocumento, 1, 0);
        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);
        formulario.add(new Label("# Teléfono:"), 0, 2);
        formulario.add(txtTelefono, 1, 2);
        formulario.add(new Label("Correo:"), 0, 3);
        formulario.add(txtCorreo, 1, 3);

        ColumnConstraints columnaCampo = new ColumnConstraints();
        columnaCampo.setHgrow(Priority.ALWAYS);
        formulario.getColumnConstraints().addAll(new ColumnConstraints(80), columnaCampo);

        VBox botonesAccion = new VBox(10);
        botonesAccion.setAlignment(Pos.TOP_CENTER);

        Button btnGuardar = new Button("Guardar");
        Button btnActualizar = new Button("Actualizar");
        Button btnBorrar = new Button("Borrar");

        btnGuardar.setPrefWidth(100);
        btnActualizar.setPrefWidth(100);
        btnBorrar.setPrefWidth(100);

        botonesAccion.getChildren().addAll(btnGuardar, btnActualizar, btnBorrar);

        btnGuardar.setOnAction(e -> guardarCliente());
        btnActualizar.setOnAction(e -> actualizarCliente());
        btnBorrar.setOnAction(e -> borrarCliente());

        topContainer.getChildren().addAll(formulario, botonesAccion);
        return topContainer;
    }

    private TableView<Cliente> crearTablaClientes() {
        TableView<Cliente> tabla = new TableView<>();
        tabla.setItems(datosClientes);

        TableColumn<Cliente, String> colDocumento = new TableColumn<>("Documento");
        colDocumento.setCellValueFactory(cellData -> cellData.getValue().documentoProperty());

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("# Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());

        TableColumn<Cliente, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());

        tabla.getColumns().addAll(colDocumento, colNombre, colTelefono, colCorreo);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tabla;
    }

    private void conectarSeleccionTabla() {
        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        txtDocumento.setText(newValue.getDocumento());
                        txtNombre.setText(newValue.getNombre());
                        txtTelefono.setText(newValue.getTelefono());
                        txtCorreo.setText(newValue.getCorreo());
                    } else {
                        limpiarCampos();
                    }
                }
        );
    }

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