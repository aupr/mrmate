package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import com.wiexon.restServer.*;
import com.wiexon.restServer.control.CORSFilter;
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

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private String dbUrl = null;
    private Stage primaryStage = null;
    private int selectedServiceId;
    HttpServer server = null;

    // JFX Fields
    @FXML
    private JFXButton playButton, stopButton, addButton, editButton, subButton, checkButton, crossButton;
    @FXML
    private TableView<ServiceTableData> serviceTable;
    @FXML
    private TableColumn sl, term, uri, pid, connection, mode, status;
    @FXML
    private Text hostNameView, portNameView;

    // Constructor Method
    public Controller(Stage primaryStage) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        dbUrl = "jdbc:sqlite:Base.db";
        this.primaryStage = primaryStage;
    }

    // Fxml method and other methods/////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void initialize() {
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

    @FXML
    void StartServices(ActionEvent event) {
        System.out.println("Working Start service!");

        try {
            server = HttpServerFactory.create("http://localhost:8888/");
            server.start();

            Resource.setModbusServiceMap(ModbusServiceMap.load());

            playButton.setDisable(true);
            stopButton.setDisable(false);
            addButton.setDisable(true);
            editButton.setDisable(true);
            subButton.setDisable(true);
            checkButton.setDisable(true);
            crossButton.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
        server.stop(0);

        Map<String, ModbusService> modbusServices = Resource.getModbusServiceMap();

        for (ModbusService modbusService : modbusServices.values()) {
            modbusService.close();
        }

        playButton.setDisable(false);
        stopButton.setDisable(true);
        addButton.setDisable(false);
        editButton.setDisable(true);
        subButton.setDisable(true);
        checkButton.setDisable(true);
        crossButton.setDisable(true);
    }

    @FXML
    void AddService(ActionEvent event) throws IOException {
        System.out.println("Add Action event");

        Stage newService = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/newService.fxml"));
        NewServiceController nsc = new NewServiceController();
        fxmlLoader.setController(nsc);

        Parent root = fxmlLoader.load();

        newService.setTitle("Add New Service Form!");
        //newService.initOwner(primaryStage);
        newService.setResizable(false);
        newService.initModality(Modality.APPLICATION_MODAL);
        //newService.initStyle(StageStyle.UTILITY);
       // newService.setAlwaysOnTop(true);
        newService.setScene(new Scene(root));
        newService.setOnHidden(e->{
            System.out.println("new form hiding!");
            if (nsc.isMake) {
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
            }
        });
        newService.showAndWait();
    }

    @FXML
    void EditService(ActionEvent event) throws IOException {
        System.out.println("Edit service clicked!");
        Stage editService = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/newService.fxml"));
        NewServiceController serviceController = new NewServiceController();
        fxmlLoader.setController(serviceController);
        Parent editRoot = fxmlLoader.load();

        editService.setScene(new Scene(editRoot));
        editService.setTitle("Edit service!");
        editService.setResizable(false);
        editService.initModality(Modality.APPLICATION_MODAL);

        Map<String, String> resmap = readService(selectedServiceId);
        if (resmap != null) {
            serviceController.serviceName.setText(resmap.get("serviceName"));
            serviceController.serviceURI.setText(resmap.get("uri"));
            serviceController.connectionType.setValue(resmap.get("connectionType"));
            serviceController.responseTimuout.setText(resmap.get("responseTimeout"));
            serviceController.ipAddress.setText(resmap.get("host"));
            serviceController.portNumber.setText(resmap.get("port"));
            serviceController.connectionTimeout.setText(resmap.get("connectionTimeout"));
            serviceController.comPortNumber.setValue(resmap.get("comport"));
            serviceController.baudRate.setValue(resmap.get("baudRate"));
            serviceController.dataBits.setValue(resmap.get("dataBits"));
            serviceController.parityBit.setValue(resmap.get("parityBits"));
            serviceController.stopBit.setValue(resmap.get("stopBits"));
            serviceController.encoding.setValue(resmap.get("mode"));

            serviceController.okButton.setText("Save Service");
        }

        editService.setOnHidden(e->{
            System.out.println("hiding edit service window!");
            if (serviceController.isMake) {
                Map<String, String> serviceDataMap= new HashMap<>();

                serviceDataMap.put("serviceName", serviceController.serviceName.getText().toString());
                serviceDataMap.put("serviceURI", serviceController.serviceURI.getText().toString());
                serviceDataMap.put("connectionType", serviceController.connectionType.getValue().toString());
                serviceDataMap.put("responseTimuout", serviceController.responseTimuout.getText().toString());
                serviceDataMap.put("ipAddress", serviceController.ipAddress.getText().toString());
                serviceDataMap.put("portNumber", serviceController.portNumber.getText().toString());
                serviceDataMap.put("connectionTimeout", serviceController.connectionTimeout.getText().toString());
                serviceDataMap.put("comPortNumber", serviceController.comPortNumber.getValue().toString());
                serviceDataMap.put("baudRate", serviceController.baudRate.getValue().toString());
                serviceDataMap.put("dataBits", serviceController.dataBits.getValue().toString());
                serviceDataMap.put("parityBit", serviceController.parityBit.getValue().toString());
                serviceDataMap.put("stopBit", serviceController.stopBit.getValue().toString());
                serviceDataMap.put("mode", serviceController.encoding.getValue().toString());

                updateService(serviceDataMap, selectedServiceId);

                loadTable();
            }
        });

        editService.showAndWait();
    }

    @FXML
    void RemoveService(ActionEvent event) throws SQLException, ClassNotFoundException {
        deleteService(selectedServiceId);
    }

    @FXML
    void EnableService(ActionEvent event) {
        System.out.println("Enable button.");
        if (alert("Do you confirm to enable it?")) {
            Connection con = null;
            Statement state = null;

            try {
                con = DriverManager.getConnection(dbUrl);
                state = con.createStatement();
                state.execute("UPDATE service SET modeView='Enabled' WHERE id="+selectedServiceId);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            loadTable();
        }
    }

    @FXML
    void DisableService(ActionEvent event) {

        System.out.println("Disable button clicked.");
        if (alert("Do you confirm to disable it?")) {
            Connection con = null;
            Statement state = null;
            try {
                con = DriverManager.getConnection(dbUrl);
                state = con.createStatement();
                state.execute("UPDATE service SET modeView='Disabled' WHERE id="+selectedServiceId);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            loadTable();
        }
    }

    @FXML
    void ShowErrors(ActionEvent event) {

    }

    @FXML
    private void tableRowSelect() {
        System.out.println("table clicked!");
        if (serviceTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println("Selected table row id: "+serviceTable.getSelectionModel().getSelectedItem().getId());

            this.selectedServiceId = serviceTable.getSelectionModel().getSelectedItem().getId();

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

    private void loadTable() {
        Connection con = null;
        Statement state = null;
        ResultSet rs = null;
        ObservableList<ServiceTableData> tableDataList = FXCollections.observableArrayList();

        try {
            con = DriverManager.getConnection(dbUrl);
            state = con.createStatement();
            rs = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='service'");

            if (!rs.next()) {
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
                serviceTable.setItems(tableDataList);
                editButton.setDisable(true);
                subButton.setDisable(true);
                checkButton.setDisable(true);
                crossButton.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeNewService(Map<String, String> data) {
        Connection con = null;
        PreparedStatement preps = null;

        try {
            con = DriverManager.getConnection(dbUrl);
            preps = con.prepareStatement("INSERT INTO service (rowid, serviceName, uri, connectionType," +
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> readService(int serviceId) {
        Connection con = null;
        Statement state = null;
        ResultSet res = null;
        Map<String, String> resmap = null;
        try {

            con = DriverManager.getConnection(dbUrl);
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM service WHERE id="+serviceId);
            if (res.next()) {
                resmap = new HashMap<>();
                resmap.put("serviceName", res.getString("serviceName"));
                resmap.put("uri", res.getString("uri"));
                resmap.put("connectionType", res.getString("connectionType"));
                resmap.put("responseTimeout", res.getString("responseTimeout"));
                resmap.put("host", res.getString("host"));
                resmap.put("port", res.getString("port"));
                resmap.put("connectionTimeout", res.getString("connectionTimeout"));
                resmap.put("comport", res.getString("comport"));
                resmap.put("baudRate", res.getString("baudRate"));
                resmap.put("dataBits", res.getString("dataBits"));
                resmap.put("parityBits", res.getString("parityBits"));
                resmap.put("stopBits", res.getString("stopBits"));
                resmap.put("mode", res.getString("mode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resmap;
    }

    private void updateService(Map<String, String> data, int id) {
        Connection con = null;
        PreparedStatement preps = null;
        try {
            con = DriverManager.getConnection(dbUrl);
            preps = con.prepareStatement("UPDATE service SET serviceName=?, uri=?, connectionType=?," +
                    " responseTimeout=?, host=?, port=?, connectionTimeout=?, comport=?, baudRate=?," +
                    " dataBits=?, parityBits=?, stopBits=?, mode=? WHERE id=?");

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
            preps.setInt(14, id);

            preps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteService(int serviceId) {
        if (alert("Do you confirm to remove this service!")) {
            Connection con = null;
            PreparedStatement preps = null;
            try {
                con = DriverManager.getConnection(dbUrl);
                preps = con.prepareStatement("DELETE FROM service WHERE id=?");
                preps.setInt(1, serviceId);
                preps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    preps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
