package com.wiexon.app;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class NewServiceController {
    ObservableList<String> connectionTypes = FXCollections.observableArrayList("Serial Port", "Modbus TCP/IP");

    @FXML
    private JFXTextField serviceName;

    @FXML
    private JFXTextField serviceURI;

    @FXML
    private ChoiceBox connectionType;

    @FXML
    private JFXTextField responseTimuout;

    @FXML
    private VBox serialPortVbox;

    @FXML
    private ChoiceBox<?> comPortNumber;

    @FXML
    private ChoiceBox<?> baudRate;

    @FXML
    private ChoiceBox<?> dataBits;

    @FXML
    private ChoiceBox<?> evenParity;

    @FXML
    private ChoiceBox<?> stopBit;

    @FXML
    private VBox modbusTcpIpVbox;

    @FXML
    private JFXTextField ipAddress;

    @FXML
    private JFXTextField portNumber;

    @FXML
    private JFXTextField connectionTimeout;

    @FXML
    void makeService(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }


    @FXML
    private void initialize(){
        System.out.println("init ok");
        connectionType.setValue("Serial Port");
        connectionType.setItems(connectionTypes);
        connectionType.setOnAction(e->{
            System.out.println("trigged");
            if (connectionType.getValue().equals("Modbus TCP/IP")) {
                //System.out.println("data match");
                modbusTcpIpVbox.setVisible(true);
                serialPortVbox.setVisible(false);
            } else if (connectionType.getValue().equals("Serial Port")) {
                modbusTcpIpVbox.setVisible(false);
                serialPortVbox.setVisible(true);
            }
        });
    }


}