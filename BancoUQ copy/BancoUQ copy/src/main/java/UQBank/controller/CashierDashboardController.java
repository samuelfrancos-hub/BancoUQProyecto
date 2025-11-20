package UQBank.controller;

import UQBank.model.service.BankService;
import UQBank.model.users.Client;
import UQBank.model.users.User;
import UQBank.model.accounts.BankAccount;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TextField searchClientField;
    @FXML private Label clientNameLabel;
    @FXML private Label clientIdLabel;
    @FXML private Label clientAccountsCountLabel;
    @FXML private Label clientSelectedLabel;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private TextField accountNumberField;
    @FXML private TextField transactionAmountField;

    @FXML private TextField regFullNameField;
    @FXML private TextField regEmailField;
    @FXML private TextField regPasswordField;
    @FXML private TextField regInitialDepositField;
    @FXML private ComboBox<String> regAccountTypeComboBox;

    private final BankService bankService = BankService.getInstance();
    private Client currentClient = null;

    @FXML
    public void initialize() {
        User user = bankService.getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Cajero: " + user.getFullName());
        }

        ObservableList<String> accountTypes = FXCollections.observableArrayList(
                "Ahorros",
                "Corriente",
                "Negocios"
        );

        accountTypeComboBox.setItems(accountTypes);
        accountTypeComboBox.getSelectionModel().selectFirst();

        regAccountTypeComboBox.setItems(accountTypes);
        regAccountTypeComboBox.getSelectionModel().selectFirst();

        clientSelectedLabel.setText("Datos del Cliente Seleccionado: Ninguno");
    }

    @FXML
    private void handleSearchClient() {
        String search = searchClientField.getText();
        if (search.isEmpty()) {
            showAlert("Búsqueda", "Por favor, introduce el ID o Email del cliente.");
            return;
        }

        User foundUser = bankService.findUserByEmail(search);
        if (foundUser == null) {
            foundUser = bankService.findUserById(search);
        }

        if (foundUser instanceof Client) {
            this.currentClient = (Client) foundUser;

            clientNameLabel.setText(currentClient.getFullName());
            clientIdLabel.setText(currentClient.getUserId());
            clientAccountsCountLabel.setText(String.valueOf(currentClient.getAccounts().size()));
            clientSelectedLabel.setText("Cliente Seleccionado: " + currentClient.getFullName() + " (" + currentClient.getUserId() + ")");

            showAlert("Éxito", "Cliente encontrado y seleccionado.");
        } else {
            this.currentClient = null;
            clientNameLabel.setText("-");
            clientIdLabel.setText("-");
            clientAccountsCountLabel.setText("-");
            clientSelectedLabel.setText("Datos del Cliente Seleccionado: Ninguno");
            showAlert("Error", "No se encontró un cliente con ese ID/Email.");
        }
    }

    @FXML
    private void handleRegisterNewClient() {
        String fullName = regFullNameField.getText();
        String email = regEmailField.getText();
        String password = regPasswordField.getText();
        String depositText = regInitialDepositField.getText();
        String accountType = regAccountTypeComboBox.getValue();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || depositText.isEmpty() || accountType == null) {
            showAlert("Error de Registro", "Por favor, completa todos los campos.");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(depositText);
            if (initialDeposit <= 0) {
                showAlert("Error de Registro", "El depósito inicial debe ser positivo.");
                return;
            }

            String newUserId = bankService.generateNextId("CLIENT");
            Client newClient = new Client(newUserId, fullName, password, email);

            bankService.addUser(newClient);

            String newAccountNumber = bankService.openAccount(newClient, accountType, initialDeposit);

            if (newAccountNumber != null) {
                showAlert("Registro Exitoso",
                        "Nuevo Cliente Creado:\n" +
                                "ID: " + newUserId + "\n" +
                                "Email: " + email + "\n" +
                                "Cuenta Inicial (" + accountType + "): " + newAccountNumber);

                regFullNameField.clear();
                regEmailField.clear();
                regPasswordField.clear();
                regInitialDepositField.clear();
            } else {
                showAlert("Fallo", "Cliente creado, pero no se pudo abrir la cuenta inicial.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El depósito inicial debe ser un número válido.");
        }
    }

    @FXML
    private void handleOpenNewAccount() {
        if (currentClient == null) {
            showAlert("Error", "Debes buscar y seleccionar un cliente primero.");
            return;
        }

        String type = accountTypeComboBox.getValue();
        String depositText = initialDepositField.getText();

        if (type == null || depositText.isEmpty()) {
            showAlert("Error", "Selecciona el tipo de cuenta e ingresa el depósito inicial.");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(depositText);
            if (initialDeposit <= 0) {
                showAlert("Error", "El depósito inicial debe ser un valor positivo.");
                return;
            }

            String newAccountNumber = bankService.openAccount(currentClient, type, initialDeposit);

            if (newAccountNumber != null) {
                showAlert("Éxito", "Cuenta de tipo " + type + " abierta exitosamente para " + currentClient.getFullName() +
                        "\nNúmero de Cuenta: " + newAccountNumber);
                clientAccountsCountLabel.setText(String.valueOf(currentClient.getAccounts().size()));
                initialDepositField.clear();
            } else {
                showAlert("Fallo", "No se pudo abrir la cuenta.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El depósito inicial debe ser un número válido.");
        }
    }

    @FXML
    private void handleDeposit() {
        String accNumber = accountNumberField.getText();
        String amountText = transactionAmountField.getText();

        if (accNumber.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Ingresa el número de cuenta y el monto para depositar.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            BankAccount account = bankService.findAccountByNumber(accNumber);

            if (account != null && account.deposit(amount)) {
                showAlert("Éxito", String.format("Depósito de $%.2f realizado en la cuenta %s.", amount, accNumber));
                transactionAmountField.clear();
            } else {
                showAlert("Fallo", "El depósito falló. Verifica el número de cuenta o el monto.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El monto debe ser un número válido.");
        }
    }

    @FXML
    private void handleWithdraw() {
        String accNumber = accountNumberField.getText();
        String amountText = transactionAmountField.getText();

        if (accNumber.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Ingresa el número de cuenta y el monto para retirar.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            BankAccount account = bankService.findAccountByNumber(accNumber);

            if (account != null && account.withdraw(amount)) {
                showAlert("Éxito", String.format("Retiro de $%.2f realizado en la cuenta %s.", amount, accNumber));
                transactionAmountField.clear();
            } else {
                showAlert("Fallo", "El retiro falló. Fondos insuficientes o restricciones de la cuenta.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El monto debe ser un número válido.");
        }
    }

    @FXML
    private void handleLogout() {
        bankService.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UQBank/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("UQ BANK - Acceso");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error de Navegación", "No se pudo cargar la vista de Login.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}