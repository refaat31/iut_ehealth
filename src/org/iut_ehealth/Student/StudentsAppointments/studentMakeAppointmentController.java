package org.iut_ehealth.Student.StudentsAppointments;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class studentMakeAppointmentController implements Initializable {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton editProfileButton = new JFXButton();
    @FXML
    private JFXButton MakeAnAppointmentButton = new JFXButton();
    @FXML
    private JFXTextArea selectedFilePath = new JFXTextArea();
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private JFXTextField problemField = new JFXTextField();
    @FXML
    private JFXComboBox<String> dayField = new JFXComboBox<>();
    @FXML
    private JFXComboBox<String> monthField = new JFXComboBox<>();
    @FXML
    private JFXComboBox<String> timeField = new JFXComboBox<>();

    private File file ;
    private Desktop desktop = Desktop.getDesktop();

    @FXML
    private ImageView profilePicture = new ImageView();
    private Image image;

    private FileInputStream fis;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize(URL url, ResourceBundle rb) {

        try {
            String query = "SELECT image from userstudentinfo WHERE studentid=?";
            PreparedStatement pst = null;
            pst = myConn.prepareStatement(query);
            pst.setString(1,userSession.getUsername());

            ResultSet rs = pst.executeQuery();
            if(rs.next()){

                try{
                    InputStream is = rs.getBinaryStream("image");
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while((size = is.read(content))!=-1){
                        os.write(content,0,size);
                    }
                    is.close();
                    os.close();
                }catch(FileNotFoundException e){

                }catch(IOException e){
                    
                }


                image = new Image("file:photo.jpg",100,150,true,true);
                profilePicture.setImage(image);
                profilePicture.setFitHeight(100);
                profilePicture.setFitWidth(100);
                profilePicture.setPreserveRatio(true);
            }
            pst.close();
            rs.close();
            monthField.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
            timeField.getItems().addAll("08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00");
            dayField.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9","10","11", "12", "13", "14", "15", "16", "17", "18", "19","20","21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
    public void onMakeAnAppointmentClick(ActionEvent actionEvent){
        Date dt= new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("dd");
        String y1,m1,d1;
        d1=df1.format(dt);
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        m1=df2.format(dt);
        SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
        y1=df3.format(dt);
        String d,m,y;
        d=(String)dayField.getValue();
        m=(String)monthField.getValue();
        int flag=0;
        if(Integer.parseInt(m)<Integer.parseInt(m1)){
            flag=1;
        }
        if(Integer.parseInt(m)==Integer.parseInt(m1)&&Integer.parseInt(d)<Integer.parseInt(d1)){
            flag=1;
        }
        String sql;
        String sq2 ="select *from appointment where day = '"+(String)dayField.getValue()+"'and month = '"+(String)monthField.getValue()+"' and time = '"+(String)timeField.getValue()+"'";

        PreparedStatement pst = null;
        try {
            pst = myConn.prepareStatement(sq2);
            ResultSet rss=pst.executeQuery(sq2);
            if(rss.next()){
                //JOptionPane.showMessageDialog(null, "Time slot filled. Try again using different time");
            }
            else if(Integer.parseInt(m)==2 ||Integer.parseInt(m)==4 ||Integer.parseInt(m)==6 ||Integer.parseInt(m)==9 ||Integer.parseInt(m)==11 ){
                if(Integer.parseInt(d)>30 || (Integer.parseInt(d)>28 && Integer.parseInt(m)==2)){
                    //JOptionPane.showMessageDialog(null, "Invalid Date Input.");
                }
                else{
                    sql = "CALL make_appointment('"+userSession.getUsername()+"','"+(String)timeField.getValue()+"','"+problemField.getText()+"','"+(String)dayField.getValue()+"','"+(String)monthField.getValue()+"','"+y1+"')";
                    PreparedStatement pst2 = myConn.prepareStatement(sql);
                    pst2.execute();
                    //JOptionPane.showMessageDialog(null, "Your Appointment has been made.");
                }
            }
            else if(flag==1){
                //JOptionPane.showMessageDialog(null, "You have entered a date in the past.");
            }
            else{
                /*sql = "insert into appointment values (?,?,?,?,?,?)";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, u.id);
                pst.setString(2, time.getSelectedItem().toString());
                pst.setString(3, problem.getText());
                pst.setString(4, day.getSelectedItem().toString());
                pst.setString(5, month.getSelectedItem().toString());
                pst.setString(6, y1);
                pst.execute();*/
                sql = "CALL make_appointment('"+userSession.getUsername()+"','"+(String)timeField.getValue()+"','"+problemField.getText()+"','"+(String)dayField.getValue()+"','"+(String)monthField.getValue()+"','"+y1+"')";
                PreparedStatement pst2 = myConn.prepareStatement(sql);
                pst2.execute();
                //JOptionPane.showMessageDialog(null, "Your Appointment has been made.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
