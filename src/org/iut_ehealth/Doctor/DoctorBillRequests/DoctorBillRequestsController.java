package org.iut_ehealth.Doctor.DoctorBillRequests;

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
import org.iut_ehealth.Student.StudentRefunds.refundModel;
import org.iut_ehealth.UserSession;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorBillRequestsController{
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
    private javafx.scene.control.Label refundAlertMessage = new Label();

    private FileInputStream fis;

    @FXML
    private JFXTreeTableView refundsListView ;
    ObservableList<refundModelDoctor> refundsList;

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
        TreeTableColumn id = new TreeTableColumn("Student Id");
        TreeTableColumn BillNo = new TreeTableColumn("Bill Number");
        TreeTableColumn status = new TreeTableColumn("Status");
        TreeTableColumn amount = new TreeTableColumn("Receivable Amount");
//        BillNo.setPrefWidth(250);
//        status.setPrefWidth(150);
        refundsListView.getColumns().addAll(id,BillNo,status,amount);

        refundsList = FXCollections.observableArrayList();

//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("002","pending"));
//        refundsList.add(new refundModel("003","pending"));
//        refundsList.add(new refundModel("007","pending"));
//        refundsList.add(new refundModel("005","pending"));
//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("001","pending"));

        query = "SELECT id,BillNo,status,amount from billdatabase";
        pst = myConn.prepareStatement(query);

        rs = pst.executeQuery();
        while(rs.next()){
            if(rs.getString("status").equals("pending")) {
                refundsList.add(new refundModelDoctor(rs.getString("id"), rs.getString("BillNo"), rs.getString("status"), rs.getString("amount")));
            }
        }
        pst.close();
        rs.close();

        id.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("id")
        );
        BillNo.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("BillNo")
        );
        status.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("status")
        );
        amount.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModelDoctor,String>("amount")
        );

        TreeItem<refundModelDoctor> root = new RecursiveTreeItem<>(refundsList, RecursiveTreeObject::getChildren);
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
        String query = "SELECT MAX(BillNo) from billdatabase";
        PreparedStatement pst = myConn.prepareStatement(query);
        //manually input the id of the student
        if(studentId.getText().equals("")){
            refundAlertMessage.setText("Please enter the id");
        }

        ResultSet rs = pst.executeQuery();
        rs.next();
        int last_billNo = rs.getInt("MAX(BillNo)");
        rs.close();

        String query2 = "INSERT into billdatabase (BillNo,id,status,image,amount) values (?,?,?,?,?)";
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
        pst.setString(5,"0");
        pst.execute();
        refundAlertMessage.setText("File uploaded!");
    }
    public void browseHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files (*.jpg,*.png)","*.jpg","*.png")
        );
        file = fileChooser.showOpenDialog(window);
        if(file!=null){
            selectedFilePath.setText(file.getAbsolutePath());
            image2 = new Image(file.toURI().toString(),100,150,true,true); //prefheight,prefwidth,preserveRatio,Smooth
            refundImage.setImage(image2);
            refundImage.setFitHeight(300);
            refundImage.setFitWidth(400);
            refundImage.setPreserveRatio(true);
        }
        else refundAlertMessage.setText("Please select a file");
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

    public void getSelectedItem(MouseEvent mouseEvent) throws IOException, SQLException {
        refundModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        showRefundImage(rm.getBillNo(),rm.getId());

    }

    public void getSelectedItemKey(KeyEvent keyEvent) throws IOException, SQLException {
        if(keyEvent.getCode().toString()=="UP"||keyEvent.getCode().toString()=="DOWN"){
            refundModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
            showRefundImage(rm.getBillNo(),rm.getId());
//           System.out.println(rm.getBillNo());
//           System.out.println(rm.getStatus());
        }

    }

    public void showRefundImage(String BillNumber,String id) throws SQLException, IOException {
        String query = "SELECT image from billdatabase WHERE billNo = ? AND id=? ";
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
            image2 = new Image("file:refundImage.jpg",100,150,true,true);
            refundImage.setImage(image2);
            refundImage.setFitHeight(300);
            refundImage.setFitWidth(400);
            refundImage.setPreserveRatio(true);
        }
        pst.close();
        rs.close();
    }


    public void onBillAccepted(ActionEvent actionEvent) throws SQLException {
        refundModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        String Amount = amountButton.getText();
        System.out.println(Amount);
        updateBillStatus(rm.getBillNo(),rm.getId(),"accepted",Amount);


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

    public void onBillRejected(ActionEvent actionEvent) throws SQLException {
        refundModelDoctor rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        updateBillStatus(rm.getBillNo(),rm.getId(),"rejected","0");
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

    public void updateBillStatus(String BillNo,String id,String status,String Amount) throws SQLException {
        String query = "UPDATE billdatabase SET status=?,amount = ? WHERE BillNo=? AND id=?";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,status);
        pst.setString(2,Amount);
        pst.setString(3,BillNo);
        pst.setString(4,id);
        System.out.println(Amount);
        System.out.println("upore amount");
        pst.executeUpdate();
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
}
