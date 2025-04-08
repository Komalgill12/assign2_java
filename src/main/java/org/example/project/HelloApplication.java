package org.example.project;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelloApplication extends Application {

    private String apiKey = "";

    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/project/hello-view.fxml"));

        if (fxmlLoader.getLocation() == null) {
            System.out.println("Error: FXML file not found!");
            return;
        }

        VBox root = fxmlLoader.load();
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Load the API Key
        loadApiKey();
    }

    private void loadApiKey() {
        try (InputStream input = getClass().getResourceAsStream("/org/example/project/config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            Properties properties = new Properties();
            properties.load(input);
            apiKey = properties.getProperty("nytimes.apiKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
