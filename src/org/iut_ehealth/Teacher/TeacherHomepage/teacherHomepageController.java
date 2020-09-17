package org.iut_ehealth.Teacher.TeacherHomepage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.UserSession;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class teacherHomepageController  {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton seeYellowSlipsButton = new JFXButton();
    @FXML
    private JFXTextArea selectedFilePath = new JFXTextArea();
    private FileChooser fileChooser = new FileChooser();


    private File file ;
    private Desktop desktop = Desktop.getDesktop();

    @FXML
    private ImageView profilePicture = new ImageView();
    private Image image;

    private FileInputStream fis;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize() throws SQLException, IOException {
        String query = "SELECT image from userteacherinfo WHERE teacherid=?";

        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,userSession.getUsername());
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            try {
                InputStream is = rs.getBinaryStream("image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                is.close();
                os.close();
                image = new Image("file:photo.jpg", 100, 150, true, true);
                profilePicture.setImage(image);
                profilePicture.setFitHeight(100);
                profilePicture.setFitWidth(100);
                profilePicture.setPreserveRatio(true);
            }catch(Exception  e){
                query = "SELECT image from dp where 1";
                pst = myConn.prepareStatement(query);
                rs = pst.executeQuery();
                rs.next();
                InputStream is = rs.getBinaryStream("image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                is.close();
                os.close();
                image = new Image("file:photo.jpg", 100, 150, true, true);
                profilePicture.setImage(image);
                profilePicture.setFitHeight(100);
                profilePicture.setFitWidth(100);
                profilePicture.setPreserveRatio(true);
            }
        }
        pst.close();
        rs.close();
    }

    public void onEditProfileClick(ActionEvent actionEvent){
        Parent teacherEditProfile = null;
        try {
            teacherEditProfile = FXMLLoader.load(getClass().getResource("../TeacherEditProfile/teacherEditProfile.fxml"));
            Scene teacherEditProfileScene = new Scene(teacherEditProfile);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(teacherEditProfileScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        //the scene that we want to load
        Parent LoginController = null;
        try {
            LoginController = FXMLLoader.load(getClass().getResource("../../Login/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene LoginControllerScene = new Scene(LoginController);

        //this line gets stage information
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(LoginControllerScene);
        window.show();
    }
    public void onSeeYellowSlipsClick(ActionEvent actionEvent){
        //the scene that we want to load
        Parent TeacherYellowSlipsController = null;
        try {
            TeacherYellowSlipsController = FXMLLoader.load(getClass().getResource("../TeacherYellowSlips/teacherYellowSlips.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene TeacherYellowSlipsControllerScene = new Scene(TeacherYellowSlipsController);

        //this line gets stage information
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(TeacherYellowSlipsControllerScene);
        window.show();
    }
    public void uploadImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "UPDATE userteacherinfo SET image=? WHERE teacherid=?";
        PreparedStatement pst = myConn.prepareStatement(query);

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pst.setBinaryStream(1,(InputStream)fis,(int)file.length());
        pst.setString(2,userSession.getUsername());
        pst.execute();
    }

    public void browseHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files (*.jpg,*.png)","*.jpg","*.png")
        );
        file = fileChooser.showOpenDialog(window);
        if(file!=null){
            selectedFilePath.setText(file.getAbsolutePath());
            image = new Image(file.toURI().toString(),100,150,true,true); //prefheight,prefwidth,preserveRatio,Smooth
            profilePicture.setImage(image);
            profilePicture.setFitHeight(100);
            profilePicture.setFitWidth(100);
            profilePicture.setPreserveRatio(true);
        }
        else selectedFilePath.setText("No file selected");
    }


}
