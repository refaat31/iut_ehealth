package org.iut_ehealth.Student.StudentEditProfile;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.UserSession;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;

import java.text.SimpleDateFormat;
import java.util.Date;

public class studentEditProfileController {


    @FXML
    private JFXTextField nameField = new JFXTextField();
    @FXML
    private JFXTextField departmentField = new JFXTextField();
    @FXML
    private JFXTextField idField = new JFXTextField();
    @FXML
    private JFXTextField emailField = new JFXTextField();
    @FXML
    private JFXTextField ageField = new JFXTextField();
    @FXML
    private JFXTextField contactnumberField = new JFXTextField();
    @FXML
    private JFXTextField addressField = new JFXTextField();
    @FXML
    private JFXTextField bloodGroupField = new JFXTextField();
    @FXML
    private JFXPasswordField passwordField = new JFXPasswordField();
    @FXML
    private JFXPasswordField confirmpasswordField = new JFXPasswordField();


    @FXML
    private RadioButton ResidentialButton = new RadioButton();
    @FXML
    private RadioButton NonResButton = new RadioButton();

    @FXML
    private RadioButton studentButton = new RadioButton();
    @FXML
    private RadioButton teacherButton = new RadioButton();
    @FXML
    private RadioButton doctorButton = new RadioButton();


    @FXML
    private JFXButton editProfilenowButton = new JFXButton();



    public String name = new String("");
    public String id_no = new String("");
    public String email = new String("");
    public String age = new String("");
    public String bg = new String("");
    public String dept = new String("");
    public String contact = new String("");
    public String address = new String("");
    public String pass = new String("");
    public String confirm_pass = new String("");
    public String resident = new String("");
    public String p2,c2;

    public String name_init = new String("");
    public String id_init = new String("");
    public String email_init = new String("");
    public String age_init = new String("");
    public String system_year = new String("");
    public String bg_init = new String("");
    public String dept_init = new String("");
    public String contact_init = new String("");
    public String address_init = new String("");
    public String pass_init = new String("");
    public String confirm_pass_init = new String("");
    public String resident_init = new String("");

    @FXML
    private JFXButton logoutButton = new JFXButton();
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

        ageField.setEditable(false);
        idField.setEditable(false);
        departmentField.setEditable(false);
        bloodGroupField.setEditable(false);

        String query = "SELECT image from userstudentinfo WHERE studentid=?";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,userSession.getUsername());

        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            InputStream is = rs.getBinaryStream("image");
            OutputStream os = new FileOutputStream(new File("photo.jpg"));
            byte[] content = new byte[1024];
            int size = 0;
            while((size = is.read(content))!=-1){
                os.write(content,0,size);
            }
            is.close();
            os.close();
            image = new Image("file:photo.jpg",100,150,true,true);
            profilePicture.setImage(image);
            profilePicture.setFitHeight(100);
            profilePicture.setFitWidth(100);
            profilePicture.setPreserveRatio(true);

            String sql3;
            //System.out.print("a");
            // sql = "select * from userstudentinfo where studentid = '"+id+"'";
            sql3 = "SELECT * from userstudent where studentid = ?";
            PreparedStatement pst3 = myConn.prepareStatement(sql3);
            pst3.setString(1,userSession.getUsername());

            //System.out.print("b");
            ResultSet rs3  = pst3.executeQuery();
            rs3.next();
            passwordField.setText(rs3.getString("studentpassword"));
            confirmpasswordField.setText(rs3.getString("studentpassword"));

            p2=String.valueOf(passwordField.getText());
            c2=String.valueOf(confirmpasswordField.getText());

            String sql;
            //System.out.print("a");
            // sql = "select * from userstudentinfo where studentid = '"+id+"'";
            sql = "SELECT * from userstudentinfo where studentid = ?";
            pst = myConn.prepareStatement(sql);
            pst.setString(1,userSession.getUsername());

            //System.out.print("b");
            rs  = pst.executeQuery();
            rs.next();
            // name = rs.getString("studentname");
            nameField.setText(rs.getString("studentname"));
            age_init = rs.getString("studentage");
            // LocalDate today = LocalDate.now();
            //Period p = Period.between(LocalDate.parse(age_init), today);
            //system_year = select extract(year from sysdate) from dual;
            //ageField.setText(String.valueOf(p.getYears()));
            Date dt= new Date();
            String y1,age_n;
            SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
            y1 = df3.format(dt);
            int birth_d,current_d,diff;
            current_d = Integer.parseInt(String.valueOf(y1));
            birth_d = Integer.parseInt(String.valueOf(age_init));
            diff = current_d-birth_d;
            age_n= Integer.toString(diff);
            //System.out.println(birth_d + " " + current_d+ " " + diff+ " " + age_n );
            ageField.setText(age_n);
            idField.setText(rs.getString("studentid"));
            bloodGroupField.setText(rs.getString("studentbg"));
            departmentField.setText(rs.getString("studentdept"));
            contactnumberField.setText(rs.getString("studentcontact"));
            emailField.setText(rs.getString("studentemail"));
            resident = rs.getString("studentres");
            // System.out.println(resident);
            if(resident.equals("Residential")){
                ResidentialButton.setSelected(true);
            }
            else{
                NonResButton.setSelected(true);
            }
            addressField.setText(rs.getString("studentaddress"));

            name_init = String.valueOf(nameField.getText());
            age_init = String.valueOf(ageField.getText());
            bg_init = String.valueOf(bloodGroupField.getText());
            dept_init = String.valueOf(departmentField.getText());
            id_init = String.valueOf(idField.getText());
            email_init = String.valueOf(emailField.getText());
            contact_init = String.valueOf(contactnumberField.getText());
            address_init = String.valueOf(addressField.getText());
            pass_init    = String.valueOf(passwordField.getText());
            confirm_pass_init = String.valueOf(confirmpasswordField.getText());



        }
        pst.close();
        rs.close();
    }
    public void onAppointmentsClick(ActionEvent actionEvent){
        Parent studentAppointments = null;
        try {
            studentAppointments = FXMLLoader.load(getClass().getResource("../StudentsAppointments/studentAppointments.fxml"));
            Scene studentAppointmentsScene = new Scene(studentAppointments);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(studentAppointmentsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onStudentTeacherConnectionClick(ActionEvent actionEvent){
        Parent StudentTeacherConnection = null;
        try {
            StudentTeacherConnection = FXMLLoader.load(getClass().getResource("../StudentTeacherConnection/studentTeacherConnection.fxml"));
            Scene StudentTeacherConnectionScene = new Scene(StudentTeacherConnection);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentTeacherConnectionScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onstudentRefundsClick(ActionEvent actionEvent){
        Parent studentRefunds = null;
        try {
            studentRefunds = FXMLLoader.load(getClass().getResource("../StudentRefunds/studentRefunds.fxml"));
            Scene studentRefundsScene = new Scene(studentRefunds);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(studentRefundsScene);
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


    public void onZoomClick(ActionEvent actionEvent) {

        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("https://zoom.us/"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();

        }
    }

    public void oneditProfileClick(ActionEvent actionEvent) {

        try{
            //   Connection myConn = databaseConnection.getConnectionObject();
            //   Statement myStatement = myConn.createStatement();
            name= String.valueOf(nameField.getText());
            age = String.valueOf(ageField.getText());
            bg= String.valueOf(bloodGroupField.getText());
            dept = String.valueOf(departmentField.getText());
            id_no = String.valueOf(idField.getText());
            email = String.valueOf(emailField.getText());
            contact = String.valueOf(contactnumberField.getText());
            address = String.valueOf(addressField.getText());
            pass    = String.valueOf(passwordField.getText());
            confirm_pass = String.valueOf(confirmpasswordField.getText());

            String stat = "";
            if(ResidentialButton.isSelected())
                stat = "Residential";
            else if(NonResButton.isSelected())
                stat = "Non-Residential";
            String p,c;
            p=String.valueOf(passwordField.getText());
            c=String.valueOf(confirmpasswordField.getText());

            int flag = 0 ;
            if(p.length()==0)
                JOptionPane.showMessageDialog(null, "password field cannot be empty");
            else if(!p.equals(c))
                JOptionPane.showMessageDialog(null, "password and confirm pass must match");
            else if(name.length()==0 || age.length()==0 || bg.length()==0 || dept.length()==0 || id_no.length()==0 || email.length()==0 || contact.length()==0 || address.length()==0 || pass.length()==0 || confirm_pass.length()==0){

                JOptionPane.showMessageDialog(null, "Cannot sign up.Some field are missing");
            }
            else{
                String sql2 = "update userstudent set studentpassword = '" + p + "' where studentid = '" + id_no + "'";
                PreparedStatement pst = myConn.prepareStatement(sql2);
                pst.execute();
                //String sql = "update userstudentinfo set studentname = '" + name + "' ,studentage ='" + age + "',studentbg='" + bg + "',studentdept='" + dept + "',studentcontact='" + contact + "',studentemail='" + email + "',studentaddress='" + address + "',studentres='" + stat + "'where studentid='" + id_no + "'";
                String sql = "update userstudentinfo set studentname = '" + name +"',studentcontact='" + contact + "',studentemail='" + email + "',studentaddress='" + address + "',studentres='" + stat + "'where studentid='" + id_no + "'";
                // PreparedStatement pst;
                //   PreparedStatement pst = myConn.prepareStatement(sql);
                //  try {
                //pst = cn.prepareStatement(sql);
                // pst.execute();
                PreparedStatement pst2 = myConn.prepareStatement(sql);
                pst2.execute();

                Parent StudentHomepage;
                StudentHomepage = FXMLLoader.load(getClass().getResource("../StudentHomepage/studentHomepage.fxml"));
                Scene StudentHomepageScene = new Scene(StudentHomepage);
                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                //login confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene updateSuccess = new Scene(FXMLLoader.load(getClass().getResource("studentEditProfileSuccess.fxml")));
                dialog.setScene(updateSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();

                window.setScene(StudentHomepageScene);
                window.show();
                flag = 1 ;

            }
            if(flag==0){
                JOptionPane.showMessageDialog(null, "Unsuccessful in Updating userinfo.");
                nameField.setText(name_init);
                ageField.setText(age_init);
                bloodGroupField.setText(bg_init);
                departmentField.setText(dept_init);
                idField.setText(id_init);
                emailField.setText(email_init);
                contactnumberField.setText(contact_init);
                addressField.setText(address_init);
                passwordField.setText(p2);
                confirmpasswordField.setText(c2);
            }

        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update Failed.");
        }
    }

    public void onStudentButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(false);
        teacherButton.setSelected(false);
        studentButton.setSelected(true);
    }

    public void onTeacherButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(false);
        teacherButton.setSelected(false);
        studentButton.setSelected(true);
    }

    public void onDoctorButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(false);
        teacherButton.setSelected(false);
        studentButton.setSelected(true);
    }
    public void onNonResButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(true);
        ResidentialButton.setSelected(false);
    }
    public void onResidentialButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(false);
        ResidentialButton.setSelected(true);
    }



    public void onPrescriptionButtonClick(ActionEvent actionEvent){
        Parent StudentPrescription = null;
        try {
            StudentPrescription = FXMLLoader.load(getClass().getResource("../StudentPrescription/studentPrescription.fxml"));
            Scene StudentPrescriptionScene = new Scene(StudentPrescription);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentPrescriptionScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStudentMedicalRecordsClick(ActionEvent actionEvent){
        Parent StudentMedicalRecords = null;
        try {
            StudentMedicalRecords = FXMLLoader.load(getClass().getResource("../StudentMedicalRecords/studentMedicalRecords.fxml"));
            Scene StudentMedicalRecordsScene = new Scene(StudentMedicalRecords);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentMedicalRecordsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "UPDATE userstudentinfo SET image=? WHERE studentid=?";
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
    public void continueHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }


}
