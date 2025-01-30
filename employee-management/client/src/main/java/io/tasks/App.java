package io.tasks;

import io.tasks.fxml.AddPageController;
import io.tasks.fxml.DeletePageController;
import io.tasks.managers.Director;
import io.tasks.fxml.MainPageController;
import io.tasks.fxml.UpdatePageController;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
    private Parent mainPageParent;
    private MainPageController mainPageController;
    
    private Parent addPageParent;
    private AddPageController addPageController;
    
    private Parent updatePageParent;
    private UpdatePageController updatePageController;
    
    private Parent deletePageParent;
    private DeletePageController deletePageController;
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Management App");
        
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        
        stage.setWidth(800);
        stage.setHeight(600);
        
    	final String mainPageName = "/MainPage.fxml";
        final String addPageName = "/AddPage.fxml";
        final String updatePageName = "/UpdatePage.fxml";
        final String deletePageName = "/DeletePage.fxml";
        
        loadMainPage(mainPageName);
        loadAddPage(addPageName);
        loadUpdatePage(updatePageName);
        loadDeletePage(deletePageName);
        
        if (mainPageParent == null || mainPageController == null
        ||  addPageParent == null || addPageController == null
        ||  updatePageParent == null || updatePageController == null
        ||  deletePageParent == null || deletePageController == null) {
            Text text = new Text("Could not load some of the resources.");
            Scene fallBackScene = new Scene(new StackPane(text));
            stage.setScene(fallBackScene);
            stage.show();
            return;
        }
    	
        Director mainDirector = new Director(stage,
                mainPageParent, mainPageController,
                addPageParent, addPageController,
                updatePageParent, updatePageController,
                deletePageParent, deletePageController);
        mainDirector.startWorking();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    private void loadMainPage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        try {
            mainPageParent = loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load parent from " + pageName + " file.");
        }
        mainPageController = loader.getController();
        if (mainPageController == null) {
            System.err.println("Could not load controller for " + pageName + " file.");
        }
    }
    
    private void loadAddPage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        try {
            addPageParent = loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load parent from " + pageName + " file.");
        }
        addPageController = loader.getController();
        if (addPageController == null) {
            System.err.println("Could not load controller for " + pageName + " file.");
        }
    }
    
    private void loadUpdatePage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        try {
            updatePageParent = loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load parent from " + pageName + " file.");
        }
        updatePageController = loader.getController();
        if (updatePageController == null) {
            System.err.println("Could not load controller for " + pageName + " file.");
        }
    }
    
    private void loadDeletePage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        try {
            deletePageParent = loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load parent from " + pageName + " file.");
        }
        deletePageController = loader.getController();
        if (deletePageController == null) {
            System.err.println("Could not load controller for " + pageName + " file.");
        }
    }
}