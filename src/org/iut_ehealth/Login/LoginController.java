package org.iut_ehealth.Login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    @FXML
    private JFXTextField emailField = new JFXTextField();
    @FXML
    private JFXPasswordField passwordField = new JFXPasswordField();
    @FXML
    private RadioButton studentButton = new RadioButton();
    @FXML
    private RadioButton teacherButton = new RadioButton();
    @FXML
    private RadioButton doctorButton = new RadioButton();
    @FXML
    private JFXButton loginButton = new JFXButton();
    @FXML
    private JFXButton signupButton = new JFXButton();
    @FXML
    private JFXButton commonDiseasesInfoButton = new JFXButton();



    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    public void onSignupClicked(ActionEvent actionEvent){
        try {
            Parent signuppage = FXMLLoader.load(getClass().getResource("../Signup/signup.fxml"));
            Scene signuppageScene = new Scene(signuppage);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(signuppageScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onCommonDiseasesInfoClick(ActionEvent actionEvent){
        try {
            Parent commonDiseasesInfoPage = FXMLLoader.load(getClass().getResource("../CommonDiseasesInfo/commonDiseasesInfo.fxml"));
            Scene commonDiseasesInfoScene = new Scene(commonDiseasesInfoPage);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(commonDiseasesInfoScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onLoginClick(ActionEvent actionEvent) {


        //getText() & isSelected()


        try{
            Connection myConn = databaseConnection.getConnectionObject();
            Statement myStatement = myConn.createStatement();

            if(studentButton.isSelected()) {
                String studentQuery = "select *from userstudent where studentid = '"+emailField.getText()+"'and studentpassword = '"+passwordField.getText()+"'";
                ResultSet rs = myStatement.executeQuery(studentQuery);
                if(rs.next()){
//                    System.out.println(emailField.getText());
//                    System.out.println(passwordField.getText());
                    userSession.setUsername(emailField.getText());
                    userSession.setUsertype("student");
                    //the scene that we want to load
                    Parent studentHomepage = FXMLLoader.load(getClass().getResource("../Student/StudentHomepage/studentHomepage.fxml"));
                    Scene studentHomepageScene = new Scene(studentHomepage);


                    //this line gets stage information
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                    //login confirmation
                    Stage dialog = new Stage();
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginSuccess.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();

                    window.setScene(studentHomepageScene);
                    window.show();
                }
                else{
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                    Stage dialog = new Stage();
                    System.out.println("invalid username/password");
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginFailed.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();
                }
            }
            else if(teacherButton.isSelected()) {
                String teacherQuery = "select *from userteacher where teacherid = '"+emailField.getText()+"'and teacherpassword = '"+passwordField.getText()+"'";
                ResultSet rs = myStatement.executeQuery(teacherQuery);
                if(rs.next()){
                    userSession.setUsername(emailField.getText());
                    userSession.setUsertype("teacher");
                    //the scene that we want to load
                    Parent teacherHomepage = FXMLLoader.load(getClass().getResource("../Teacher/TeacherHomepage/teacherHomepage.fxml"));
                    Scene teacherHomepageScene = new Scene(teacherHomepage);
                    //this line gets stage information
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                    //login confirmation
                    Stage dialog = new Stage();
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginSuccess.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();

                    window.setScene(teacherHomepageScene);
                    window.show();
                }
                else {
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                    Stage dialog = new Stage();
                    System.out.println("invalid username/password");
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginFailed.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();
                }
            }
            else if(doctorButton.isSelected()) {
                String doctorQuery = "select *from userdoctor where doctorid = '"+emailField.getText()+"'and doctorpassword = '"+passwordField.getText()+"'";
                ResultSet rs = myStatement.executeQuery(doctorQuery);
                if(rs.next()){
                    userSession.setUsername(emailField.getText());
                    userSession.setUsertype("doctor");
                    //the scene that we want to load
                    Parent doctorHomepage = FXMLLoader.load(getClass().getResource("../Doctor/DoctorHomepage/DoctorHomepage.fxml"));
                    Scene doctorHomepageScene = new Scene(doctorHomepage);
                    //this line gets stage information
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                    //login confirmation
                    Stage dialog = new Stage();
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginSuccess.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();

                    window.setScene(doctorHomepageScene);
                    window.show();
                }
                else {
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                    Stage dialog = new Stage();
                    System.out.println("invalid username/password");
                    dialog.initOwner(window);
                    dialog.setHeight(250);
                    dialog.setWidth(500);
                    Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginFailed.fxml")));
                    dialog.setScene(loginSuccess);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();
                }
            }
            else {
                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                Stage dialog = new Stage();
                System.out.println("invalid username/password");
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/loginFailedType.fxml")));
                dialog.setScene(loginSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            }

           // System.out.println("Connection successful");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onStudentButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(false);
        teacherButton.setSelected(false);
    }

    public void onTeacherButtonChosen(ActionEvent actionEvent) {
        studentButton.setSelected(false);
        doctorButton.setSelected(false);
    }

    public void onDoctorButtonChosen(ActionEvent actionEvent) {
        studentButton.setSelected(false);
        teacherButton.setSelected(false);
    }



    public void continueHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
