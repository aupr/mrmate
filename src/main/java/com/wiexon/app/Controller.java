package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Stage primaryStage;
    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private int selectedServiceId;
    Connection con;

    // JFX Fields
    @FXML
    private JFXButton playButton, stopButton, addButton, editButton, subButton, checkButton, crossButton;
    @FXML
    private TableView<ServiceTableData> serviceTable;
    @FXML
    private TableColumn sl, term, uri, pid, connection, mode, status;
    @FXML
    private Text hostNameView, portNameView;


    // Fxml method and other methods/////////////////////////////////////////////////////////////////////////////////////////
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
        //newService.initOwner(primaryStage);
        newService.setResizable(false);
        newService.initModality(Modality.APPLICATION_MODAL);
        //newService.initStyle(StageStyle.UTILITY);
       // newService.setAlwaysOnTop(true);
        newService.setScene(new Scene(root));
        newService.setOnHidden(e->{
            if (nsc.isMake) {
                try {
                    Map<String, String> serviceDataMap= new HashMap<>();

                    serviceDataMap.put("serviceName", nsc.serviceName.getText().toString());
                    serviceDataMap.put("serviceURI", nsc.serviceURI.getText().toString());
                    serviceDataMap.put("connectionType", nsc.connectionType.getValue().toString());
                    serviceDataMap.put("responseTimuout", nsc.responseTimuout.getText().toString());
                    serviceDataMap.put("ipAddress", nsc.ipAddress.getText().toString());
                    serviceDataMap.put("portNumber", nsc.portNumber.getText().toString());
                    serviceDataMap.put("connectionTimeout", nsc.connectionTimeout.getText().toString());
                    serviceDataMap.put("comPortNumber", nsc.comPortNumber.getValue().toString());
                    serviceDataMap.put("baudRate", nsc.baudRate.getValue().toString());
                    serviceDataMap.put("dataBits", nsc.dataBits.getValue().toString());
                    serviceDataMap.put("parityBit", nsc.parityBit.getValue().toString());
                    serviceDataMap.put("stopBit", nsc.stopBit.getValue().toString());
                    serviceDataMap.put("mode", nsc.encoding.getValue().toString());
                    serviceDataMap.put("modeView", "Enabled");

                    storeNewService(serviceDataMap);
                    loadTable();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("new form hiding!");
        });
        newService.showAndWait();
    }

    @FXML
    void EditService(ActionEvent event) {
        System.out.println("Edit service clicked!");
        //primaryStage

    }

    @FXML
    void EnableService(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("Enable button.");
        if (alert("Do you confirm to enable it?")) {
            Statement state = con.createStatement();
            state.execute("UPDATE service SET modeView='Enabled' WHERE id="+selectedServiceId);
            loadTable();
        }
    }

    @FXML
    void DisableService(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Disable button.");
        if (alert("Do you confirm to disable it?")) {
            Statement state = con.createStatement();
            state.execute("UPDATE service SET modeView='Disabled' WHERE id="+selectedServiceId);
            loadTable();
        }
    }

    @FXML
    void RemoveService(ActionEvent event) throws SQLException, ClassNotFoundException {
        deleteService(selectedServiceId);
    }

    @FXML
    void ShowErrors(ActionEvent event) {

    }

    @FXML
    void StartServices(ActionEvent event) {

        System.out.println("Working Start service!");

        playButton.setDisable(true);
        stopButton.setDisable(false);
        addButton.setDisable(true);
        editButton.setDisable(true);
        subButton.setDisable(true);
        checkButton.setDisable(true);
        crossButton.setDisable(true);
    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
        playButton.setDisable(false);
        stopButton.setDisable(true);
        addButton.setDisable(false);
        editButton.setDisable(true);
        subButton.setDisable(true);
        checkButton.setDisable(true);
        crossButton.setDisable(true);
    }

    @FXML
    private void tableRowSelect() {
        System.out.println("table clicked!");
        if (serviceTable.getSelectionModel().getSelectedItem() != null) {
            this.selectedServiceId = serviceTable.getSelectionModel().getSelectedItem().getId();
            System.out.println(serviceTable.getSelectionModel().getSelectedItem().getId());

            if (!playButton.isDisable()) {
                editButton.setDisable(false);
                subButton.setDisable(false);
                if (serviceTable.getSelectionModel().getSelectedItem().getMode().equals("Enabled")) {
                    checkButton.setDisable(true);
                    crossButton.setDisable(false);
                } else {
                    checkButton.setDisable(false);
                    crossButton.setDisable(true);
                }


            }
        }

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
            int i=0;
            while (res.next()) {
                i++;
                tableDataList.add(new ServiceTableData(res.getInt("id"), i, res.getString("serviceName"), res.getString("uri"), 1010, res.getString("connectionType"), res.getString("modeView"), "ok"));
            }
            if (i>=20){
                addButton.setDisable(true);
            } else {
                addButton.setDisable(false);
            }
        }
        serviceTable.setItems(tableDataList);
        editButton.setDisable(true);
        subButton.setDisable(true);
        checkButton.setDisable(true);
        crossButton.setDisable(true);
    }

    private void storeNewService(Map<String, String> data) throws SQLException {

        PreparedStatement preps = con.prepareStatement("INSERT INTO service (rowid, serviceName, uri, connectionType," +
                " responseTimeout, host, port, connectionTimeout, comport, baudRate, dataBits, parityBits, stopBits, mode, modeView)" +
                " VALUES ( null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        preps.setString(1, data.get("serviceName"));
        preps.setString(2, data.get("serviceURI"));
        preps.setString(3, data.get("connectionType"));
        preps.setInt(4, Integer.parseInt(data.get("responseTimuout")));
        preps.setString(5, data.get("ipAddress"));
        preps.setString(6, data.get("portNumber"));
        preps.setInt(7, Integer.parseInt(data.get("connectionTimeout")));
        preps.setString(8, data.get("comPortNumber"));
        preps.setString(9, data.get("baudRate"));
        preps.setString(10, data.get("dataBits"));
        preps.setString(11, data.get("parityBit"));
        preps.setString(12, data.get("stopBit"));
        preps.setString(13, data.get("mode"));
        preps.setString(14, data.get("modeView"));

        preps.execute();
    }

    private ResultSet readService(int serviceId) throws SQLException {
        Statement state = con.createStatement();
        return state.executeQuery("SELECT * FROM service WHERE id="+serviceId);
    }

    private void updateService(Map<String, String> data) throws SQLException {
        PreparedStatement preps = con.prepareStatement("UPDATE service SET serviceName=?, uri=?, connectionType=?," +
                " responseTimeout=?, host=?, port=?, connectionTimeout=?, comport=?, baudRate=?," +
                " dataBits=?, parityBits=?, stopBits=?, mode=?, modeView=?");

        preps.setString(1, data.get("serviceName"));
        preps.setString(2, data.get("serviceURI"));
        preps.setString(3, data.get("connectionType"));
        preps.setInt(4, Integer.parseInt(data.get("responseTimuout")));
        preps.setString(5, data.get("ipAddress"));
        preps.setString(6, data.get("portNumber"));
        preps.setInt(7, Integer.parseInt(data.get("connectionTimeout")));
        preps.setString(8, data.get("comPortNumber"));
        preps.setString(9, data.get("baudRate"));
        preps.setString(10, data.get("dataBits"));
        preps.setString(11, data.get("parityBit"));
        preps.setString(12, data.get("stopBit"));
        preps.setString(13, data.get("mode"));
        preps.setString(14, data.get("modeView"));

        preps.execute();
    }

    private void deleteService(int serviceId) throws SQLException, ClassNotFoundException {
        if (alert("Do you confirm to remove this service!")) {
            PreparedStatement preps = con.prepareStatement("DELETE FROM service WHERE id=?");
            preps.setInt(1, serviceId);
            preps.execute();
            loadTable();
        }
    }

    private boolean alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }
}
