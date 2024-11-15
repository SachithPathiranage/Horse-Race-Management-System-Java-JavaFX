package org.example.horse_management_system;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


public class Horse {
    private final StringProperty horseID;
    private final StringProperty horseName;
    private final StringProperty jockeyName;
    private final IntegerProperty age;
    private final StringProperty breed;
    private final StringProperty raceRecord;
    private final StringProperty horseImagePath;

    public Horse(String horseID, String horseName, String jockeyName, String age, String breed, String raceRecord, String horseImagePath) {
        this.horseID = new SimpleStringProperty(horseID);
        this.horseName = new SimpleStringProperty(horseName);
        this.jockeyName = new SimpleStringProperty(jockeyName);
        this.age = new SimpleIntegerProperty(Integer.parseInt(String.valueOf(age))); // Parse age to integer
        this.breed = new SimpleStringProperty(breed);
        this.raceRecord = new SimpleStringProperty(raceRecord);
        this.horseImagePath = new SimpleStringProperty(horseImagePath);
    }

    // Getters for properties
    public StringProperty horseIDProperty() {
        return horseID;
    }

    public StringProperty horseNameProperty() {
        return horseName;
    }

    public StringProperty jockeyNameProperty() {
        return jockeyName;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public StringProperty breedProperty() {
        return breed;
    }

    public StringProperty raceRecordProperty() {
        return raceRecord;
    }

    public StringProperty horseImagePathProperty() {
        return horseImagePath;
    }

    // Getter for horseID
    public String getHorseID() {
        return horseID.get();
    }

    public String getGroup() {
        // Assuming horseID is not null or empty
        return String.valueOf(horseID.get().charAt(0));
    }

    ///////////////////////////////////////////////////////////
    /////////////////////Common Methods////////////////////////
    ///////////////////////////////////////////////////////////

    //Method to choose the image in add and update classes
    static void chooseImageforUpdate(ActionEvent event, ImageView horseImageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Horse Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            // Set the selected image to the horseImageView
            Image image = new Image(selectedFile.toURI().toString());
            horseImageView.setImage(image);
        }
    }

    //Method to Read horse details in add and update classes
    public static Map<String, Map<String, String>> readHorseDetails() {
        // Initialize an empty map to hold horse details
        Map<String, Map<String, String>> horseDetailsMap = new HashMap<>();

        // Create a file object for horse_details.txt
        File file = new File("horse_details.txt");

        // Check if the file exists
        if (file.exists()) {
            // If the file exists, read the details from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String currentHorseId = null;
                Map<String, String> currentHorseDetails = new HashMap<>();

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        // Empty line indicates the end of current horse details, add to map
                        if (currentHorseId != null) {
                            horseDetailsMap.put(currentHorseId, currentHorseDetails);
                            currentHorseDetails = new HashMap<>();
                        }
                        currentHorseId = null;
                    } else {
                        // Split the line into key-value pair and add to current horse details
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            currentHorseDetails.put(key, value);
                            if (key.equals("Horse ID")) {
                                currentHorseId = value;
                            }
                        }
                    }
                }
                // Add the last horse details to the map
                if (currentHorseId != null && !currentHorseDetails.isEmpty()) {
                    horseDetailsMap.put(currentHorseId, currentHorseDetails);
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            System.out.println("horse_details.txt does not exist. Creating a new file.");
            // If the file does not exist, create it
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }

        return horseDetailsMap;
    }

    //Method to write horse details into the text file in add and update classes
    public static void WriteHorseDetails(Map<String, Map<String, String>> horseDetailsMap, String[] fieldOrder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("horse_details.txt"))) {
            Map<Character, List<Map.Entry<String, Map<String, String>>>> groupedHorseDetails = new HashMap<>();

            // Group horse details by the first character of the horse ID
            for (Map.Entry<String, Map<String, String>> entry : horseDetailsMap.entrySet()) {
                String horseId = entry.getKey();
                Map<String, String> details = entry.getValue();
                char group = horseId.charAt(0);
                groupedHorseDetails.computeIfAbsent(group, k -> new ArrayList<>()).add(entry);
            }

            // Sort horse details within each group based on the numeric part of the horse ID
            for (List<Map.Entry<String, Map<String, String>>> groupDetails : groupedHorseDetails.values()) {
                groupDetails.sort(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey().substring(1))));
            }

            // Write horse details under their respective group names
            for (Map.Entry<Character, List<Map.Entry<String, Map<String, String>>>> groupEntry : groupedHorseDetails.entrySet()) {
                char group = groupEntry.getKey();
                List<Map.Entry<String, Map<String, String>>> groupDetails = groupEntry.getValue();

                writer.write("Group " + group + "\n");
                writer.write("-------------\n");

                for (Map.Entry<String, Map<String, String>> detailsEntry : groupDetails) {
                    Map<String, String> details = detailsEntry.getValue();
                    for (String field : fieldOrder) {
                        String value = details.get(field);
                        if (value != null) {
                            writer.write(String.format("%-15s: %s\n", field, value));
                        }
                    }

                    // Write image path if available
                    String imagePath = details.get("Image Path");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        writer.write(String.format("%-15s: %s\n", "Image Path", imagePath));
                    }

                    writer.write("\n"); // Add a newline between horse details
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Method to go to the Main Menu
    static void GotoPreviousStage(MouseEvent mouseEvent) {
        // Get the source node of the event
        Node source = (Node) mouseEvent.getSource();
        // Get the stage
        Stage stage = (Stage) source.getScene().getWindow();

        try {
            // Load the FXML file for the main menu
            Parent root = FXMLLoader.load(Horse.class.getResource("mainMenu.fxml"));

            // Set the desired width and height of the scene
            double sceneWidth = 600; // Set your desired width
            double sceneHeight = 400; // Set your desired height
            Scene scene = new Scene(root, sceneWidth, sceneHeight);

            // Set the scene
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Main Menu");

            // Calculate the center coordinates of the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2;

            // Set the stage position to the center of the screen
            stage.setX(centerX);
            stage.setY(centerY);

            // Show the stage
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Map<String, String>> readAllHorseDetails(String filename) {
        // Initialize an empty map to hold horse details
        Map<String, Map<String, String>> horseDetailsMap = new HashMap<>();

        // Create a file object for the specified filename
        File file = new File(filename);

        // Check if the file exists
        if (file.exists()) {
            // If the file exists, read the details from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String currentHorseId = null;
                Map<String, String> currentHorseDetails = new HashMap<>();

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        // Empty line indicates the end of current horse details, add to map
                        if (currentHorseId != null) {
                            horseDetailsMap.put(currentHorseId, currentHorseDetails);
                            currentHorseDetails = new HashMap<>();
                        }
                        currentHorseId = null;
                    } else {
                        // Split the line into key-value pair and add to current horse details
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            currentHorseDetails.put(key, value);
                            if (key.equals("Horse ID")) {
                                currentHorseId = value;
                            }
                        }
                    }
                }
                // Add the last horse details to the map
                if (currentHorseId != null && !currentHorseDetails.isEmpty()) {
                    horseDetailsMap.put(currentHorseId, currentHorseDetails);
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            System.out.println(filename + " does not exist. Creating a new file.");
            // If the file does not exist, create it
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }

        return horseDetailsMap;
    }
}

