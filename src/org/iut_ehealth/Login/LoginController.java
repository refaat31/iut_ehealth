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
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.UserSession;

import java.sql.Connection;
import java.sql.DriverManager;
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

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

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
                    
                    window.setScene(studentHomepageScene);
                    window.show();
                }
                else System.out.println("invalid username/password");
            }
            else if(teacherButton.isSelected()) {
                String teacherQuery = "select *from userteacher where teacherid = '"+emailField.getText()+"'and teacherpassword = '"+passwordField.getText()+"'";
                ResultSet rs = myStatement.executeQuery(teacherQuery);
                if(rs.next()){
                    System.out.println(emailField.getText());
                    System.out.println(passwordField.getText());
                }
                else System.out.println("invalid username/password");
            }
            else if(doctorButton.isSelected()) {
                String doctorQuery = "select *from userdoctor where doctorid = '"+emailField.getText()+"'and doctorpassword = '"+passwordField.getText()+"'";
                ResultSet rs = myStatement.executeQuery(doctorQuery);
                if(rs.next()){
                    System.out.println(emailField.getText());
                    System.out.println(passwordField.getText());
                }
                else System.out.println("invalid username/password");
            }
            else System.out.println("select a type of user");

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
}
