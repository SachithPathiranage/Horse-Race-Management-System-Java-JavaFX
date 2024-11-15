package org.example.horse_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    @FXML
    private MediaView mediaView;

    public void initialize() {
        // Load the video file
        String videoFile = getClass().getResource("Race.mp4").toExternalForm();

        // Create a Media object
        Media media = new Media(videoFile);

        // Create a MediaPlayer with the Media object
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Set the MediaPlayer to the MediaView
        mediaView.setMediaPlayer(mediaPlayer);

        // Set the MediaPlayer to loop the video
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        });

        // Set the MediaView to preserve aspect ratio
        mediaView.setPreserveRatio(true);

        // Start playing the video
        mediaPlayer.play();
    }

    public void AddDetails(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddHorse.fxml"));
        newStage.setTitle("Add Horse Details");
        newStage.setScene(new Scene(root, 600, 635));
        newStage.show();
        newStage.setResizable(false);

        Stage previousStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        previousStage.close();
    }

    public void UpdateDetails(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("UpdateHorse.fxml"));
        newStage.setTitle("Update Horse Details");
        newStage.setScene(new Scene(root, 600, 650));
        newStage.show();
        newStage.setResizable(false);

        Stage previousStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        previousStage.close();
    }

    public void DeleteDetails(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DeleteHorse.fxml"));
        newStage.setTitle("Delete Horse Details");
        newStage.setScene(new Scene(root, 600, 430));
        newStage.show();
        newStage.setResizable(false);

        Stage previousStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        previousStage.close();
    }

    public void ViewDetails(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ViewHorse.fxml"));
        newStage.setTitle("View Horse Details");

        // Set a fixed width for the scene
        double sceneWidth = 1025;
        // Set a preferred height for the scene (you can adjust this value)
        double preferredHeight = 650;

        newStage.setScene(new Scene(root, sceneWidth, preferredHeight));
        // Prevent resizing of the stage
        newStage.setResizable(false);
        newStage.show();

        // Close the previous stage
        Stage previousStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        previousStage.close();
    }


    public void StartRace(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("StartRace.fxml"));
        newStage.setTitle("Start Race");
        newStage.setScene(new Scene(root, 838, 642));
        newStage.show();
        // Prevent resizing of the stage
        newStage.setResizable(false);

        Stage previousStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        previousStage.close();

    }

    public void Exit() {
        System.exit(0);
    }
}