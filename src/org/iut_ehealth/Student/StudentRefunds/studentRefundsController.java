package org.iut_ehealth.Student.StudentRefunds;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class studentRefundsController {
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
    private JFXTreeTableView refundsListView ;
    ObservableList <refundModel> refundsList;

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

        TreeTableColumn BillNo = new TreeTableColumn("Bill Number");
        TreeTableColumn status = new TreeTableColumn("Status");
        TreeTableColumn amount = new TreeTableColumn("Receivable Amount");
        BillNo.setPrefWidth(250);
        status.setPrefWidth(100);
        amount.setPrefWidth(150);
        refundsListView.getColumns().addAll(BillNo,status,amount);

        refundsList = FXCollections.observableArrayList();

//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("002","pending"));
//        refundsList.add(new refundModel("003","pending"));
//        refundsList.add(new refundModel("007","pending"));
//        refundsList.add(new refundModel("005","pending"));
//        refundsList.add(new refundModel("001","pending"));
//        refundsList.add(new refundModel("001","pending"));

        query = "SELECT BillNo,status,amount from billdatabase where id = ?";
        pst = myConn.prepareStatement(query);
        pst.setString(1,userSession.getUsername());
        rs = pst.executeQuery();
        while(rs.next()){
            refundsList.add(new refundModel(rs.getString("BillNo"),rs.getString("status"),rs.getString("amount")));
        }
        pst.close();
        rs.close();

        BillNo.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModel,String>("BillNo")
        );
        status.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModel,String>("status")
        );
        amount.setCellValueFactory(
                new TreeItemPropertyValueFactory<refundModel,String>("amount")
        );

       TreeItem <refundModel>  root = new RecursiveTreeItem<>(refundsList,RecursiveTreeObject::getChildren);
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

    public void refundImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "SELECT MAX(BillNo) from billdatabase";
        PreparedStatement pst = myConn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.next();
        int last_billNo = rs.getInt("MAX(BillNo)");
        rs.close();

        String query2 = "INSERT into billdatabase (BillNo,id,image,amount) values (?,?,?,?)";
        pst = myConn.prepareStatement(query2);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            refundAlertMessage.setText("Please select a valid file");
        }

        pst.setString(1,Integer.toString(last_billNo+1));
        pst.setString(2,userSession.getUsername());
        pst.setBinaryStream(3,(InputStream)fis,(int)file.length());
        pst.setString(4,"0");
        pst.execute();
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
            image2 = new Image(file.toURI().toString(),400,500,true,true); //prefheight,prefwidth,preserveRatio,Smooth
            refundImage.setImage(image2);
            refundImage.setFitHeight(300);
            refundImage.setFitWidth(400);
            refundImage.setPreserveRatio(true);
        }
        else refundAlertMessage.setText("Please select a file");
    }


    public void getSelectedItem(MouseEvent mouseEvent) throws IOException, SQLException {
        refundModel rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
        showRefundImage(rm.getBillNo());

    }

    public void getSelectedItemKey(KeyEvent keyEvent) throws IOException, SQLException {
       if(keyEvent.getCode().toString()=="UP"||keyEvent.getCode().toString()=="DOWN"){
           refundModel rm = refundsList.get(refundsListView.getSelectionModel().getSelectedIndex());
           showRefundImage(rm.getBillNo());
//           System.out.println(rm.getBillNo());
//           System.out.println(rm.getStatus());
       }

    }

    public void showRefundImage(String BillNumber) throws SQLException, IOException {
        String query = "SELECT BillNo,image from billdatabase WHERE billNo = ? AND id=? ";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,BillNumber);
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


}
