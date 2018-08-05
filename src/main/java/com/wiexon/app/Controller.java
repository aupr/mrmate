package com.wiexon.app;

import com.jfoenix.controls.JFXButton;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import com.wiexon.app.log.LogController;
import com.wiexon.app.log.LogTable;
import com.wiexon.app.log.LogTableData;
import com.wiexon.app.service.NewServiceController;
import com.wiexon.app.service.ServiceTable;
import com.wiexon.app.service.ServiceTableData;
import com.wiexon.app.settings.SettingsController;
import com.wiexon.app.settings.SettingsHolder;
import com.wiexon.restServer.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class Controller {
    //ObservableList<ServiceTableData> tableDataList = FXCollections.observableArrayList();
    private String dbUrl = null;
    private Stage primaryStage = null;
    private int selectedServiceId;
    HttpServer server = null;
    private boolean multiClickFilter = true;

    // JFX Menu fields
    @FXML
    private MenuItem menuAddNew, menuStart, menuStop, menuErrorShow, menuSettings, menuClose, menuEnable, menuDisable, menuEdit, menuDelete;

    // JFX Fields
    @FXML
    private JFXButton playButton, stopButton, addButton, editButton, subButton, checkButton, crossButton, errorButton;
    @FXML
    private TableView<ServiceTableData> serviceTable;
    @FXML
    private TableColumn sl, term, uri, connection, address, mode, status;
    @FXML
    private Text hostNameView, portNameView, statusView;

    // Constructor Method
    public Controller(Stage primaryStage) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        dbUrl = "jdbc:sqlite:Base.db";
        this.primaryStage = primaryStage;
    }

    // Fxml method and other methods/////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void initialize() {
        // Button tooltip initialization
        Tooltip playButtonTooltip = new Tooltip();
        playButtonTooltip.setText("Start All Services");
        playButton.setTooltip(playButtonTooltip);

        Tooltip stopButtonTooltip = new Tooltip();
        stopButtonTooltip.setText("Stop All Services");
        stopButton.setTooltip(stopButtonTooltip);

        Tooltip addButtonTooltip = new Tooltip();
        addButtonTooltip.setText("Add New Service");
        addButton.setTooltip(addButtonTooltip);

        Tooltip editButtonTooltip = new Tooltip();
        editButtonTooltip.setText("Edit Service");
        editButton.setTooltip(editButtonTooltip);

        Tooltip subButtonTooltip = new Tooltip();
        subButtonTooltip.setText("Delete Service");
        subButton.setTooltip(subButtonTooltip);

        Tooltip checkButtonTooltip = new Tooltip();
        checkButtonTooltip.setText("Enable Service");
        checkButton.setTooltip(checkButtonTooltip);

        Tooltip crossButtonTooltip = new Tooltip();
        crossButtonTooltip.setText("Disable Service");
        crossButton.setTooltip(crossButtonTooltip);

        Tooltip errorButtonTooltip = new Tooltip();
        errorButtonTooltip.setText("System Log!");
        //errorButtonTooltip.set
        errorButton.setTooltip(errorButtonTooltip);

        // Table column property settings
        // serviceTable.setEditable(true);
        sl.setCellValueFactory(new PropertyValueFactory<ServiceTableData,String>("sl"));
        term.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("term"));
        uri.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("uri"));
        connection.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("connection"));
        address.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("address"));
        mode.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("mode"));
        status.setCellValueFactory(new PropertyValueFactory<ServiceTableData, String>("status"));


        //errorButton.setDisable(false);
        SettingsHolder.loadSettings();
        //System.out.println("System settings loaded!");
        loadTable();
        portNameView.setText("Port: "+SettingsHolder.getPort());
        LogTable.getDataList().add(new LogTableData("Application started!"));

        if (SettingsHolder.isAutoStartService()) {
            StartServices(new ActionEvent());
        }
    }

    @FXML
    void StartServices(ActionEvent event) {
        playButton.setDisable(true);
        addButton.setDisable(true);

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("Start Button Clicked!");
                LogTable.getDataList().add(new LogTableData("Attempt to start server!"));
                statusView.setText("Status: Starting");

                try {
                    server = HttpServerFactory.create("http://127.0.0.1:"+SettingsHolder.getPort()+"/");
                    server.start();


                    statusView.setText("Status: Running");
                    LogTable.getDataList().add(new LogTableData("Server started"));

                    Resource.setModbusServiceMap(ModbusServiceMap.load());
                    buttonDisplayControler("start");
                } catch (IOException e) {
                    multiClickFilter = true;
                    statusView.setText("Status: Error");
                    LogTable.getDataList().add(new LogTableData("Failed to start server! Assigned port is using by another application."));
                    e.printStackTrace();
                }
                return null;
            }
        };
        if (multiClickFilter) {
            new Thread(task).start();
            multiClickFilter = false;
        }
    }

    @FXML
    void StopServices(ActionEvent event) {
        System.out.println("Working Stop service!");
        LogTable.getDataList().add(new LogTableData("Attempt to stop services!"));
        server.stop(0);
        statusView.setText("Status: Stopped");
        Map<String, ModbusService> modbusServices = Resource.getModbusServiceMap();

        for (ModbusService modbusService : modbusServices.values()) {
            try {
                modbusService.close();
            } catch (IOException e) {
                System.out.println("Got connection close exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        loadTable();

        buttonDisplayControler("stop");
        LogTable.getDataList().add(new LogTableData("Service stopped successfully!"));
        multiClickFilter = true;
    }

    @FXML
    void AddService(ActionEvent event) throws IOException {
        System.out.println("Add Action event");

        Stage newService = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/newService.fxml"));
        NewServiceController nsc = new NewServiceController(false);
        fxmlLoader.setController(nsc);

        Parent root = fxmlLoader.load();

        newService.setTitle("Add New Service!");
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
                //serviceDataMap.put("comPortNumber", nsc.comPortNumber.getValue().toString());
                serviceDataMap.put("comPortNumber", nsc.comPortNumber.getText().toString());
                serviceDataMap.put("baudRate", nsc.baudRate.getValue().toString());
                serviceDataMap.put("dataBits", nsc.dataBits.getValue().toString());
                serviceDataMap.put("parityBit", nsc.parityBit.getValue().toString());
                serviceDataMap.put("stopBit", nsc.stopBit.getValue().toString());
                serviceDataMap.put("mode", nsc.encoding.getValue().toString());
                serviceDataMap.put("modeView", "Enabled");

                storeNewService(serviceDataMap);
                loadTable();
                LogTable.getDataList().add(new LogTableData("A new service has added to the service list!"));
            }
        });
        newService.setX(primaryStage.getX()+120);
        newService.setY(primaryStage.getY()+50);
        newService.showAndWait();
    }

    @FXML
    void EditService(ActionEvent event) throws IOException {
        System.out.println("Edit service clicked!");
        Stage editService = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/newService.fxml"));
        NewServiceController serviceController = new NewServiceController(true);

        fxmlLoader.setController(serviceController);
        Parent editRoot = fxmlLoader.load();

        editService.setScene(new Scene(editRoot));
        editService.setTitle("Edit Service!");
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
            serviceController.comPortNumber.setText(resmap.get("comport"));
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
                //serviceDataMap.put("comPortNumber", serviceController.comPortNumber.getValue().toString());
                serviceDataMap.put("comPortNumber", serviceController.comPortNumber.getText().toString());
                serviceDataMap.put("baudRate", serviceController.baudRate.getValue().toString());
                serviceDataMap.put("dataBits", serviceController.dataBits.getValue().toString());
                serviceDataMap.put("parityBit", serviceController.parityBit.getValue().toString());
                serviceDataMap.put("stopBit", serviceController.stopBit.getValue().toString());
                serviceDataMap.put("mode", serviceController.encoding.getValue().toString());

                updateService(serviceDataMap, selectedServiceId);

                loadTable();
                LogTable.getDataList().add(new LogTableData("A service has been updated!"));
            }
        });
        editService.setX(primaryStage.getX()+120);
        editService.setY(primaryStage.getY()+50);
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

        try {
            Stage logStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/log.fxml"));
            LogController logController = new LogController(LogTable.getDataList());
            fxmlLoader.setController(logController);

            Parent root = fxmlLoader.load();

            logStage.setTitle("System Log!");
            logStage.setResizable(false);
            logStage.initModality(Modality.APPLICATION_MODAL);
            logStage.setScene(new Scene(root));

            logStage.setX(primaryStage.getX()+50);
            logStage.setY(primaryStage.getY()+50);
            logStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void about(ActionEvent event) {
        System.out.println("about clicked");
        try {
            Stage aboutStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/about.fxml"));
            AboutController aboutController = new AboutController();
            fxmlLoader.setController(aboutController);

            Parent root = fxmlLoader.load();

            aboutStage.setTitle("About!");
            aboutStage.setResizable(false);
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.setScene(new Scene(root));

            aboutStage.setX(primaryStage.getX()+75);
            aboutStage.setY(primaryStage.getY()+50);
            aboutStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void communication(ActionEvent event) {
        System.out.println("clicked communication");

        /*try {
            Desktop.getDesktop().browse(new URL("http://ariexon.com").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

        try {
            Stage communicationStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/communication.fxml"));
            CommunicationController communicationController = new CommunicationController();
            fxmlLoader.setController(communicationController);

            Parent root = fxmlLoader.load();

            communicationStage.setTitle("Communication!");
            communicationStage.setResizable(false);
            communicationStage.initModality(Modality.APPLICATION_MODAL);
            communicationStage.setScene(new Scene(root));

            communicationStage.setX(primaryStage.getX()+75);
            communicationStage.setY(primaryStage.getY()+50);
            communicationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuCloseFunction() {
        primaryStage.fireEvent(
                new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)
        );
    }

    @FXML
    private void tableRowSelect() {
        System.out.println("table clicked!");
        if (serviceTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println("Selected table row id: "+serviceTable.getSelectionModel().getSelectedItem().getId());

            this.selectedServiceId = serviceTable.getSelectionModel().getSelectedItem().getId();

            if (!playButton.isDisable()) {
                buttonDisplayControler("rowSelect");
                if (serviceTable.getSelectionModel().getSelectedItem().getMode().equals("Enabled")) {
                    buttonDisplayControler("rowEnabled");
                } else {
                    buttonDisplayControler("rowDisabled");
                }
            }
        }
    }

    @FXML
    private void appSettings() {
        //
        //System.out.println(SettingsHolder.getPort()+" "+SettingsHolder.isAutoStartService()+" "+SettingsHolder.isAutoMinimize()+" "+SettingsHolder.isExitOnClose());

        try {
            Stage settingsStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/settings.fxml"));
            SettingsController settingsController = new SettingsController();
            fxmlLoader.setController(settingsController);

            Parent root = fxmlLoader.load();

            settingsStage.setTitle("Settings!");
            settingsStage.setResizable(false);
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setScene(new Scene(root));

            settingsStage.setX(primaryStage.getX()+220);
            settingsStage.setY(primaryStage.getY()+90);
            settingsStage.setOnHidden(event -> {
                System.out.println("Settings Hidden");
                if (settingsController.isMake) {
                    System.out.println("updating");
                    SettingsHolder.updateSettings(Integer.parseInt(settingsController.settingsPort.getText()), settingsController.settingsAutoStartService.isSelected(),settingsController.settingsAutoMinimize.isSelected(),settingsController.settingsExitOnClose.isSelected());
                    portNameView.setText("Port: "+SettingsHolder.getPort());
                    LogTable.getDataList().add(new LogTableData("Application settings has changed!"));
                }
            });

            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadTable() {
        serviceTable.setItems(ServiceTable.getDataList());
        int i = ServiceTable.loadDataList();

        if (i>=20){
            buttonDisplayControler("addFalse");

        } else {
            buttonDisplayControler("addTrue");
        }
        buttonDisplayControler("loadTable");
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
            if (data.get("connectionType").equals("Serial Port"))
                preps.setString(8, data.get("comPortNumber"));
            else
                preps.setString(8, "");
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
        if (alert("Do you confirm to remove this service?")) {
            Connection con = null;
            PreparedStatement preps = null;
            try {
                con = DriverManager.getConnection(dbUrl);
                preps = con.prepareStatement("DELETE FROM service WHERE id=?");
                preps.setInt(1, serviceId);
                preps.execute();
                LogTable.getDataList().add(new LogTableData("A service has been deleted!"));
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
        alert.setTitle("Confirm?");
        alert.setX(primaryStage.getX()+200);
        alert.setY(primaryStage.getY()+150);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }

    private void buttonDisplayControler(String mode) {
        switch (mode) {
            case "start":
                playButton.setDisable(true);
                stopButton.setDisable(false);
                addButton.setDisable(true);
                editButton.setDisable(true);
                subButton.setDisable(true);
                checkButton.setDisable(true);
                crossButton.setDisable(true);
                break;
            case "stop":
                playButton.setDisable(false);
                stopButton.setDisable(true);
                addButton.setDisable(false);
                editButton.setDisable(true);
                subButton.setDisable(true);
                checkButton.setDisable(true);
                crossButton.setDisable(true);
                break;
            case  "addTrue":
                addButton.setDisable(false);
                break;
            case "addFalse":
                addButton.setDisable(true);
                break;
            case "loadTable":
                editButton.setDisable(true);
                subButton.setDisable(true);
                checkButton.setDisable(true);
                crossButton.setDisable(true);
                break;
            case "rowSelect":
                editButton.setDisable(false);
                subButton.setDisable(false);
                break;
            case "rowEnabled":
                checkButton.setDisable(true);
                crossButton.setDisable(false);
                break;
            case "rowDisabled":
                checkButton.setDisable(false);
                crossButton.setDisable(true);
                break;
            default:
        }

        // for menu button
        menuStart.setDisable(playButton.isDisable());
        menuStop.setDisable(stopButton.isDisable());
        menuAddNew.setDisable(addButton.isDisable());
        menuEdit.setDisable(editButton.isDisable());
        menuDelete.setDisable(subButton.isDisable());
        menuEnable.setDisable(checkButton.isDisable());
        menuDisable.setDisable(crossButton.isDisable());

        menuSettings.setDisable(playButton.isDisable());

    }
}
