package UQBank.controller;

import UQBank.model.service.BankService;
import UQBank.model.users.User;
import UQBank.model.users.Admin;
import UQBank.model.users.Cashier;
import UQBank.model.users.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final BankService bankService = BankService.getInstance();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error de Acceso", "Por favor, introduce tu correo y contraseña.");
            return;
        }

        User authenticatedUser = bankService.login(email, password);

        if (authenticatedUser != null) {
            showAlert("Acceso Concedido", "¡Bienvenido, " + authenticatedUser.getFullName() + "!");
            navigateToDashboard(authenticatedUser);
        } else {
            showAlert("Error de Acceso", "Credenciales incorrectas. Verifica tu correo y contraseña.");
        }
    }

    private void navigateToDashboard(User user) {
        String fxmlPath = "";
        String title = "UQBank - ";

        if (user instanceof Admin) {
            fxmlPath = "/UQBank/view/AdminDashboard.fxml";
            title += "Consola de Administración";
        } else if (user instanceof Cashier) {
            fxmlPath = "/UQBank/view/CashierDashboard.fxml";
            title += "Consola de Cajero";
        } else if (user instanceof Client) {
            fxmlPath = "/UQBank/view/ClientDashboard.fxml";
            title += "Panel de Cliente";
        } else {
            showAlert("Error", "Tipo de usuario no reconocido.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error de Navegación", "No se pudo cargar la interfaz: " + fxmlPath);
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