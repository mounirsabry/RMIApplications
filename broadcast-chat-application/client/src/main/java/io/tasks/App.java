package io.tasks;

import io.tasks.fxml.MainPageController;
import io.tasks.managers.Director;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
    Parent mainPageParent;
    MainPageController mainPageController;
    Image connectedDotImage;

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        stage.setWidth(1000);
        stage.setHeight(750);
        
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);
        
        final String mainPageName = "/MainPage.fxml";
        loadMainPage(mainPageName);
        if (mainPageParent == null || mainPageController == null || connectedDotImage == null) {
            Text text = new Text("Could not load some of the resources.");
            Scene scene = new Scene(new StackPane(text));
            stage.setScene(scene);
            stage.show();
            return;
        }
        
        Director mainDirector = new Director(stage,
                mainPageParent, connectedDotImage,
                mainPageController);
        mainDirector.startWorking();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    private void loadMainPage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        try {
            mainPageParent = loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load the main page.");
            ex.printStackTrace();
        }
        mainPageController = loader.getController();
        if (mainPageController == null) {
            System.err.println("Could not load the main page controller.");
        }
        
        InputStream inputStream = getClass().getResourceAsStream("/green1_dot.png");
        if (inputStream == null) {
            System.err.println("Could not find the image file.");
            return;
        }
        connectedDotImage = new Image(inputStream);
        if (connectedDotImage == null) {
            System.out.println("Could not find the image.");
        }
        if (connectedDotImage.isError()) {
            System.err.println("Error in loading the image.");
        }
    }
}