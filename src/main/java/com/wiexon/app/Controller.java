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
import java.sql.*;
import java.util.Enumeration;

public class Controller {
    Connection con;

    //private final

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
        NewServiceController nsc = new NewServiceController();
        fxmlLoader.setController(nsc);

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
            if (nsc.isMake) {

                // serviceName, uri, connectionType, responseTimeout, host, port, connectionTimeout, comport, baudRate, dataBits, parityBits, stopBits, mode, modeView
                try {
                    storeNewService(nsc.serviceName.getText().toString(), nsc.serviceURI.getText().toString(), nsc.connectionType.getValue().toString(),
                            nsc.responseTimuout.getText().toString(), nsc.ipAddress.getText().toString(), nsc.portNumber.getText().toString(),
                            nsc.connectionTimeout.getText().toString(), nsc.comPortNumber.getValue().toString(), nsc.baudRate.getValue().toString(),
                            nsc.dataBits.getValue().toString(), nsc.parityBit.getValue().toString(), nsc.stopBit.getValue().toString(), nsc.encoding.getValue().toString());

                    //System.out.println(nsc.connectionType.getValue().toString());

                    loadTable();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        // Table column property settings
        // serviceTable.setEditable(true);
        sl.setCellValueFactory(new PropertyValueFactory<ServiceTableData,String>("sl"));
        term.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("term"));
        uri.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("uri"));
        pid.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("pid"));
        connection.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("connection"));
        mode.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("mode"));
        status.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("status"));

        serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            System.out.println("table row selected!");
            System.out.println(obs.getValue().getSl().toString());
        });

        loadTable();
    }

    private void loadTable() throws ClassNotFoundException, SQLException {

        ObservableList<ServiceTableData> tableDataList = FXCollections.observableArrayList();

        // Database Connection for table data and other services
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:Base.db");
        Statement state = con.createStatement();

        ResultSet resultSet = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='service'");
        if (!resultSet.next()) {
            System.out.println("Service Table not exists! Creating New table...");
            state.execute("CREATE TABLE service(id INTEGER PRIMARY KEY, serviceName VARCHAR(20), uri VARCHAR(20), connectionType VARCHAR(30), responseTimeout INT, host VARCHAR(30), port VARCHAR(10), connectionTimeout INT, comport VARCHAR(10), baudRate VARCHAR(30), dataBits VARCHAR(30), parityBits VARCHAR(30), stopBits VARCHAR(30), mode VARCHAR(20), modeView VARCHAR(20))");
        } else {
            System.out.println("Table exists fetching data!");
            ResultSet res = state.executeQuery("SELECT * FROM service");
            while (res.next()) {
                tableDataList.add(new ServiceTableData(res.getInt("id"), res.getString("serviceName"), res.getString("uri"), 1010, res.getString("connectionType"), res.getString("modeView"), "ok"));
            }
        }
        serviceTable.setItems(tableDataList);


    }

    private void storeNewService(String serviceName, String uri, String connectionType, String responseTimeout, String host, String port, String connectionTimeout, String comport, String baudRate, String dataBits, String parityBits, String stopBits, String mode) throws SQLException {

        PreparedStatement preps = con.prepareStatement("INSERT INTO service (rowid, serviceName, uri, connectionType," +
                " responseTimeout, host, port, connectionTimeout, comport, baudRate, dataBits, parityBits, stopBits, mode)" +
                " VALUES ( null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        preps.setString(1, serviceName);
        preps.setString(2, uri);
        preps.setString(3, connectionType);
        preps.setInt(4, Integer.parseInt(responseTimeout));
        preps.setString(5, host);
        preps.setString(6, port);
        preps.setInt(7, Integer.parseInt(connectionTimeout));
        preps.setString(8, comport);
        preps.setString(9, baudRate);
        preps.setString(10, dataBits);
        preps.setString(11, parityBits);
        preps.setString(12, stopBits);
        preps.setString(13, mode);

        preps.execute();
    }


}
