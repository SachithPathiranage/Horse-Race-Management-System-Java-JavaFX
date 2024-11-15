package org.example.horse_management_system;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;

import static org.example.horse_management_system.Horse.GotoPreviousStage;

public class ViewHorse {
    @FXML
    private TableView<Horse> horseTableView;

    @FXML
    private TableColumn<Horse, String> horseIDColumn;

    @FXML
    private TableColumn<Horse, String> horseNameColumn;

    @FXML
    private TableColumn<Horse, String> jockeyNameColumn;

    @FXML
    private TableColumn<Horse, Integer> ageColumn;

    @FXML
    private TableColumn<Horse, String> breedColumn;

    @FXML
    private TableColumn<Horse, String> raceRecordColumn;

    @FXML
    private TableColumn<Horse, String> horseImageColumn;

    @FXML
    private TableColumn<Horse, String> groupColumn;

    @FXML
    public void initialize() {
        // Initialize columns
        horseIDColumn.setCellValueFactory(cellData -> cellData.getValue().horseIDProperty());
        horseNameColumn.setCellValueFactory(cellData -> cellData.getValue().horseNameProperty());
        jockeyNameColumn.setCellValueFactory(cellData -> cellData.getValue().jockeyNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        breedColumn.setCellValueFactory(cellData -> cellData.getValue().breedProperty());
        raceRecordColumn.setCellValueFactory(cellData -> cellData.getValue().raceRecordProperty());
        horseImageColumn.setCellValueFactory(cellData -> cellData.getValue().horseImagePathProperty());
        groupColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGroup()));

        horseImageColumn.setCellFactory(column -> {
            return new TableCell<Horse, String>() {
                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        // Load image from imagePath and set it as graphic
                        try {
                            ImageView imageView = new ImageView(new Image(new FileInputStream(imagePath)));
                            imageView.setFitWidth(50); // Set width of the image
                            imageView.setFitHeight(50); // Set height of the image
                            setGraphic(imageView);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            setGraphic(null);
                        }
                    }
                }
            };
        });
        // Load and populate horse details
        ObservableList<Horse> horseData = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("horse_details.txt"))) {
            String line;
            String horseID = null, horseName = null, jockeyName = null, age = null, breed = null, raceRecord = null, imagePath = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Horse ID")) {
                    if (horseID != null) {
                        horseData.add(new Horse(horseID, horseName, jockeyName, age, breed, raceRecord, imagePath));
                    }
                    horseID = line.split(":")[1].trim();
                } else if (line.startsWith("Horse Name")) {
                    horseName = line.split(":")[1].trim();
                } else if (line.startsWith("Jockey Name")) {
                    jockeyName = line.split(":")[1].trim();
                } else if (line.startsWith("Age")) {
                    age = line.split(":")[1].trim();
                } else if (line.startsWith("Breed")) {
                    breed = line.split(":")[1].trim();
                } else if (line.startsWith("Race Record")) {
                    raceRecord = line.split(":")[1].trim();
                } else if (line.startsWith("Image Path")) {
                    imagePath = line.split(":")[1].trim();
                }
            }
            // Add the last horse
            if (horseID != null) {
                horseData.add(new Horse(horseID, horseName, jockeyName, age, breed, raceRecord, imagePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort horse data by ID using bubble sort
        bubbleSort(horseData);


        // Populate TableView
        horseTableView.setItems(horseData);
    }

    public void bubbleSort(ObservableList<Horse> horseData) {
        int n = horseData.size();
        // Sort horse data by ID manually
        for (int i = 0; i < horseData.size() - 1; i++) {
            for (int j = i + 1; j < horseData.size(); j++) {
                String horseID1 = horseData.get(i).getHorseID(); // Extract the value
                String horseID2 = horseData.get(j).getHorseID(); // Extract the value

                // Extract group and number from horse IDs
                char group1 = horseID1.charAt(0);
                char group2 = horseID2.charAt(0);
                int number1 = Integer.parseInt(horseID1.substring(1));
                int number2 = Integer.parseInt(horseID2.substring(1));

                // Compare group characters
                if (group1 > group2) {
                    // Swap horses at index i and j
                    Horse temp = horseData.get(i);
                    horseData.set(i, horseData.get(j));
                    horseData.set(j, temp);
                } else if (group1 == group2) {
                    // If group characters are the same, compare numbers
                    if (number1 > number2) {
                        // Swap horses at index i and j
                        Horse temp = horseData.get(i);
                        horseData.set(i, horseData.get(j));
                        horseData.set(j, temp);
                    }
                }
            }
        }
}
        @FXML
    public void PreviousStage(MouseEvent mouseEvent) {
        GotoPreviousStage(mouseEvent);
    }

}
