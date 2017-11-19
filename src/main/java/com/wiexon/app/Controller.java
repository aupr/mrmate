package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
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

import java.io.IOException;
import java.sql.*;

public class Controller {
    private int selectedServiceId;
    Connection con;

    // JFX buttons
    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton stopButton;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXButton subButton;
    @FXML
    private JFXButton checkButton;
    @FXML
    private JFXButton crossButton;

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
                            nsc.dataBits.getValue().toString(), nsc.parityBit.getValue().toString(), nsc.stopBit.getValue().toString(), nsc.encoding.getValue().toString(), "Enabled");

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

        System.out.println("Disable button.");

    }

    @FXML
    void EditService(ActionEvent event) {

    }

    @FXML
    void EnableService(ActionEvent event) {

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
                checkButton.setDisable(false);
                crossButton.setDisable(false);
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

    private void storeNewService(String serviceName, String uri, String connectionType, String responseTimeout, String host, String port, String connectionTimeout, String comport, String baudRate, String dataBits, String parityBits, String stopBits, String mode, String modeView) throws SQLException {

        PreparedStatement preps = con.prepareStatement("INSERT INTO service (rowid, serviceName, uri, connectionType," +
                " responseTimeout, host, port, connectionTimeout, comport, baudRate, dataBits, parityBits, stopBits, mode, modeView)" +
                " VALUES ( null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
        preps.setString(14, modeView);

        preps.execute();
    }

    private void deleteService(int serviceId) throws SQLException, ClassNotFoundException {
        PreparedStatement preps = con.prepareStatement("DELETE FROM service WHERE id=?");
        preps.setInt(1, serviceId);
        preps.execute();
        loadTable();
    }
}
