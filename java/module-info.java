module com.example.project_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.project_2 to javafx.fxml;
    exports com.example.project_2;
}