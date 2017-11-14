package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private TableView<?> serviceTable;

    @FXML
    private Text portNameView;

    @FXML
    private Text hostNameView;

    @FXML
    private JFXButton AddService;

    @FXML
    void AddService(ActionEvent event) {

    }

    @FXML
    void DisableService(ActionEvent event) {

    }

    @FXML
    void EditService(ActionEvent event) {

    }

    @FXML
    void EnableService(ActionEvent event) {

    }

    @FXML
    void RemoveService(ActionEvent event) {

    }

    @FXML
    void ShowErrors(ActionEvent event) {

    }

    @FXML
    void StartServices(ActionEvent event) {

        System.out.println("Working Start service!");

    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
    }
}
