package org.iut_ehealth.CommonDiseasesInfo;

import com.jfoenix.controls.JFXButton;
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

public class commonDiseasesInfoController {
    @FXML
    private JFXButton logoutButton = new JFXButton();
    @FXML
    private JFXButton editProfileButton = new JFXButton();

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
    private JFXTreeTableView commonDiseasesInfoListView ;
    ObservableList <commonDiseasesInfoModel> commonDiseasesInfoList;

    UserSession userSession = UserSession.getInstance();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection myConn = databaseConnection.getConnectionObject();




    public void initialize() throws SQLException, IOException {

        TreeTableColumn name = new TreeTableColumn("name");
        TreeTableColumn symptoms = new TreeTableColumn("symptoms");
        TreeTableColumn prevention = new TreeTableColumn("prevention");
        TreeTableColumn cure = new TreeTableColumn("cure");
        name.setPrefWidth(130);
        symptoms.setPrefWidth(275);
        prevention.setPrefWidth(275);
        cure.setPrefWidth(250);
        commonDiseasesInfoListView.getColumns().addAll(name,symptoms,prevention,cure);

        commonDiseasesInfoList = FXCollections.observableArrayList();

        String query = "SELECT name,symptoms,prevention,cure from commondisease";

        PreparedStatement pst = myConn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while(rs.next()){
            commonDiseasesInfoList.add(new commonDiseasesInfoModel(rs.getString("name"),rs.getString("symptoms"),rs.getString("prevention"),rs.getString("cure")));
        }
        pst.close();
        rs.close();

        name.setCellValueFactory(
                new TreeItemPropertyValueFactory<commonDiseasesInfoModel,String>("name")
        );
        symptoms.setCellValueFactory(
                new TreeItemPropertyValueFactory<commonDiseasesInfoModel,String>("symptoms")
        );
        prevention.setCellValueFactory(
                new TreeItemPropertyValueFactory<commonDiseasesInfoModel,String>("prevention")
        );
        cure.setCellValueFactory(
                new TreeItemPropertyValueFactory<commonDiseasesInfoModel,String>("cure")
        );
       TreeItem <commonDiseasesInfoModel>  root = new RecursiveTreeItem<>(commonDiseasesInfoList,RecursiveTreeObject::getChildren);
        commonDiseasesInfoListView.setRoot(root);
        commonDiseasesInfoListView.setShowRoot(false);


    }
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        //the scene that we want to load
        Parent LoginController = null;
        try {
            LoginController = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene LoginControllerScene = new Scene(LoginController);

        //this line gets stage information
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(LoginControllerScene);
        window.show();
    }


    public void browseHandler(ActionEvent actionEvent) {
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

    }
}
