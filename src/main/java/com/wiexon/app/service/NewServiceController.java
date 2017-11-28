package com.wiexon.app.service;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import jssc.SerialPortList;

public class NewServiceController {
    public boolean isMake = false;

    // Modbus Common fx fields
    String[] connectionTypeArray = new String[]{"Serial Port", "Modbus TCP/IP"};
    ObservableList<String> connectionTypeList = FXCollections.observableArrayList(connectionTypeArray);
    @FXML
    public JFXTextField serviceName;
    @FXML
    public JFXTextField serviceURI;
    @FXML
    public ChoiceBox connectionType;
    @FXML
    public JFXTextField responseTimuout;
    @FXML
    public Button okButton;

    // Modbus Serial port fx fields
    String[] comPortArray = SerialPortList.getPortNames();
    ObservableList<String> comPortList = FXCollections.observableArrayList(comPortArray);
    String[] baudRateArray = new String[]{"300 Baud", "600 Baud", "1200 Baud", "2400 Baud", "4800 Baud", "9600 Baud", "14400 Baud", "19200 Baud", "38400 Baud", "56000 Baud", "57600 Baud", "115200 Baud", "128000 Baud", "256000 Baud"};
    ObservableList<String> baudRateList = FXCollections.observableArrayList(baudRateArray);
    String[] dataBitsArray = new String[]{"7 Data bits", "8 Data bits"};
    ObservableList<String> dataBitsList = FXCollections.observableArrayList(dataBitsArray);
    String[] parityBitArray = new String[]{"None Parity", "Odd Parity", "Even Parity"};
    ObservableList<String> parityBitList = FXCollections.observableArrayList(parityBitArray);
    String[] stopBitArray = new String[]{"1 Stop Bit", "2 Stop Bits"};
    ObservableList<String> stopBitList = FXCollections.observableArrayList(stopBitArray);
    String[] encodingArray = new String[]{"RTU Mode", "ASCII Mode"};
    ObservableList<String> encodingList = FXCollections.observableArrayList(encodingArray);
    @FXML
    private VBox serialPortVbox;
    @FXML
    public ChoiceBox comPortNumber;
    @FXML
    public ChoiceBox baudRate;
    @FXML
    public ChoiceBox dataBits;
    @FXML
    public ChoiceBox parityBit;
    @FXML
    public ChoiceBox stopBit;
    @FXML
    public ChoiceBox encoding;

    // modbus TCP/IP fx fields
    @FXML
    private VBox modbusTcpIpVbox;
    @FXML
    public JFXTextField ipAddress;
    @FXML
    public JFXTextField portNumber;
    @FXML
    public JFXTextField connectionTimeout;


    @FXML
    void makeService(ActionEvent event) {
        this.isMake = true;
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }


    @FXML
    private void initialize(){
        System.out.println("init ok");

        // Connection Type choice list and control there view
        connectionType.setValue(connectionTypeArray[0]);
        connectionType.setItems(connectionTypeList);
        connectionType.setOnAction(e->{
            if (connectionType.getValue().equals("Modbus TCP/IP")) {
                modbusTcpIpVbox.setVisible(true);
                serialPortVbox.setVisible(false);
            } else if (connectionType.getValue().equals("Serial Port")) {
                modbusTcpIpVbox.setVisible(false);
                serialPortVbox.setVisible(true);
            }
        });

        // Serial or Comm Port List dropdown
        comPortNumber.setValue(comPortArray.length>0? comPortArray[0]:"");
        comPortNumber.setItems(comPortList);

        // Serial comm baud rate settings
        baudRate.setValue(baudRateArray[5]);
        baudRate.setItems(baudRateList);

        // Data bits selection
        dataBits.setValue(dataBitsArray[1]);
        dataBits.setItems(dataBitsList);

        // Parity bit selection
        parityBit.setValue(parityBitArray[2]);
        parityBit.setItems(parityBitList);

        // Stop bit selection
        stopBit.setValue(stopBitArray[0]);
        stopBit.setItems(stopBitList);

        // Encoding mode selection
        encoding.setValue(encodingArray[0]);
        encoding.setItems(encodingList);
    }
}