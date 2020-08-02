package org.iut_ehealth.Signup;


import com.jfoenix.controls.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
import java.sql.PreparedStatement;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.net.URL;

public class SignupController implements Initializable {
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
    private JFXButton signupnowButton = new JFXButton();
    @FXML
    private JFXTextField nameField = new JFXTextField();
    @FXML
    private JFXTextField idField = new JFXTextField();
    @FXML
    private JFXTextField addressField = new JFXTextField();
    @FXML
    private JFXTextField contactnumberField = new JFXTextField();
    @FXML
    private JFXComboBox<String> ageField = new JFXComboBox<>();
    @FXML
    private JFXComboBox<String> bloodgroupField = new JFXComboBox<>();
    @FXML
    private JFXComboBox<String> departmentField = new JFXComboBox<>();

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    public void initialize(URL url, ResourceBundle rb){
        departmentField.getItems().addAll("CSE","EEE","MPE","CEE","BTM","TVE");
        bloodgroupField.getItems().addAll("A+","A-","B+","B-","AB+","AB-","O+","O-");
        ageField.getItems().addAll("1940", "1941", "1942", "1943", "1944", "1945", "1946", "1947", "1948", "1949",
                "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959",
                "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969",
                "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979",
                "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
                "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
                "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009");
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

    public void onSignupClick(ActionEvent actionEvent){
        try{
            Connection myConn = databaseConnection.getConnectionObject();
            Statement myStatement = myConn.createStatement();

            String stat,p,c;
            p=String.valueOf(passwordField.getText());
            c=String.valueOf(confirmpasswordField.getText());
            int flag=0;
            if(studentButton.isSelected()){
                String sq2 ="select * from userstudent where studentid = '"+idField.getText()+"'";
                ResultSet rss=myStatement.executeQuery(sq2);
                if(rss.next()){
                    //JOptionPane.showMessageDialog(null, "ID already signed up!");
                    //signupform sf = new signupform();
                    //dispose();
                    flag=1;
                }
            }
            else if(teacherButton.isSelected()){
                String sq2 ="select * from userteacher where teacherid = '"+idField.getText()+"'";
                ResultSet rss=myStatement.executeQuery(sq2);
                if(rss.next()){
                    //JOptionPane.showMessageDialog(null, "ID already signed up!");
                    //signupform sf = new signupform();
                    //dispose();
                    flag=1;
                }
            }
            else{
                String sq2 ="select * from userdoctor where doctorid = '"+idField.getText()+"'";
                ResultSet rss=myStatement.executeQuery(sq2);
                if(rss.next()){
                    //JOptionPane.showMessageDialog(null, "ID already signed up!");
                    //signupform sf = new signupform();
                    //dispose();
                    flag=1;
                }
            }
            if(p.equals(c)&&flag==0){
                String sql,sql2;
                if(studentButton.isSelected()){
                    String sq2 ="select * from userstudent where studentid = '"+idField.getText()+"'";
                    ResultSet rss=myStatement.executeQuery(sq2);
                    if(rss.next()){
                        //JOptionPane.showMessageDialog(null, "ID already signed up!");
                        //signupform sf = new signupform();
                        //dispose();
                    }
                    sql = "insert into userstudentinfo values (?,?,?,?,?,?,?,?,?,?)";
                    sql2= "insert into userstudent values (?,?)";
                }
                else if(teacherButton.isSelected()){
                    sql = "insert into userteacherinfo values (?,?,?,?,?,?,?,?,?,?)";
                    sql2= "insert into userteacher values (?,?)";
                }
                else {
                    sql = "insert into userdoctorinfo values (?,?,?,?,?,?,?,?,?,?)";
                    sql2= "insert into userdoctor values (?,?)";
                }


                PreparedStatement pst = myConn.prepareStatement(sql);
                pst.setString(1, nameField.getText());
                pst.setString(2, (String)ageField.getValue());
                pst.setString(3, (String)bloodgroupField.getValue());
                pst.setString(4, idField.getText());
                pst.setString(5, (String)departmentField.getValue());
                pst.setString(6, contactnumberField.getText());
                pst.setString(7, emailField.getText());
                if(ResidentialButton.isSelected())
                    stat="Residential";
                else if(NonResButton.isSelected())
                    stat="Nonres";
                else
                    stat="Nonapp";
                pst.setString(8, stat);
                pst.setString(9, addressField.getText());
                pst.setString(10, null);
                pst.execute();

                PreparedStatement pst2=myConn.prepareStatement(sql2);
                pst2.setString(1,idField.getText());
                pst2.setString(2,p);
                pst2.execute();

                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                //login confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("./signupSuccess.fxml")));
                dialog.setScene(loginSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                //signinform signinf = new signinform();
                //signinf.setVisible(true);
                //JOptionPane.showMessageDialog(null, "Signup Successful.");
                //dispose();
            }
            else if(flag==1){
                //JOptionPane.showMessageDialog(null, "Try logging in");
            }
            else{
                //JOptionPane.showMessageDialog(null, "Password and Confirmation did not match.");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onBackClick(ActionEvent actionEvent){
        //the scene that we want to load
        Parent LoginController = null;
        try {
            LoginController = FXMLLoader.load(getClass().getResource("../Login/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene LoginControllerScene = new Scene(LoginController);

        //this line gets stage information
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(LoginControllerScene);
        window.show();
    }
    public void continueHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
