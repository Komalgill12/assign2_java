package org.example.project;



import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;  // Import Properties class
import javafx.application.Platform;


public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Text articleDetailsText;
package org.example.project;



import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;  // Import Properties class
import javafx.application.Platform;


public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Text articleDetailsText;
package org.example.project;



import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;  // Import Properties class
import javafx.application.Platform;


public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Text articleDetailsText;
    private String apiKey;

    public void initialize() {
        // Load API key from config.properties file
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
            System.out.println("API Key Loaded in Controller: " + apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        if (!searchTerm.isEmpty()) {
            System.out.println("Searching for: " + searchTerm);  // Debugging line
            listView.getItems().clear();
            fetchArticles(searchTerm);
        } else {
            System.out.println("Search term is empty.");
        }
    }




    private void fetchArticles(String searchTerm) {
        String urlString = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + searchTerm + "&api-key=" + apiKey;

        try {
            // Create a URL connection
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Check the response code
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                System.out.println("Error: API request failed with status code " + statusCode);
                return;
            }

            // Read the response
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Print the raw response
            String responseJson = responseBuilder.toString();
            System.out.println("Raw response: " + responseJson);  // Debugging line to print raw API response

            // Parse the response JSON
            Gson gson = new Gson();
            NyTimesResponse response = gson.fromJson(responseJson, NyTimesResponse.class);

            // Check if response is null or empty
            if (response == null || response.getResponse().getDocs().length == 0) {
                System.out.println("No articles found for the search term.");
                return;
            }

            // Use Platform.runLater to update the UI on the JavaFX thread
            Platform.runLater(() -> {
                // Clear the ListView before adding new items
                listView.getItems().clear();

                // Add articles to ListView
                Arrays.stream(response.getResponse().getDocs())
                        .forEach(doc -> listView.getItems().add(doc.getHeadline().getMain()));

                // Set up the ListView click event for article details
                listView.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        String selectedTitle = listView.getSelectionModel().getSelectedItem();
                        fetchArticleDetails(selectedTitle);
                    }
                });
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fetchArticleDetails(String title) {
        // For simplicity, we'll just show the title as details
        articleDetailsText.setText("Details for: " + title);
    }

    // Response model for parsing JSON
    class NyTimesResponse {
        private Response response;

        public Response getResponse() {
            return response;
        }

        class Response {
            private Doc[] docs;

            public Doc[] getDocs() {
                return docs;
            }
        }

        class Doc {
            private Headline headline;

            public Headline getHeadline() {
                return headline;
            }

            class Headline {
                private String main;

                public String getMain() {
                    return main;
                }
            }
        }
    }
}

