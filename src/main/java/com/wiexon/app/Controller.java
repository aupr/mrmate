package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import com.sparetimelabs.serial.CommPortIdentifier;
import com.sparetimelabs.serial.PureJavaSerialPort;
import com.sparetimelabs.serial.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jssc.SerialPortList;

import java.io.IOException;
import java.util.Enumeration;

public class Controller {

    private final ObservableList<ServiceTableData> tableDataList = FXCollections.observableArrayList(
            new ServiceTableData("1", "abc", "def", "pid", "connection", "mode", "runnning")
    );

    @FXML
    private TableView<ServiceTableData> serviceTable;
    @FXML
    private TableColumn sl, term, uri, pid, connection, mode, status;

    @FXML
    private Text hostNameView, portNameView;

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


                serviceTable.setItems(tableDataList);

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

    @FXML
    private void initialize() {
        //serviceTable.setEditable(true);
        sl.setCellValueFactory(new PropertyValueFactory<ServiceTableData,String>("sl"));
        term.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("term"));
        uri.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("uri"));
        pid.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("pid"));
        connection.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("connection"));
        mode.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("mode"));
        status.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("status"));

    }
}
