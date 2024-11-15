package org.example.horse_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import static org.example.horse_management_system.Horse.*;
import static org.example.horse_management_system.ValidationUtils.showErrorAlert;
import static org.example.horse_management_system.ValidationUtils.showInformationAlert;

public class UpdateHorse {

    @FXML
    private TextField horseIdField;

    @FXML
    private TextField horseIdTextField;

    @FXML
    private TextField horseNameTextField;

    @FXML
    private TextField jockeyNameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField breedTextField;

    @FXML
    private TextField raceRecordTextField;

    @FXML
    private ImageView horseImageView;
    private static final String[] FIELD_ORDER = {"Horse ID", "Horse Name", "Jockey Name", "Age", "Breed", "Race Record"};


    public void initialize() {
        // Set tooltip for horseIdField
        Tooltip horseIdTooltip = new Tooltip("Enter a Horse ID that starts with A, B, C, or D followed by any number.");
        horseIdField.setTooltip(horseIdTooltip);

        // Set placeholder text
        horseIdField.setPromptText("eg: A1");
        horseNameTextField.setPromptText("Horse Name");
        jockeyNameTextField.setPromptText("Jockey Name");
        ageTextField.setPromptText("Horse Age");
        breedTextField.setPromptText("Horse Breed");

        // Set tooltip for raceRecordField
        Tooltip raceRecordTooltip = new Tooltip("Enter race record in the format: eg: '10 wins, 5 second places, 2 third places'");
        raceRecordTextField.setTooltip(raceRecordTooltip);

        // Set placeholder text for raceRecordField
        raceRecordTextField.setPromptText(" ' ' wins, ' ' second places, ' ' third places");
    }

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
                    horseNameTextField.setText(details[0]);
                    jockeyNameTextField.setText(details[1]);
                    ageTextField.setText(details[2]);
                    breedTextField.setText(details[3]);
                    raceRecordTextField.setText(details[4]);

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
                    horseIdTextField.setText(horseId);
                    horseIdTextField.setEditable(false);
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

        // Clear the horse ID field
        horseIdField.setText("");
    }


    @FXML
    public void onSaveButtonClick() {
        // Retrieve the updated horse details from the text fields
        String horseId = horseIdTextField.getText();
        String horseName = horseNameTextField.getText();
        String jockeyName = jockeyNameTextField.getText();
        String age = ageTextField.getText();
        String breed = breedTextField.getText();
        String raceRecord = raceRecordTextField.getText();
        String imagePath;

        // Save horse details to text file
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
        }
        else {
            showErrorAlert("All fields are required.");
            return;
        }

        // Perform validations
        if (horseId.isEmpty() || horseName.isEmpty() || jockeyName.isEmpty() || age.isEmpty() || breed.isEmpty() || raceRecord.isEmpty()) {
            // Show error message if any field is empty
            showErrorAlert("All fields are required.");
            return;
        }

        if (!ValidationUtils.isValidName(horseName)) {
            showErrorAlert("Invalid horse name. Please enter only letters and spaces.");
            return;
        }

        if (!ValidationUtils.isValidName(jockeyName)) {
            showErrorAlert("Invalid jockey name. Please enter only letters and spaces.");
            return;
        }

        try {
            int agetext = Integer.parseInt(age);
            // Add further validation if needed
        } catch (NumberFormatException e) {
            // Show error message if age is not a valid number
            showErrorAlert("Age must be a valid number.");
            return;
        }

        if (!ValidationUtils.isValidName(breed)) {
            showErrorAlert("Invalid breed name. Please enter only letters and spaces.");
            return;
        }

        // Update the horse details in the text file
        updateHorseDetails(horseId, horseName, jockeyName, age, breed, raceRecord,imagePath);
    }

    public static void updateHorseDetails(String horseId, String horseName, String jockeyName, String age, String breed, String raceRecord,String imagePath) {
        // Read all existing horse details from the file and store them in a map
        Map<String, Map<String, String>> horseDetailsMap = readAllHorseDetails();

        // Update the details of the specified horse in the map
        Map<String, String> updatedHorseDetails = new HashMap<>();
        updatedHorseDetails.put("Horse ID", horseId);
        updatedHorseDetails.put("Horse Name", horseName);
        updatedHorseDetails.put("Jockey Name", jockeyName);
        updatedHorseDetails.put("Age", age);
        updatedHorseDetails.put("Breed", breed);
        updatedHorseDetails.put("Race Record", raceRecord);
        updatedHorseDetails.put("Image Path", imagePath);
        System.out.println(imagePath);
        horseDetailsMap.put(horseId, updatedHorseDetails);

        // Write all the horse details back to the file
        writeAllHorseDetails(horseDetailsMap);

        // Show an information alert
        showInformationAlert("Horse details updated successfully.");
    }

    public static Map<String, Map<String, String>> readAllHorseDetails() {
        return readHorseDetails();
    }


    private static void writeAllHorseDetails(Map<String, Map<String, String>> horseDetailsMap) {
        WriteHorseDetails(horseDetailsMap, FIELD_ORDER);
    }

    @FXML
    private void chooseImage(ActionEvent event) {
        chooseImageforUpdate(event, horseImageView);
    }

    @FXML
    public void PreviousStage(MouseEvent mouseEvent) {
        GotoPreviousStage(mouseEvent);
    }



    public void ClearDetails(ActionEvent actionEvent) {
        // Reset all text fields to empty strings
        horseIdField.setText("");
        horseIdTextField.setText("");
        horseNameTextField.setText("");
        jockeyNameTextField.setText("");
        ageTextField.setText("");
        breedTextField.setText("");
        raceRecordTextField.setText("");
        horseImageView.setImage(null);
    }
}
