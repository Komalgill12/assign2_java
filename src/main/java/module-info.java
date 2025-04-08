module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.example.project to javafx.fxml;
    exports org.example.project;
}
