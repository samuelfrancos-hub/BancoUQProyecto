 module com.example.taller5preparcial {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.taller5preparcial to javafx.fxml;
    exports com.example.taller5preparcial;
}
