package org.iut_ehealth.Student.StudentPrescription;

import com.jfoenix.controls.*;
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


public class studentPrescriptionController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton editProfileButton = new JFXButton();
    @FXML
    private JFXTextArea selectedFilePath = new JFXTextArea();
    private FileChooser fileChooser = new FileChooser();


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
    private JFXTreeTableView prescriptionListView ;
    ObservableList <prescriptionModel> prescriptionList;

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

        TreeTableColumn slipNo = new TreeTableColumn("Slip Number");
        TreeTableColumn status = new TreeTableColumn("Status");
        slipNo.setPrefWidth(250);
        status.setPrefWidth(100);
        prescriptionListView.getColumns().addAll(slipNo,status);

        prescriptionList = FXCollections.observableArrayList();

//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("002","pending"));
//        refundsList.add(new refundModel("003","pending"));
//        refundsList.add(new refundModel("007","pending"));
//        refundsList.add(new refundModel("005","pending"));
//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("001","pending"));

        query = "SELECT SlipNo,status from yellowslip where id = ?";
        pst = myConn.prepareStatement(query);
        pst.setString(1,userSession.getUsername());
        rs = pst.executeQuery();
        while(rs.next()){
            prescriptionList.add(new prescriptionModel(rs.getString("slipNo"),rs.getString("status")));
        }
        pst.close();
        rs.close();

        slipNo.setCellValueFactory(
                new TreeItemPropertyValueFactory<prescriptionModel,String>("slipNo")
        );
        status.setCellValueFactory(
                new TreeItemPropertyValueFactory<prescriptionModel,String>("status")
        );
        TreeItem <prescriptionModel>  root = new RecursiveTreeItem<>(prescriptionList,RecursiveTreeObject::getChildren);
        prescriptionListView.setRoot(root);
        prescriptionListView.setShowRoot(false);


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

    public void onZoomClick(ActionEvent actionEvent) {

        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("https://zoom.us/j/8812060673?pwd=M05OUUd1bGQ2Qzd5T2J0Z1psc3o4UT09/"));
        } catch (IOException | URISyntaxException e) {
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

    public void refundImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "SELECT MAX(SlipNo) from yellowslip";
        PreparedStatement pst = myConn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.next();
        int last_slipNo = rs.getInt("MAX(SlipNo)");
        rs.close();

        String query2 = "INSERT into yellowslip (SlipNo,id,image) values (?,?,?)";
        pst = myConn.prepareStatement(query2);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            refundAlertMessage.setText("Please select a valid file");
        }

        pst.setString(1,Integer.toString(last_slipNo+1));
        pst.setString(2,userSession.getUsername());
        pst.setBinaryStream(3,(InputStream)fis,(int)file.length());
        pst.execute();
        Parent studentPrescription = null;
        try {
            studentPrescription = FXMLLoader.load(getClass().getResource("../StudentPrescription/studentPrescription.fxml"));
            Scene StudentPrescriptionScene = new Scene(studentPrescription);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(StudentPrescriptionScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refundAlertMessage.setText("File uploaded!");
    }

    public void browseHandlerPrescription(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files (*.jpg,*.png)","*.jpg","*.png")
        );
        file = fileChooser.showOpenDialog(window);
        if(file!=null){
            selectedFilePath.setText(file.getAbsolutePath());
            image2 = new Image(file.toURI().toString(),400,500,true,true); //prefheight,prefwidth,preserveRatio,Smooth
            refundImage.setImage(image2);
            refundImage.setFitHeight(300);
            refundImage.setFitWidth(400);
            refundImage.setPreserveRatio(true);
        }
        else refundAlertMessage.setText("Please select a file");
    }


    public void getSelectedItem(MouseEvent mouseEvent) throws IOException, SQLException {
        prescriptionModel rm = prescriptionList.get(prescriptionListView.getSelectionModel().getSelectedIndex());
        showRefundImage(rm.getSlipNo());

    }

    public void getSelectedItemKey(KeyEvent keyEvent) throws IOException, SQLException {
        if(keyEvent.getCode().toString()=="UP"||keyEvent.getCode().toString()=="DOWN"){
            prescriptionModel rm = prescriptionList.get(prescriptionListView.getSelectionModel().getSelectedIndex());
            showRefundImage(rm.getSlipNo());
//           System.out.println(rm.getBillNo());
//           System.out.println(rm.getStatus());
        }

    }

    public void showRefundImage(String SlipNumber) throws SQLException, IOException {
        String query = "SELECT SlipNo,image from yellowslip WHERE SlipNo = ? AND id=? ";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,SlipNumber);
        pst.setString(2,userSession.getUsername());

        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            InputStream is = rs.getBinaryStream("image");
            OutputStream os = new FileOutputStream(new File("refundImage.jpg"));
            byte[] content = new byte[1024];
            int size = 0;
            while((size = is.read(content))!=-1){
                os.write(content,0,size);
            }
            is.close();
            os.close();
            image2 = new Image("file:refundImage.jpg",400,500,true,true);
            refundImage.setImage(image2);
            refundImage.setFitHeight(300);
            refundImage.setFitWidth(400);
            refundImage.setPreserveRatio(true);
        }
        pst.close();
        rs.close();
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
