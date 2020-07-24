package org.iut_ehealth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




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

        //connecting to db
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12353692";
        String username = "sql12353692";
        String password="NruRn74dY6";
        try{
            Connection myConn = DriverManager.getConnection(url,username,password);
            System.out.println("Connection successful");
        }catch (Exception e){
            e.printStackTrace();
        }
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Resources/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}