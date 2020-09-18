package org.iut_ehealth.Student.StudentTeacherConnection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.UserSession;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Desktop;
import java.net.URI;

public class studentTeacherConnectionController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton ConnectButton = new JFXButton();
    @FXML
    private JFXTextField teacherIDField = new JFXTextField();
    @FXML
    private JFXButton StudentTeacherConnectionButton = new JFXButton();
    @FXML
    private JFXButton editProfileButton = new JFXButton();
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
        String query = "SELECT image from userstudentinfo WHERE studentid=?";
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
    public void onConnectClick(ActionEvent actionEvent) {
        try {
            String sql ="select * from teacher_student_connection where studentid = '"+userSession.getUsername()+"' and teacherid = '"+teacherIDField.getText()+"'" ;
            PreparedStatement pst2 = myConn.prepareStatement(sql);
            ResultSet rss=pst2.executeQuery(sql);
            boolean acflag=false;
            boolean dneflag=true;

            if(rss.next()){
                acflag=true;
                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                //appointment confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/alreadyConnected.fxml")));
                dialog.setScene(loginSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                //JOptionPane.showMessageDialog(null,"Already connected");
                //dispose();
            }
            String sql2="select * from userteacher where teacherid = '"+teacherIDField.getText()+"'" ;
            ResultSet rss1;
            rss1 = pst2.executeQuery(sql2);
            if(rss1.next())
                dneflag=false;
            if(dneflag){
                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                //appointment confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/teacherNotFound.fxml")));
                dialog.setScene(loginSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                //JOptionPane.showMessageDialog(null,"This teacher ID does not exist!");
                //dispose();
            }
            if(!dneflag&&!acflag){
                String sql3 = "insert into teacher_student_connection values (?,?)";
                PreparedStatement pst3;
                pst3 = myConn.prepareStatement(sql3);
                pst3.setString(1, teacherIDField.getText());
                pst3.setString(2, userSession.getUsername());
                pst3.execute();
                Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                //appointment confirmation
                Stage dialog = new Stage();
                dialog.initOwner(window);
                dialog.setHeight(250);
                dialog.setWidth(500);
                Scene loginSuccess = new Scene(FXMLLoader.load(getClass().getResource("Popups/connectionSuccess.fxml")));
                dialog.setScene(loginSuccess);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                //JOptionPane.showMessageDialog(null, "Connection made");
            }


        } catch (SQLException | IOException ex) {

        }

    }

    public void onEditProfileClick(ActionEvent actionEvent){
        Parent studentEditProfile = null;
        try {
            studentEditProfile = FXMLLoader.load(getClass().getResource("../StudentEditProfile/studentEditProfile.fxml"));
            Scene studentEditProfileScene = new Scene(studentEditProfile);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(studentEditProfileScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onMyAppointmentsClick(ActionEvent actionEvent){
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
    public void onStudentRefundsClick(ActionEvent actionEvent){
        Parent studentRefunds = null;
        try {
            studentRefunds = FXMLLoader.load(getClass().getResource("../StudentRefunds/studentRefunds.fxml"));
            Scene StudentRefundsScene = new Scene(studentRefunds);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentRefundsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onZoomClick(ActionEvent actionEvent) {

        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("https://zoom.us/"));
        } catch (IOException | URISyntaxException e) {
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


}
