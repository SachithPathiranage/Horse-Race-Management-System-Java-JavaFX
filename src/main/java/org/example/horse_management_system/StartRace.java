package org.example.horse_management_system;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.example.horse_management_system.ValidationUtils.showErrorAlert;
import static org.example.horse_management_system.ValidationUtils.showInformationAlert;

public class StartRace implements Initializable {
    @FXML
    private AnchorPane Background;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private Button selectHorsesButton;

    @FXML
    private AnchorPane selectHorsesContent;

    @FXML
    private AnchorPane slider;

    @FXML
    private Button visualizationButton;

    @FXML
    private AnchorPane visualizationContent;

    @FXML
    private Button winnersButton;

    @FXML
    private AnchorPane winnersContent;

    @FXML
    private Button Select;

    @FXML
    private Button Visual;

    @FXML
    private Button Winning;

    @FXML
    private Label horse1IdLabel, horse1NameLabel, group1Label;

    @FXML
    private Label horse2IdLabel, horse2NameLabel, group2Label;

    @FXML
    private Label horse3IdLabel, horse3NameLabel, group3Label;

    @FXML
    private Label horse4IdLabel, horse4NameLabel, group4Label;

    @FXML
    private ImageView horse1ImageView, horse2ImageView, horse3ImageView, horse4ImageView;

    @FXML
    private Button selectButton;

    @FXML
    private Button startRaceButton;

    @FXML
    private Button clearButton;

    // Define a class-level variable to store selected horses
    private static List<HorseDetails> selectedHorses;

    //Store winning horses
    List<HorseDetails> winners;

    @FXML
    private ImageView firstPlaceImage;

    @FXML
    private Label firstPlaceName;

    @FXML
    private Label firstPlaceId;

    @FXML
    private Label firstPlaceJockey;

    @FXML
    private Label firstPlaceBreed;

    @FXML
    private Label firstPlaceTime;

    @FXML
    private ImageView secondPlaceImage;

    @FXML
    private Label secondPlaceName;

    @FXML
    private Label secondPlaceId;

    @FXML
    private Label secondPlaceJockey;

    @FXML
    private Label secondPlaceBreed;

    @FXML
    private Label secondPlaceTime;

    @FXML
    private ImageView thirdPlaceImage;

    @FXML
    private Label thirdPlaceName;

    @FXML
    private Label thirdPlaceId;

    @FXML
    private Label thirdPlaceJockey;

    @FXML
    private Label thirdPlaceBreed;

    @FXML
    private Label thirdPlaceTime;

    @FXML
    private BarChart<String, Number> barChart; // Add this field


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });

        // Initially hide the content panes
        selectHorsesContent.setVisible(false);
        winnersContent.setVisible(false);
        visualizationContent.setVisible(false);

        // Initialize buttons
        startRaceButton.setVisible(false);
        clearButton.setVisible(false);

        // Set up event handlers for sidebar buttons
        setupSidebarButtonHandlers();
    }

    private void setupSidebarButtonHandlers() {
        // Event handler for "Select Horses" button
        selectHorsesButton.setOnAction(event -> {
            // Show the content for "Select Horses" and hide others
            selectHorsesContent.setVisible(true);
            winnersContent.setVisible(false);
            visualizationContent.setVisible(false);
            Background.setVisible(false);

            // Change background color of buttons
            Select.setStyle("-fx-background-color: #24b6e5;");
            Winning.setStyle("-fx-background-color: transparent;");
            Visual.setStyle("-fx-background-color: transparent;");
        });

        // Event handler for "Winners" button
        winnersButton.setOnAction(event -> {
            // Show the content for "Winners" and hide others
            selectHorsesContent.setVisible(false);
            winnersContent.setVisible(true);
            visualizationContent.setVisible(false);
            Background.setVisible(false);

            // Change background color of buttons
            Select.setStyle("-fx-background-color: transparent;");
            Winning.setStyle("-fx-background-color: #24b6e5;");
            Visual.setStyle("-fx-background-color: transparent;");
        });

        // Event handler for "Visualization" button
        visualizationButton.setOnAction(event -> {
            // Show the content for "Visualization" and hide others
            selectHorsesContent.setVisible(false);
            winnersContent.setVisible(false);
            visualizationContent.setVisible(true);
            Background.setVisible(false);

            // Change background color of buttons
            Select.setStyle("-fx-background-color: transparent;");
            Winning.setStyle("-fx-background-color: transparent;");
            Visual.setStyle("-fx-background-color: #24b6e5;");
        });
    }

    // This method will be called when the "Select" button is clicked
    @FXML
    private List<HorseDetails> onSelectButtonClick() {
        List<HorseDetails> horses = readHorseDetailsFromFile("horse_details.txt");
        if (horses.size() < 4) {
            showErrorAlert("Not enough horses in the file.");
            return Collections.emptyList(); // Return an empty list if there are not enough horses
        }

        List<HorseDetails> selectedHorses = selectRandomHorses(horses, 4);
        if (selectedHorses.isEmpty()) {
            showErrorAlert("Failed to select horses. Try Again");
            return Collections.emptyList(); // Return an empty list if selection failed
        }
        for (HorseDetails horse : selectedHorses) {
            System.out.println("Selected Horse:");
            System.out.println("ID: " + horse.id);
            System.out.println("Name: " + horse.name);
            System.out.println("Jockey Name: " + horse.jockeyName);
            System.out.println("Age: " + horse.age);
            System.out.println("Breed: " + horse.breed);
            System.out.println("Race Record: " + horse.raceRecord);
            System.out.println("Image Path: " + horse.imagePath);
            System.out.println();
        }
        displaySelectedHorses(selectedHorses);

        showInformationAlert("Horses selected successfully.");

        // Show Start Race and Clear buttons
        startRaceButton.setVisible(true);
        clearButton.setVisible(true);

        selectButton.setText("Re-Select");

        return selectedHorses; // Return the selected horses
    }


    public static List<HorseDetails> readHorseDetailsFromFile(String filename) {
        List<HorseDetails> horses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Horse ID")) {
                    String id = line.split(":")[1].trim();
                    String name = br.readLine().split(":")[1].trim();
                    String jockeyName = br.readLine().split(":")[1].trim();
                    int age = Integer.parseInt(br.readLine().split(":")[1].trim());
                    String breed = br.readLine().split(":")[1].trim();
                    String raceRecord = br.readLine().split(":")[1].trim();
                    String imagePath = br.readLine().split(":")[1].trim();
                    HorseDetails horse = new HorseDetails(id, name, jockeyName, age, breed, raceRecord, imagePath);
                    horses.add(horse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return horses;
    }

    public static List<HorseDetails> selectRandomHorses(List<HorseDetails> horses, int count) {
        Map<Character, List<HorseDetails>> groupedHorses = new HashMap<>();

        // Group the horses by the first character of their IDs
        for (HorseDetails horse : horses) {
            char group = horse.getId().charAt(0);
            groupedHorses.computeIfAbsent(group, k -> new ArrayList<>()).add(horse);
        }

        selectedHorses = new ArrayList<>();
        Random random = new Random();

        // Select one horse from each group
        List<Character> groupKeys = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D'));
        Collections.shuffle(groupKeys); // Shuffle the group keys to randomize selection

        for (int i = 0; i < count; i++) {
            char group = groupKeys.get(i);
            List<HorseDetails> groupHorses = groupedHorses.get(group);
            if (groupHorses != null && !groupHorses.isEmpty()) {
                int index = random.nextInt(groupHorses.size());
                selectedHorses.add(groupHorses.get(index));
                groupHorses.remove(index);
            }
        }


        return selectedHorses;
    }

    private void displaySelectedHorses(List<HorseDetails> selectedHorses) {
        // Display the selected horses' details on the GUI
        horse1IdLabel.setText("Horse Id: " + selectedHorses.get(0).getId());
        horse1NameLabel.setText("Name: " + selectedHorses.get(0).getName());
        group1Label.setText("Group: " + selectedHorses.get(0).getId().substring(0, 1));
        File file1 = new File(selectedHorses.get(0).getImagePath());
        Image image1 = new Image(file1.toURI().toString());
        horse1ImageView.setImage(image1);


        horse2IdLabel.setText("Horse Id: " + selectedHorses.get(1).getId());
        horse2NameLabel.setText("Name: " + selectedHorses.get(1).getName());
        group2Label.setText("Group: " + selectedHorses.get(1).getId().substring(0, 1));
        File file2 = new File(selectedHorses.get(1).getImagePath());
        Image image2 = new Image(file2.toURI().toString());
        horse2ImageView.setImage(image2);

        horse3IdLabel.setText("Horse Id: " + selectedHorses.get(2).getId());
        horse3NameLabel.setText("Name: " + selectedHorses.get(2).getName());
        group3Label.setText("Group: " + selectedHorses.get(2).getId().substring(0, 1));
        File file3 = new File(selectedHorses.get(2).getImagePath());
        Image image3 = new Image(file3.toURI().toString());
        horse3ImageView.setImage(image3);

        horse4IdLabel.setText("Horse Id: " + selectedHorses.get(3).getId());
        horse4NameLabel.setText("Name: " + selectedHorses.get(3).getName());
        group4Label.setText("Group: " + selectedHorses.get(3).getId().substring(0, 1));
        File file4 = new File(selectedHorses.get(3).getImagePath());
        Image image4 = new Image(file4.toURI().toString());
        horse4ImageView.setImage(image4);
    }

    // Define method to clear labels and image views
    @FXML
    private void clearLabels() {
        // Clear horse 1 labels and image view
        horse1IdLabel.setText("");
        horse1NameLabel.setText("");
        group1Label.setText("");
        horse1ImageView.setImage(null);

        // Clear horse 2 labels and image view
        horse2IdLabel.setText("");
        horse2NameLabel.setText("");
        group2Label.setText("");
        horse2ImageView.setImage(null);

        // Clear horse 3 labels and image view
        horse3IdLabel.setText("");
        horse3NameLabel.setText("");
        group3Label.setText("");
        horse3ImageView.setImage(null);

        // Clear horse 4 labels and image view
        horse4IdLabel.setText("");
        horse4NameLabel.setText("");
        group4Label.setText("");
        horse4ImageView.setImage(null);

        selectButton.setText("Select");
    }

    public List<HorseDetails> startRace(ActionEvent actionEvent) {
        winners = new ArrayList<>();
        if (selectedHorses == null || selectedHorses.size() != 4) {
            showErrorAlert("Please select horses before starting the race.");
            return winners; // Return an empty list if the race cannot be started
        }

        // Assign random times to the selected horses
        for (HorseDetails horse : selectedHorses) {
            horse.setFinishTime((int) (Math.random() * 91)); // Random time between 0 to 90 seconds
            System.out.println(horse.getName() + " = " + horse.getFinishTime());
        }

        // Sort the horses based on their finishing times
        Collections.sort(selectedHorses, Comparator.comparingInt(HorseDetails::getFinishTime));

        // Determine the 1st, 2nd, and 3rd places
        HorseDetails firstPlace = selectedHorses.get(0);
        HorseDetails secondPlace = selectedHorses.get(1);
        HorseDetails thirdPlace = selectedHorses.get(2);

        // Add the winning horses to the winners list
        winners.add(firstPlace);
        winners.add(secondPlace);
        winners.add(thirdPlace);

        // Display the results
        showInformationAlert("Results:\n1st Place: " + firstPlace.getName() + "\n2nd Place: " + secondPlace.getName() + "\n3rd Place: " + thirdPlace.getName());

        startRaceButton.setText("Race Again");
        System.out.println(winners);

        return winners;
    }


    private void displayHorseDetails(int place, ImageView imageView, Label nameLabel, Label idLabel, Label jockeyLabel, Label breedLabel, Label timeLabel, HorseDetails horse) {
        // Set image
        File file = new File(horse.getImagePath());
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        // Set text labels
        nameLabel.setText(getPlaceString(place) + horse.getName());
        idLabel.setText("ID: " + horse.getId());
        jockeyLabel.setText("Jockey: " + horse.getJockeyName());
        breedLabel.setText("Breed: " + horse.getBreed());
        timeLabel.setText("Time Spent: " + horse.getFinishTime() + " seconds");
    }

    private String getPlaceString(int place) {
        switch (place) {
            case 1:
                return "1st Place: ";
            case 2:
                return "2nd Place: ";
            case 3:
                return "3rd Place: ";
            default:
                return "Unknown Place";
        }
    }

    public void DisplayWinningHorses(MouseEvent actionEvent) {

        // Ensure that the list contains at least 3 winners
        if (winners == null) {
            showErrorAlert("Please select horses for the race first!!!");
        } else {
            // Display the details of the winning horses
            for (int i = 0; i < winners.size(); i++) {
                HorseDetails winner = winners.get(i);
                System.out.println("Winner " + (i + 1) + ":");
                System.out.println("ID: " + winner.getId());
                System.out.println("Name: " + winner.getName());
                System.out.println("Jockey Name: " + winner.getJockeyName());
                System.out.println("Breed: " + winner.getBreed());
                System.out.println("Finish Time: " + winner.getFinishTime() + " seconds");
                System.out.println();
            }
            // Display details of the first place horse
            displayHorseDetails(1, firstPlaceImage, firstPlaceName, firstPlaceId, firstPlaceJockey, firstPlaceBreed, firstPlaceTime, winners.get(0));
            // Display details of the second place horse
            displayHorseDetails(2, secondPlaceImage, secondPlaceName, secondPlaceId, secondPlaceJockey, secondPlaceBreed, secondPlaceTime, winners.get(1));
            // Display details of the third place horse
            displayHorseDetails(3, thirdPlaceImage, thirdPlaceName, thirdPlaceId, thirdPlaceJockey, thirdPlaceBreed, thirdPlaceTime, winners.get(2));
        }
        startRaceButton.setVisible(false);
        clearButton.setVisible(false);
    }

    @FXML
    public void VisualizeWinningHorses(MouseEvent actionEvent) {
        // Ensure that the list contains at least 3 winners
        if (winners == null || winners.size() < 3) {
            showErrorAlert("Please select horses for the race first!!!");
        } else {
            // Clear existing data from the bar chart
            barChart.getData().clear();

            // Create a new series for the bar chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Finish Time");

            // Add data to the series
            for (int i = 0; i < 3; i++) {
                HorseDetails winner = winners.get(i);
                String horseName = winner.getName();
                double finishTime = winner.getFinishTime();
                series.getData().add(new XYChart.Data<>(horseName, finishTime));
            }

            // Add the series to the bar chart
            barChart.getData().add(series);
        }
        startRaceButton.setVisible(false);
        clearButton.setVisible(false);
    }
}