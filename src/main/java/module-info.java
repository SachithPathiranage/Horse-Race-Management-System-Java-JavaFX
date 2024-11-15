module org.example.horse_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.horse_management_system to javafx.fxml;
    exports org.example.horse_management_system;
}