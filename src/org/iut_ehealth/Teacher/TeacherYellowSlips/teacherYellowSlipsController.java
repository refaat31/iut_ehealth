package org.iut_ehealth.Teacher.TeacherYellowSlips;

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

public class teacherYellowSlipsController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton seeYellowSlipsButton = new JFXButton();
    @FXML
    private JFXTextArea selectedFilePath = new JFXTextArea();
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private ImageView yellowSlipsImage = new ImageView();

    private File file ;
    private Desktop desktop = Desktop.getDesktop();

    @FXML
    private ImageView profilePicture = new ImageView();
    private Image image;
    private Image image2;

    private FileInputStream fis;
    @FXML
    private JFXTreeTableView yellowSlipsListView ;
    ObservableList<yellowSlipsModel> yellowSlipsList;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize() throws SQLException, IOException {
        String query = "SELECT image from userteacherinfo WHERE teacherid=?";
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
        TreeTableColumn SlipNo = new TreeTableColumn("SlipNo");
        TreeTableColumn Id = new TreeTableColumn("Id");
        SlipNo.setPrefWidth(250);
        Id.setPrefWidth(150);
        yellowSlipsListView.getColumns().addAll(SlipNo,Id);

        yellowSlipsList = FXCollections.observableArrayList();
        String accept1 = "accepted";
        query = "select * from yellowslip,teacher_student_connection where teacher_student_connection.teacherid = '"+userSession.getUsername()+"' and yellowslip.status = '"+accept1+"'and teacher_student_connection.studentid = yellowslip.id" ;
        pst = myConn.prepareStatement(query);
        rs = pst.executeQuery();
        while(rs.next()){
            yellowSlipsList.add(new yellowSlipsModel(rs.getString("SlipNo"),rs.getString("Id")));
        }
        pst.close();
        rs.close();

        SlipNo.setCellValueFactory(
                new TreeItemPropertyValueFactory<yellowSlipsModel,String>("SlipNo")
        );
        Id.setCellValueFactory(
                new TreeItemPropertyValueFactory<yellowSlipsModel,String>("Id")
        );

        TreeItem<yellowSlipsModel> root = new RecursiveTreeItem<>(yellowSlipsList, RecursiveTreeObject::getChildren);
        yellowSlipsListView.setRoot(root);
        yellowSlipsListView.setShowRoot(false);
    }
    public void getSelectedItem(MouseEvent mouseEvent) throws IOException, SQLException {
        yellowSlipsModel rm = yellowSlipsList.get(yellowSlipsListView.getSelectionModel().getSelectedIndex());
        showYellowSlipsImage(rm.getSlipNo());

    }

    public void getSelectedItemKey(KeyEvent keyEvent) throws IOException, SQLException {
        if(keyEvent.getCode().toString()=="UP"||keyEvent.getCode().toString()=="DOWN"){
            yellowSlipsModel rm = yellowSlipsList.get(yellowSlipsListView.getSelectionModel().getSelectedIndex());
            showYellowSlipsImage(rm.getSlipNo());
//           System.out.println(rm.getBillNo());
//           System.out.println(rm.getStatus());
        }

    }

    public void showYellowSlipsImage(String SlipNumber) throws SQLException, IOException {
        String query = "SELECT SlipNo,image from yellowslip WHERE slipNo = ?";
        PreparedStatement pst = myConn.prepareStatement(query);
        pst.setString(1,SlipNumber);

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
            yellowSlipsImage.setImage(image2);
            yellowSlipsImage.setFitHeight(300);
            yellowSlipsImage.setFitWidth(400);
            yellowSlipsImage.setPreserveRatio(true);
        }
        pst.close();
        rs.close();
    }
    public void onEditProfileClick(ActionEvent actionEvent){
        Parent teacherEditProfile = null;
        try {
            teacherEditProfile = FXMLLoader.load(getClass().getResource("../TeacherEditProfile/teacherEditProfile.fxml"));
            Scene teacherEditProfileScene = new Scene(teacherEditProfile);

            //this line gets stage information
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(teacherEditProfileScene);
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
    public void onSeeYellowSlipsClick(ActionEvent actionEvent){
        //the scene that we want to load
        Parent TeacherYellowSlipsController = null;
        try {
            TeacherYellowSlipsController = FXMLLoader.load(getClass().getResource("../TeacherYellowSlips/teacherYellowSlips.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene TeacherYellowSlipsControllerScene = new Scene(TeacherYellowSlipsController);

        //this line gets stage information
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(TeacherYellowSlipsControllerScene);
        window.show();
    }
    public void uploadImageHandler(ActionEvent actionEvent) throws SQLException {
        String query = "UPDATE userteacherinfo SET image=? WHERE teacherid=?";
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
