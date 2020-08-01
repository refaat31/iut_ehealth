package org.iut_ehealth.Signup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class SignupController {
    @FXML
    private JFXTextField emailField = new JFXTextField();
    @FXML
    private JFXPasswordField passwordField = new JFXPasswordField();
    @FXML
    private JFXPasswordField confirmpasswordField = new JFXPasswordField();
    @FXML
    private RadioButton studentButton = new RadioButton();
    @FXML
    private RadioButton teacherButton = new RadioButton();
    @FXML
    private RadioButton doctorButton = new RadioButton();
    @FXML
    private RadioButton ResidentialButton = new RadioButton();
    @FXML
    private RadioButton NonResButton = new RadioButton();
    @FXML
    private RadioButton DocTeachButton = new RadioButton();
    @FXML
    private JFXButton signupButton = new JFXButton();
    @FXML
    private JFXTextField nameField = new JFXTextField();
    @FXML
    private JFXTextField idField = new JFXTextField();
    @FXML
    private JFXTextField addressField = new JFXTextField();
    @FXML
    private JFXTextField contactnumberField = new JFXTextField();
    @FXML
    private JFXComboBox ageField = new JFXComboBox();
    @FXML
    private JFXComboBox bloodgroupField = new JFXComboBox();
    @FXML
    private JFXComboBox departmentField = new JFXComboBox();


    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

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
    public void onResidentialButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(false);
        DocTeachButton.setSelected(false);
    }
    public void onNonResButtonChosen(ActionEvent actionEvent) {
        ResidentialButton.setSelected(false);
        DocTeachButton.setSelected(false);
    }
    public void onDocTeachButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(false);
        ResidentialButton.setSelected(false);
    }


    public void continueHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
