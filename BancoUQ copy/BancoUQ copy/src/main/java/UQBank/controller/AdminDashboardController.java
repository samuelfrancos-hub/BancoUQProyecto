package UQBank.controller;

import UQBank.model.service.BankService;
import UQBank.model.users.User;
import UQBank.model.users.Admin;
import UQBank.model.users.Cashier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;

public class AdminDashboardController {

    @FXML private Label adminWelcomeLabel;
    @FXML private TextField cashierNameField;
    @FXML private TextField cashierEmailField;
    @FXML private PasswordField cashierPasswordField;
    @FXML private TextField cashierIdField;
    @FXML private PasswordField adminSecurityPinField;
    @FXML private TextArea adminLogArea;
    @FXML private ListView<String> userListView;
    @FXML private TextField searchField;

    private final BankService bankService = BankService.getInstance();
    private Admin currentAdmin;

    @FXML
    public void initialize() {
        User currentUser = bankService.getCurrentUser();

        if (currentUser instanceof Admin) {
            this.currentAdmin = (Admin) currentUser;
            if (adminWelcomeLabel != null) {
                adminWelcomeLabel.setText("Consola de Admin: Bienvenido, " + currentAdmin.getFullName());
            }
        } else {
            if (adminWelcomeLabel != null) {
                adminWelcomeLabel.setText("Consola de Admin: Error cargando perfil");
            }
        }

        logAction("Consola inicializada.");

        if (userListView != null) {
            updateUserListView();
        }
    }

    private boolean checkAdminPin(String actionName) {
        final String CORRECT_PIN = "123456";
        String enteredPin = adminSecurityPinField.getText();

        if (enteredPin.isEmpty()) {
            showAlert("Error de Seguridad", "Introduce el PIN para " + actionName + ".");
            return false;
        }

        if (!enteredPin.equals(CORRECT_PIN)) {
            showAlert("Error de Seguridad", "PIN de Administrador incorrecto. Operación denegada.");
            adminSecurityPinField.clear();
            return false;
        }

        return true;
    }

    @FXML
    private void handleRegisterCashier() {
        String action = "Registrar Cajero";
        if (!checkAdminPin(action)) return;

        String name = cashierNameField.getText();
        String email = cashierEmailField.getText();
        String password = cashierPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error de Entrada", "Todos los campos de registro de cajero son obligatorios.");
            return;
        }

        String newId = bankService.generateNextId("CASHIER");
        Cashier newCashier = new Cashier(newId, name, password, email);
        bankService.addUser(newCashier);

        logAction("Cajero registrado con éxito: " + name + " (ID: " + newId + ")");
        showAlert("Éxito", "Cajero " + name + " registrado con ID: " + newId);

        cashierNameField.clear();
        cashierEmailField.clear();
        cashierPasswordField.clear();
        adminSecurityPinField.clear();
        if (userListView != null) updateUserListView();
    }

    @FXML
    private void handleRemoveCashier() {
        String action = "Eliminar Cajero";
        if (!checkAdminPin(action)) return;

        String idToRemove = cashierIdField.getText();

        if (idToRemove.isEmpty()) {
            showAlert("Error de Entrada", "Introduce el ID del cajero a eliminar.");
            return;
        }

        if (bankService.deleteUser(idToRemove)) {
            logAction("Cajero eliminado con éxito: ID " + idToRemove);
            showAlert("Éxito", "Cajero con ID " + idToRemove + " eliminado.");
        } else {
            logAction("Fallo al eliminar usuario: ID " + idToRemove + ". Usuario no encontrado o es el admin actual.");
            showAlert("Error", "No se pudo eliminar el cajero. Asegúrate de que el ID sea correcto y no sea el administrador actual.");
        }

        cashierIdField.clear();
        adminSecurityPinField.clear();
        if (userListView != null) updateUserListView();
    }

    @FXML
    private void handleGenerateReport() {
        StringBuilder reportDetails = new StringBuilder();
        int userCount = bankService.getUsers().size();

        reportDetails.append("\n--- REPORTE DETALLADO DE USUARIOS DEL SISTEMA ---\n");
        reportDetails.append("TOTAL DE USUARIOS REGISTRADOS: ").append(userCount).append("\n\n");

        for (User user : bankService.getUsers()) {
            reportDetails.append("--------------------------------------\n");
            reportDetails.append("  Nombre: ").append(user.getFullName()).append("\n");
            reportDetails.append("  Rol:    ").append(user.getRole()).append("\n");
            reportDetails.append("  ID:     ").append(user.getUserId()).append("\n");
            reportDetails.append("  Email:  ").append(user.getEmail()).append("\n");
        }
        reportDetails.append("--------------------------------------\n");

        logAction("Reporte generado: " + userCount + " usuarios listados.");

        adminLogArea.appendText(reportDetails.toString() + "\n");

        showAlert("Reporte Generado", "El reporte detallado se ha escrito en el Log de Actividad del Administrador.");
    }

    @FXML
    private void handleEndOfMonthProcess() {
        String result = bankService.processEndOfMonth();

        logAction("Proceso de Fin de Mes ejecutado: " + result);
        showAlert("Proceso Finalizado", result);
    }

    @FXML
    private void handleLogout() {
        logAction("Usuario cerró sesión. Volviendo a pantalla de acceso.");
        bankService.logout();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UQBank/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) adminWelcomeLabel.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("UQBank - Acceso");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error de Navegación", "No se pudo cargar la pantalla de Login: " + e.getMessage());
        }
    }

    @FXML
    private void handleFindClientDetails() {
        showAlert("Funcionalidad Pendiente", "Buscar Cliente no implementado en esta vista.");
    }

    @FXML
    private void handleSearchUser() {
        showAlert("Funcionalidad Pendiente", "Búsqueda de usuario no implementada en esta vista.");
    }

    private void updateUserListView() {
        if (userListView != null) {
            userListView.getItems().setAll(
                    bankService.getUsers().stream()
                            .map(u -> u.getFullName() + " (" + u.getRole() + ")")
                            .collect(Collectors.toList())
            );
        }
    }

    private void logAction(String message) {
        if (adminLogArea != null) {
            adminLogArea.appendText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + " - " + message + "\n");
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