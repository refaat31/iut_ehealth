package org.iut_ehealth.Doctor.DoctorAppointment;

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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.iut_ehealth.DatabaseConnection;
import org.iut_ehealth.Student.StudentsAppointments.appointmentModel;
import org.iut_ehealth.UserSession;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class doctorAppointmentController {
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
    @FXML
    private JFXTreeTableView appointmentsListView ;
    ObservableList<appointmentModel> appointmentsList;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize() throws SQLException, IOException {
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

        query = "SELECT st_id,time,problem,day,month,year from appointment ";
        pst = myConn.prepareStatement(query);
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
                appointmentsList.add(new org.iut_ehealth.Student.StudentsAppointments.appointmentModel(rs.getString("st_id"),rs.getString("time"),rs.getString("problem"),rs.getString("day"),rs.getString("month"),rs.getString("year")));
        }
        pst.close();
        rs.close();

        st_id.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("st_id")
        );
        time.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("time")
        );
        problem.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("problem")
        );
        day.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("day")
        );
        month.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("month")
        );
        year.setCellValueFactory(
                new TreeItemPropertyValueFactory<org.iut_ehealth.Student.StudentsAppointments.appointmentModel,String>("year")
        );


        TreeItem<appointmentModel> root = new RecursiveTreeItem<>(appointmentsList, RecursiveTreeObject::getChildren);
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

    public void uploadImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "UPDATE userdoctorinfo SET image=? WHERE doctorid=?";
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
}
