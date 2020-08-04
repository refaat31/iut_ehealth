package org.iut_ehealth.Student.StudentsAppointments;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.Student.StudentRefunds.refundModel;
import org.iut_ehealth.UserSession;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class studentAppointmentsController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton editProfileButton = new JFXButton();
    @FXML
    private JFXButton makeappointmentButton = new JFXButton();

    private File file ;
    private Desktop desktop = Desktop.getDesktop();



    private Image image ;
    private Image image2;
    @FXML
    private ImageView profilePicture = new ImageView() ;
    @FXML
    private ImageView refundImage = new ImageView();
    @FXML
    private Label refundAlertMessage = new Label();

    private FileInputStream fis;

    @FXML
    private JFXTreeTableView appointmentsListView ;
    ObservableList <appointmentModel> appointmentsList;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize() throws SQLException, IOException {


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
        }
        pst.close();
        rs.close();

        TreeTableColumn st_id = new TreeTableColumn("st_id");
        TreeTableColumn time = new TreeTableColumn("time");
        TreeTableColumn problem = new TreeTableColumn("problem");
        TreeTableColumn day = new TreeTableColumn("day");
        TreeTableColumn month = new TreeTableColumn("month");
        TreeTableColumn year = new TreeTableColumn("year");
        st_id.setPrefWidth(150);
        time.setPrefWidth(75);
        problem.setPrefWidth(180);
        day.setPrefWidth(75);
        month.setPrefWidth(75);
        year.setPrefWidth(75);
        appointmentsListView.getColumns().addAll(st_id,time,problem,day,month,year);

        appointmentsList = FXCollections.observableArrayList();

        query = "SELECT st_id,time,problem,day,month,year from appointment where st_id = ?";
        pst = myConn.prepareStatement(query);
        pst.setString(1,userSession.getUsername());
        rs = pst.executeQuery();
        String d1,m1,y1;
        Date dt= new Date();
        //System.out.print("a");
        SimpleDateFormat df1 = new SimpleDateFormat("dd");
        d1=df1.format(dt);
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        m1=df2.format(dt);
        SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
        y1=df3.format(dt);
        while(rs.next()){
            if(Integer.parseInt(rs.getString("year"))>Integer.parseInt(y1)||(Integer.parseInt(rs.getString("year"))==Integer.parseInt(y1)&&Integer.parseInt(rs.getString("month"))>Integer.parseInt(m1))||(Integer.parseInt(rs.getString("year"))==Integer.parseInt(y1)&&Integer.parseInt(rs.getString("month"))==Integer.parseInt(m1)&&Integer.parseInt(rs.getString("day"))>=Integer.parseInt(d1)))
                appointmentsList.add(new appointmentModel(rs.getString("st_id"),rs.getString("time"),rs.getString("problem"),rs.getString("day"),rs.getString("month"),rs.getString("year")));
        }
        pst.close();
        rs.close();

        st_id.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("st_id")
        );
        time.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("time")
        );
        problem.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("problem")
        );
        day.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("day")
        );
        month.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("month")
        );
        year.setCellValueFactory(
                new TreeItemPropertyValueFactory<appointmentModel,String>("year")
        );


       TreeItem <appointmentModel>  root = new RecursiveTreeItem<>(appointmentsList,RecursiveTreeObject::getChildren);
        appointmentsListView.setRoot(root);
        appointmentsListView.setShowRoot(false);


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
    public void onRefundsClick(ActionEvent actionEvent){
        Parent StudentRefunds = null;
        try {
            StudentRefunds = FXMLLoader.load(getClass().getResource("../StudentRefunds/studentRefunds.fxml"));
            Scene StudentRefundsScene = new Scene(StudentRefunds);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentRefundsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onMakeAppointmentClick(ActionEvent actionEvent){
        Parent StudentMakeAppointment = null;
        try {
            StudentMakeAppointment = FXMLLoader.load(getClass().getResource("../StudentsAppointments/studentMakeAppointment.fxml"));
            Scene StudentMakeAppointmentScene = new Scene(StudentMakeAppointment);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentMakeAppointmentScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void browseHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

    }
}
