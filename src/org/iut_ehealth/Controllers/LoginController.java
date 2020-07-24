package org.iut_ehealth.Controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.iut_ehealth.App;

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



    public void onLoginClick(ActionEvent actionEvent) {


        //getText() & isSelected()
        String sqlStudent = "select *from userstudent where studentid = '"+emailField.getText()+"'and studentpassword = '"+passwordField.getText()+"'";
        String sqlTeacher = "select *from userteacher where teacherid = '"+emailField.getText()+"'and teacherpassword = '"+passwordField.getText()+"'";
        String sqlDoctor = "select *from userdoctor where doctorid = '"+emailField.getText()+"'and doctorpassword = '"+passwordField.getText()+"'";
        //connecting to db
        //redundant code
        //try to connect once
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12353692";
        String username = "sql12353692";
        String password="NruRn74dY6";

        try{
            Connection myConn = DriverManager.getConnection(url,username,password);
            Statement myStatement = myConn.createStatement();

            if(studentButton.isSelected()) {
                ResultSet rs = myStatement.executeQuery(sqlStudent);
                if(rs.next()){
                    System.out.println(emailField.getText());
                    System.out.println(passwordField.getText());

                    //the scene that we want to load
                    Parent studentHomepage = FXMLLoader.load(getClass().getResource("../Resources/studentHomepage.fxml"));
                    Scene studentHomepageScene = new Scene(studentHomepage);

                    //this line gets stage information
                    Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                    
                    window.setScene(studentHomepageScene);
                    window.show();
                }
                else System.out.println("invalid username/password");
            }
            else if(teacherButton.isSelected()) {
                ResultSet rs = myStatement.executeQuery(sqlTeacher);
                if(rs.next()){
                    System.out.println(emailField.getText());
                    System.out.println(passwordField.getText());
                }
                else System.out.println("invalid username/password");
            }
            else if(doctorButton.isSelected()) {
                ResultSet rs = myStatement.executeQuery(sqlDoctor);
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
