package UQBank.controller;

import UQBank.model.service.BankService;
import UQBank.model.users.Client;
import UQBank.model.users.User;
import UQBank.model.accounts.BankAccount;
import UQBank.model.accounts.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private ComboBox<String> accountSelectionComboBox;
    @FXML private TextArea transactionDetailsTextArea;
    @FXML private Label clientNameLabel;
    @FXML private Label clientAccountsCountLabel;

    @FXML private ComboBox<String> sourceAccountComboBox;
    @FXML private TextField targetAccountNumberField;
    @FXML private TextField transferAmountField;

    private final BankService bankService = BankService.getInstance();
    private Client currentClient;

    @FXML
    public void initialize() {
        User user = bankService.getCurrentUser();
        if (user instanceof Client) {
            this.currentClient = (Client) user;

            welcomeLabel.setText("Bienvenido, " + currentClient.getFullName());
            clientNameLabel.setText(currentClient.getFullName());
            clientAccountsCountLabel.setText(String.valueOf(currentClient.getAccounts().size()));

            List<String> accountNumbers = currentClient.getAccounts().stream()
                    .map(BankAccount::getAccountNumber)
                    .collect(Collectors.toList());

            ObservableList<String> accounts = FXCollections.observableArrayList(accountNumbers);

            accountSelectionComboBox.setItems(accounts);

            if (sourceAccountComboBox != null) {
                sourceAccountComboBox.setItems(accounts);
                if (!accounts.isEmpty()) {
                    sourceAccountComboBox.getSelectionModel().selectFirst();
                }
            }

        } else {
            handleLogout();
        }
    }

    @FXML
    private void handleTransfer() {
        String sourceAccNumber = sourceAccountComboBox.getValue();
        String targetAccNumber = targetAccountNumberField.getText();
        String amountText = transferAmountField.getText();

        if (sourceAccNumber == null || targetAccNumber.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Por favor, completa todos los campos de la transferencia.");
            return;
        }

        if (sourceAccNumber.equals(targetAccNumber)) {
            showAlert("Error", "La cuenta de origen y destino deben ser diferentes.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                showAlert("Error", "El monto a transferir debe ser positivo.");
                return;
            }

            boolean success = bankService.transfer(sourceAccNumber, targetAccNumber, amount);

            if (!success) {
                showAlert("Error", "La transferencia no pudo ser procesada.");
                return;
            }

            showAlert("Transferencia Exitosa",
                    String.format("Se transfirió %.2f desde %s hacia %s.",
                            amount, sourceAccNumber, targetAccNumber));


        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El monto debe ser un número válido.");
        }
    }

    @FXML
    private void handleShowAccountDetails() {
        String selectedAccountNumber = accountSelectionComboBox.getValue();

        if (selectedAccountNumber == null) {
            showAlert("Error de Selección", "Por favor, selecciona una cuenta de la lista.");
            return;
        }

        BankAccount selectedAccount = currentClient.getAccount(selectedAccountNumber);

        if (selectedAccount != null) {
            List<Transaction> transactions = selectedAccount.getTransactions();
            StringBuilder details = new StringBuilder();

            details.append("--- DETALLES DE CUENTA ---\n");
            details.append("Número: ").append(selectedAccount.getAccountNumber()).append("\n");
            details.append("Tipo: ").append(selectedAccount.getAccountType()).append("\n");
            details.append("Saldo Actual: $").append(String.format("%.2f", selectedAccount.getBalance())).append("\n\n");

            details.append("--- HISTORIAL DE TRANSACCIONES ---\n");
            if (transactions.isEmpty()) {
                details.append("No hay transacciones registradas para esta cuenta.");
            } else {
                for (Transaction transaction : transactions) {
                    details.append(transaction.toString()).append("\n");
                }
            }

            transactionDetailsTextArea.setText(details.toString());

        } else {
            showAlert("Error", "No se encontró la cuenta seleccionada.");
            transactionDetailsTextArea.setText("");
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