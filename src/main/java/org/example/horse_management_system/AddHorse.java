package org.example.horse_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.horse_management_system.Horse.*;
import static org.example.horse_management_system.ValidationUtils.*;

public class AddHorse {
    public TextField horseIdField;
    public TextField horseNameField;
    public TextField jockeyNameField;
    public TextField ageField;
    public TextField breedField;
    public TextField raceRecordField;
    @FXML
    public ImageView horseImageView;

    private static final String[] FIELD_ORDER = {"Horse ID", "Horse Name", "Jockey Name", "Age", "Breed", "Race Record"};


    public void initialize() {
        // Set tooltip for horseIdField
        Tooltip horseIdTooltip = new Tooltip("Enter a Horse ID that starts with A, B, C, or D followed by any number.");
        horseIdField.setTooltip(horseIdTooltip);

        // Set placeholder text
        horseIdField.setPromptText("eg: A1");
        horseNameField.setPromptText("Horse Name");
        jockeyNameField.setPromptText("Jockey Name");
        ageField.setPromptText("Horse Age");
        breedField.setPromptText("Horse Breed");

        // Set tooltip for raceRecordField
        Tooltip raceRecordTooltip = new Tooltip("Enter race record in the format: eg: '10 wins, 5 second places, 2 third places'");
        raceRecordField.setTooltip(raceRecordTooltip);

        // Set placeholder text for raceRecordField
        raceRecordField.setPromptText(" ' ' wins, ' ' second places, ' ' third places");
    }

    public void AddDetails(ActionEvent actionEvent) {
        // Retrieve data from text fields
        String horseId = horseIdField.getText();
        String horseName = horseNameField.getText();
        String jockeyName = jockeyNameField.getText();
        String ageText = ageField.getText();
        String breed = breedField.getText();
        String raceRecord = raceRecordField.getText();
        String imagePath = ""; // Initialize the image path

        // Perform validations
        if (horseId.isEmpty() || horseName.isEmpty() || jockeyName.isEmpty() || ageText.isEmpty() || breed.isEmpty() || raceRecord.isEmpty()) {
            // Show error message if any field is empty
            showErrorAlert("All fields are required.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            // Add further validation if needed
        } catch (NumberFormatException e) {
            // Show error message if age is not a valid number
            showErrorAlert("Age must be a valid number.");
            return;
        }

        if (!isValidHorseId(horseId)) {
            // Show error message if horse ID format is invalid
            showErrorAlert("Invalid horse ID format. Horse ID must start with A, B, C, or D followed by any number.");
            return;
        }

        if (!isValidName(horseName)) {
            showErrorAlert("Invalid horse name. Please enter only letters and spaces.");
            return;
        }

        if (!isValidName(jockeyName)) {
            showErrorAlert("Invalid jockey name. Please enter only letters and spaces.");
            return;
        }

        if (!isValidName(breed)) {
            showErrorAlert("Invalid breed name. Please enter only letters and spaces.");
            return;
        }

        // Check for duplicate horse ID
        if (isDuplicateHorseId(horseId)) {
            // Show error message if horse ID already exists
            showErrorAlert("Horse with ID " + horseId + " already exists.");
            return;
        }

        // If all validations pass, proceed to add the horse details to the system
        // Save horse details to text file
        try {
            // Get the image path from horseImageView
            if (horseImageView.getImage() != null) {
                imagePath = horseImageView.getImage().getUrl();

                if (imagePath.startsWith("file:/")) {
                    try {
                        String decodedUrl = URLDecoder.decode(imagePath, "UTF-8");
                        String projectPath = System.getProperty("user.dir");
                        imagePath = new File(projectPath).toURI().relativize(new File(decodedUrl.substring(6)).toURI()).getPath();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                showErrorAlert("All fields are required.");
                return;
            }
            saveHorseDetailsToFile(horseId, horseName, jockeyName, Integer.parseInt(ageText), breed, raceRecord, imagePath);
            // Show information alert for successful addition of horse details
            showInformationAlert("Horse details have been successfully added.");
        } catch (IOException e) {
            // Show error alert if there was an issue saving the horse details
            showErrorAlert("An error occurred while saving the horse details. Please try again later.");
            e.printStackTrace(); // Print the exception stack trace for debugging
        }

    }

    public boolean isValidHorseId(String horseId) {
        // Regular expression to match horse ID format
        String regex = "^[A-D][0-9]+$";
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);
        // Match the horse ID against the pattern
        Matcher matcher = pattern.matcher(horseId);
        // Return true if the horse ID matches the pattern, false otherwise
        return matcher.matches();
    }

    private void saveHorseDetailsToFile(String horseId, String horseName, String jockeyName, int age, String breed, String raceRecord, String imagePath) throws IOException {

        // Read all existing horse details from the file and store them in a map
        Map<String, Map<String, String>> horseDetailsMap = readAllHorseDetails();

        // Update the details of the specified horse in the map
        Map<String, String> updatedHorseDetails = new HashMap<>();
        updatedHorseDetails.put("Horse ID", horseId);
        updatedHorseDetails.put("Horse Name", horseName);
        updatedHorseDetails.put("Jockey Name", jockeyName);
        updatedHorseDetails.put("Age", String.valueOf(age));
        updatedHorseDetails.put("Breed", breed);
        updatedHorseDetails.put("Race Record", raceRecord);
        updatedHorseDetails.put("Image Path", imagePath);
        System.out.println(imagePath);
        horseDetailsMap.put(horseId, updatedHorseDetails);

        // Write all the horse details back to the file
        writeAllHorseDetails(horseDetailsMap);

    }
    public boolean isDuplicateHorseId(String horseId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("horse_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\s*Horse ID\\s*:\\s*.+")) {
                    String existingHorseId = line.substring(line.indexOf(":") + 1).trim(); // Extract the existing horse ID
                    System.out.println("Existing Horse ID: " + existingHorseId); // Debug print
                    if (existingHorseId.equals(horseId)) {
                        return true; // Horse ID already exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return false; // Horse ID does not exist
    }

    public void ClearDetails(ActionEvent actionEvent) {
        // Reset all text fields to empty strings
        horseIdField.setText("");
        horseNameField.setText("");
        jockeyNameField.setText("");
        ageField.setText("");
        breedField.setText("");
        raceRecordField.setText("");
        horseImageView.setImage(null);
    }

    @FXML
    public void PreviousStage(MouseEvent mouseEvent) {
        GotoPreviousStage(mouseEvent);
    }

    @FXML
    private void chooseImage(ActionEvent event) {
        chooseImageforUpdate(event, horseImageView);
    }

    private static Map<String, Map<String, String>> readAllHorseDetails() {
        return readHorseDetails();
    }

    private static void writeAllHorseDetails(Map<String, Map<String, String>> horseDetailsMap) {
        WriteHorseDetails(horseDetailsMap, FIELD_ORDER);
    }
}
