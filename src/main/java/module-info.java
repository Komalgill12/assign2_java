module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.project to javafx.fxml;
    exports org.example.project;
}