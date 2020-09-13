package org.iut_ehealth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(1000);
        stage.setResizable(false);

        Image iutLogo = new Image(new File("src/org/iut_ehealth/icons/iutLogo.png").toURI().toString());
        stage.getIcons().add(iutLogo);
        stage.setTitle("    IUT E-Health System");
        // only connect to db once
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.connectToDatabase();
        Connection myConn = databaseConnection.getConnectionObject();
        System.out.println(myConn);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}