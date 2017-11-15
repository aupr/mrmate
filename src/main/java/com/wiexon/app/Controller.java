package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import com.sparetimelabs.serial.CommPortIdentifier;
import com.sparetimelabs.serial.PureJavaSerialPort;
import com.sparetimelabs.serial.SerialPort;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jssc.SerialPortList;

import java.io.IOException;
import java.util.Enumeration;

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
    void AddService(ActionEvent event) throws IOException {
        System.out.println("Add Action event");

        Stage newService = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/newService.fxml"));
        NewServiceController newServiceController = new NewServiceController();
        fxmlLoader.setController(newServiceController);

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        newService.setTitle("Add New Service Form!");
        newService.setResizable(false);
        newService.initStyle(StageStyle.UTILITY);
        newService.setAlwaysOnTop(true);
        newService.setScene(new Scene(root));
        newService.setOnHidden(e->{
            if (newServiceController.isMake) {
                

            }
            System.out.println("new form hiding!");
        });
        newService.show();

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
        //Enumeration<String> list=
        //System.out.println(list);
        //System.out.println(SerialPortList.getPortNames()[0]);
        //System.out.println(CommPortIdentifier.getPortIdentifiers());


    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
    }
}
