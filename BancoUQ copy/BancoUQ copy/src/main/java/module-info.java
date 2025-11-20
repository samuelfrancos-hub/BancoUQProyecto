module BancoUQ {
    requires javafx.controls;
    requires javafx.fxml;

    exports UQBank;
    opens UQBank to javafx.fxml;

    opens UQBank.controller to javafx.fxml;
}
