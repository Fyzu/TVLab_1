package vstu.lab;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import vstu.lab.controllers.MathCombinatoricsController;

/**
 * TVLab_1 created by Dmitry on 19.02.2016.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;

    /**
     * Initializes the root layout.
     */
    public void initMainLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/MathCombinatorics.fxml"));
            mainLayout = (BorderPane) loader.load();

            // Show the scene containing the main layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
           // MathCombinatoricsController controller = loader.getController();

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Math Combinatorics Tool");
        this.primaryStage.setMinWidth(850);
        this.primaryStage.setMinHeight(550);
        //this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

        initMainLayout();
    }
}