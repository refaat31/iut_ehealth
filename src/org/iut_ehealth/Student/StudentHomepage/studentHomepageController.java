package org.iut_ehealth.Student.StudentHomepage;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.iut_ehealth.UserSession;

public class studentHomepageController {
    @FXML
    private JFXButton logoutButton = new JFXButton();

    UserSession userSession = UserSession.getInstance();
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        System.out.println(userSession.getUsername());
        System.out.println(userSession.getUsertype());
    }
}
