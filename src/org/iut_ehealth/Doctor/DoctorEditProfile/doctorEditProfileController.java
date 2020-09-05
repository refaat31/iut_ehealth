package org.iut_ehealth.Doctor.DoctorEditProfile;

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
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

import java.text.SimpleDateFormat;
import java.util.Date;

public class doctorEditProfileController {


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

        String query = "SELECT image from userdoctorinfo WHERE doctorid=?";
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
            sql3 = "SELECT * from userdoctor where doctorid = ?";
            PreparedStatement pst3 = myConn.prepareStatement(sql3);
            pst3.setString(1,userSession.getUsername());

            //System.out.print("b");
            ResultSet rs3  = pst3.executeQuery();
            rs3.next();
            passwordField.setText(rs3.getString("doctorpassword"));
            confirmpasswordField.setText(rs3.getString("doctorpassword"));

            p2=String.valueOf(passwordField.getText());
            c2=String.valueOf(confirmpasswordField.getText());

            String sql;
            //System.out.print("a");
            // sql = "select * from userstudentinfo where studentid = '"+id+"'";
            sql = "SELECT * from userdoctorinfo where doctorid = ?";
            pst = myConn.prepareStatement(sql);
            pst.setString(1,userSession.getUsername());

            //System.out.print("b");
            rs  = pst.executeQuery();
            rs.next();
            // name = rs.getString("studentname");
            nameField.setText(rs.getString("doctorname"));
            age_init = rs.getString("doctorage");
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
            idField.setText(rs.getString("doctorid"));
            bloodGroupField.setText(rs.getString("doctorbg"));
            departmentField.setText(rs.getString("doctorspeciality"));
            contactnumberField.setText(rs.getString("doctorcontact"));
            emailField.setText(rs.getString("doctoremail"));
            resident = rs.getString("doctorres");
            // System.out.println(resident);
            if(resident.equals("Residential")){
                ResidentialButton.setSelected(true);
            }
            else{
                NonResButton.setSelected(true);
            }
            addressField.setText(rs.getString("doctoraddress"));

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
    public void onSlipRequestsClick(ActionEvent actionEvent) {
        Parent doctorSlipRequests = null;
        try {
            doctorSlipRequests = FXMLLoader.load(getClass().getResource("../DoctorSlipRequests/doctorSlipRequests.fxml"));
            Scene DoctorSlipRequestsScene = new Scene(doctorSlipRequests);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(DoctorSlipRequestsScene);
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
                String sql2 = "update userdoctor set doctorpassword = '" + p + "' where doctorid = '" + id_no + "'";
                PreparedStatement pst = myConn.prepareStatement(sql2);
                pst.execute();
                //String sql = "update userstudentinfo set studentname = '" + name + "' ,studentage ='" + age + "',studentbg='" + bg + "',studentdept='" + dept + "',studentcontact='" + contact + "',studentemail='" + email + "',studentaddress='" + address + "',studentres='" + stat + "'where studentid='" + id_no + "'";
                String sql = "update userdoctorinfo set doctorname = '" + name +"',doctorcontact='" + contact + "',doctoremail='" + email + "',doctoraddress='" + address + "',doctorres='" + stat + "'where doctorid='" + id_no + "'";
                // PreparedStatement pst;
                //   PreparedStatement pst = myConn.prepareStatement(sql);
                //  try {
                //pst = cn.prepareStatement(sql);
                // pst.execute();
                PreparedStatement pst2 = myConn.prepareStatement(sql);
                pst2.execute();


                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                //login confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene updateSuccess = new Scene(FXMLLoader.load(getClass().getResource("doctorEditProfileSuccess.fxml")));
                dialog.setScene(updateSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();

                Parent DoctorHomepage;
                DoctorHomepage = FXMLLoader.load(getClass().getResource("../DoctorHomepage/doctorHomepage.fxml"));
                Scene DoctorHomepageScene = new Scene(DoctorHomepage);

                window.setScene(DoctorHomepageScene);
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
        doctorButton.setSelected(true);
        teacherButton.setSelected(false);
        studentButton.setSelected(false);
    }

    public void onTeacherButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(true);
        teacherButton.setSelected(false);
        studentButton.setSelected(false);
    }

    public void onDoctorButtonChosen(ActionEvent actionEvent) {
        doctorButton.setSelected(true);
        teacherButton.setSelected(false);
        studentButton.setSelected(false);
    }
    public void onNonResButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(true);
        ResidentialButton.setSelected(false);
    }
    public void onResidentialButtonChosen(ActionEvent actionEvent) {
        NonResButton.setSelected(false);
        ResidentialButton.setSelected(true);
    }




    public void onBillRequestsClick(ActionEvent actionEvent) {
        Parent doctorBillRequests = null;
        try {
            doctorBillRequests = FXMLLoader.load(getClass().getResource("../DoctorBillRequests/doctorBillRequests.fxml"));
            Scene DoctorBillRequestsScene = new Scene(doctorBillRequests);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(DoctorBillRequestsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
    }

    public void onSeeAppointmentClick(ActionEvent actionEvent) {
        Parent doctorAppointment = null;
        try {
            doctorAppointment = FXMLLoader.load(getClass().getResource("../DoctorAppointment/doctorAppointment.fxml"));
            Scene doctorAppointmentScene = new Scene(doctorAppointment);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(doctorAppointmentScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
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
