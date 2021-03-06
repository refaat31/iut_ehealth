package org.iut_ehealth.Doctor.DoctorSlipRequests;

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
import org.iut_ehealth.Doctor.DoctorBillRequests.refundModelDoctor;
import org.iut_ehealth.UserSession;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorSlipRequestsController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
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
    private JFXTreeTableView refundsListView ;
    ObservableList<slipModelDoctor> refundsList;

    @FXML
    private JFXTextField studentId = new JFXTextField();
    @FXML
    private JFXTextField amountButton = new JFXTextField();
    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();



    public void initialize() throws SQLException, IOException {
        String query = "SELECT image from userdoctorinfo WHERE doctorid=?";
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
        TreeTableColumn id = new TreeTableColumn("Student Id");
        TreeTableColumn SlipNo = new TreeTableColumn("Slip Number");
        TreeTableColumn status = new TreeTableColumn("Status");
        refundsListView.getColumns().addAll(id,SlipNo,status);

        refundsList = FXCollections.observableArrayList();

//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("002","pending"));
//        refundsList.add(new refundModel("003","pending"));
//        refundsList.add(new refundModel("007","pending"));
//        refundsList.add(new refundModel("005","pending"));
//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("001","pending"));

        query = "SELECT id,SlipNo,status from yellowslip";
        pst = myConn.prepareStatement(query);

        rs = pst.executeQuery();
        while(rs.next()){
            if(rs.getString("status").equals("pending")) {
                refundsList.add(new slipModelDoctor(rs.getString("id"), rs.getString("SlipNo"), rs.getString("status")));
            }
        }
        pst.close();
        rs.close();

        id.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("id")
        );
        SlipNo.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("SlipNo")
        );
        status.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("status")
        );

        TreeItem<slipModelDoctor> root = new RecursiveTreeItem<>(refundsList, RecursiveTreeObject::getChildren);
        refundsListView.setRoot(root);
        refundsListView.setShowRoot(false);

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

    }
    public void refundImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "SELECT MAX(SlipNo) from yellowslip";
        PreparedStatement pst = myConn.prepareStatement(query);
        //manually input the id of the student
        if(studentId.getText().equals("")){
            refundAlertMessage.setText("Please enter the id");
        }

        ResultSet rs = pst.executeQuery();
        rs.next();
        int last_billNo = rs.getInt("MAX(SlipNo)");
        rs.close();

        String query2 = "INSERT into yellowslip (SlipNo,id,status,image) values (?,?,?,?)";
        pst = myConn.prepareStatement(query2);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
           // e.printStackTrace();
            refundAlertMessage.setText("Please select a valid file");
        }

        pst.setString(1,Integer.toString(last_billNo+1));
        pst.setString(2, studentId.getText());
        pst.setString(3,"accepted");
        pst.setBinaryStream(4,(InputStream)fis,(int)file.length());
        pst.execute();
        refundAlertMessage.setText("File uploaded!");
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

    public void getSelectedItem(MouseEvent mouseEvent) throws IOException, SQLException {
        slipModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        showRefundImage(rm.getSlipNo(),rm.getId());

    }

    public void getSelectedItemKey(KeyEvent keyEvent) throws IOException, SQLException {
        if(keyEvent.getCode().toString()=="UP"||keyEvent.getCode().toString()=="DOWN"){
            slipModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
            showRefundImage(rm.getSlipNo(),rm.getId());
//           System.out.println(rm.getBillNo());
//           System.out.println(rm.getStatus());
        }

    }

    public void showRefundImage(String BillNumber,String id) throws SQLException, IOException {
        String query = "SELECT image from yellowslip WHERE SlipNo = ? AND id=? ";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,BillNumber);
        pst.setString(2,id);

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


    public void onBillAccepted(ActionEvent actionEvent) throws SQLException {
        slipModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        updateBillStatus(rm.getSlipNo(),rm.getId(),"accepted");


        Parent doctorBillRequests = null;
        try {
            doctorBillRequests = FXMLLoader.load(getClass().getResource("../DoctorSlipRequests/doctorSlipRequests.fxml"));
            Scene DoctorBillRequestsScene = new Scene(doctorBillRequests);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(DoctorBillRequestsScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBillRejected(ActionEvent actionEvent) throws SQLException {
        slipModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        updateBillStatus(rm.getSlipNo(),rm.getId(),"rejected");
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
    }

    public void updateBillStatus(String BillNo,String id,String status) throws SQLException {
        String query = "CALL update_yellowslip('" + id + "','" + BillNo + "','" + status + "')";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.execute();
        pst.close();


    }
    public void onEditProfileClick(ActionEvent actionEvent){
        Parent doctorEditProfile = null;
        try {
            doctorEditProfile = FXMLLoader.load(getClass().getResource("../DoctorEditProfile/doctorEditProfile.fxml"));
            Scene doctorEditProfileScene = new Scene(doctorEditProfile);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(doctorEditProfileScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
