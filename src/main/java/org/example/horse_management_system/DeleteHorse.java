package org.example.horse_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.horse_management_system.Horse.GotoPreviousStage;
import static org.example.horse_management_system.ValidationUtils.showErrorAlert;
import static org.example.horse_management_system.ValidationUtils.showInformationAlert;

public class DeleteHorse {

    @FXML
    public TextField horseIdField;
    @FXML
    private Label horseNameLabel;
    @FXML
    private Label jockeyNameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label breedLabel;
    @FXML
    private Label raceRecordLabel;
    @FXML
    private ImageView horseImageView;
    @FXML
    private Label Id;

    @FXML
    public void onSearchButtonClick() {
        // Retrieve the entered horse ID from the text field
        String horseId = horseIdField.getText().trim();

        // Check if the horse ID is empty
        if (horseId.isEmpty()) {
            showErrorAlert("Please enter a Horse ID to proceed.");
            return;
        }

        // Read the text file to find the details of the horse with the entered ID
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("horse_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line to get the horse ID and its details
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equals("Horse ID") && parts[1].trim().equals(horseId)) {
                    // Update the text fields with the retrieved details
                    found = true;
                    String[] details = new String[7];
                    for (int i = 0; i < 6; i++) {
                        details[i] = reader.readLine().split(":")[1].trim();
                    }
                    horseNameLabel.setText("Horse Name:    " + details[0]);
                    jockeyNameLabel.setText("Jockey Name:   " + details[1]);
                    ageLabel.setText("Horse Age:   " + details[2]);
                    breedLabel.setText("Breed:   " + details[3]);
                    raceRecordLabel.setText("Race Record:  " + details[4]);

                    // Load and display the image
                    String imagePath = details[5].trim(); // Trim the leading and trailing whitespace
                    if (!imagePath.isEmpty()) {
                        try {
                            // Create a File object using the image path
                            File file = new File(imagePath);

                            // Check if the file exists
                            if (file.exists()) {
                                // Create an Image object from the file URL
                                Image image = new Image(file.toURI().toString());

                                // Set the image to the ImageView
                                horseImageView.setImage(image);
                            } else {
                                System.err.println("Image file does not exist: " + imagePath);
                            }
                        } catch (Exception e) {
                            System.err.println("Error loading image: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        horseImageView.setImage(null); // Clear the image view if no image path is provided
                    }


                    // Disable editing of horse ID text field
                    horseIdField.setText(horseId);
                    Id.setText("Horse Id:   " + horseId);
                    break;
                }
            }
        } catch (IOException e) {
            // Handle IOException
            showErrorAlert("An error occurred while reading horse details. Please try again later.");
            e.printStackTrace();
        }

        // If the horse ID is not found, display an error alert
        if (!found) {
            showErrorAlert("Horse ID not found!");
        } else {
            // Display success message
            showInformationAlert("Horse details loaded successfully.");
        }

    }

    @FXML
    public void PreviousStage(MouseEvent mouseEvent) {
        GotoPreviousStage(mouseEvent);
    }

    public void ClearDetails(ActionEvent actionEvent) {
        // Reset all text fields to empty strings
        Id.setText("");
        horseNameLabel.setText("");
        jockeyNameLabel.setText("");
        ageLabel.setText("");
        breedLabel.setText("");
        raceRecordLabel.setText("");
        horseImageView.setImage(null);
    }

    public void DeleteDetails(ActionEvent actionEvent) {
        // Retrieve the entered horse ID from the text field
        String horseId = horseIdField.getText().trim();

        // Check if the horse ID is empty
        if (horseId.isEmpty()) {
            showErrorAlert("Please enter a Horse ID to delete.");
            return;
        }

        // Create a list to hold remaining content
        List<String> remainingLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("horse_details.txt"))) {
            String line;
            boolean found = false;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equals("Horse ID") && parts[1].trim().equals(horseId)) {
                    // Skip lines related to the horse to be deleted
                    for (int i = 0; i < 6; i++) {
                        reader.readLine(); // Skip next 6 lines (details for the horse to be deleted)
                    }
                    found = true;
                } else {
                    // Add non-matching lines to the list
                    remainingLines.add(line);
                }
            }

            // If the horse ID is not found, display an error alert
            if (!found) {
                showErrorAlert("Horse ID not found!");
                return;
            }

        } catch (IOException e) {
            showErrorAlert("An error occurred while deleting horse details. Please try again later.");
            e.printStackTrace();
            return;
        }

        // Write the remaining content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("horse_details.txt"))) {
            for (String line : remainingLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            showErrorAlert("An error occurred while writing updated horse details. Please try again later.");
            e.printStackTrace();
            return;
        }

        // Display success message
        showInformationAlert("Horse details deleted successfully.");

        // Clear all fields after deletion
        ClearDetails(null); // Assuming ClearDetails method clears all fields

        // Clear the horse ID field
        horseIdField.setText("");
    }
}
