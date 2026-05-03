package hr.javafx.tvz.projektni_zadatak.application;

import hr.javafx.tvz.projektni_zadatak.exceptions.SceneChangeException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * The main class of the Team Pulse application.
 * This class is responsible for launching the JavaFX application and managing scene transitions.
 */
@Slf4j
public class TeamPulseApplication extends Application {

    private static Stage primaryStage;

    /**
     * Called when the application is started.
     * Initializes the primary stage and loads the login scene.
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML or CSS resources cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        log.info("The application has started!");
        registerPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(TeamPulseApplication.class.getResource(
                "/hr/javafx/tvz/projektni_zadatak/loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(
                TeamPulseApplication.class.getResource("/hr/javafx/tvz/projektni_zadatak/styles.css")).toExternalForm());
        stage.setTitle("Team Pulse Application");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(_ -> log.info("The application is closing!"));
    }

    /**
     * Registers the primary stage so it can be accessed statically throughout the application.
     * @param stage the primary stage to be registered
     */
    private static void registerPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Changes the currently displayed scene by loading the specified FXML file.
     * @param fxml the path to the FXML file to load
     * @throws SceneChangeException if the scene cannot be loaded or changed
     */
    public static void changeScene(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TeamPulseApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException exc) {
            log.error("Unable to change scene: {}", exc.getMessage());
            throw new SceneChangeException("Unable to change scene: " + exc.getMessage());
        }
    }

    /**
     * The main entry point of the Java application.
     * Launches the JavaFX application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}