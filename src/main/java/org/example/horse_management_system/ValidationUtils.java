package org.example.horse_management_system;

import javafx.scene.control.Alert;

public class ValidationUtils {

    public static boolean isValidName(String name) {
        // Check if the name is not null and matches the regex pattern
        return name != null && name.matches("[a-zA-Z\\s]+");
    }

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
