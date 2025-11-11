package com.example.taller5preparcial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Taller 5 - Dashboard FXML");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el FXML del Dashboard.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}