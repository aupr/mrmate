package com.wiexon.app.service;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.wiexon.app.JFValidator.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import jssc.SerialPortList;

public class NewServiceController {
    public boolean isMake = false;
    private boolean isEditing;

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
    @FXML
    public JFXTextField comPortNumber;

    // Modbus Serial port fx fields
    //String[] comPortArray = SerialPortList.getPortNames();
    //ObservableList<String> comPortList = FXCollections.observableArrayList(comPortArray);
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
    private VBox serialPortVbox, modbusTcpIpVbox;
    @FXML
    public ChoiceBox baudRate, dataBits, parityBit, stopBit, encoding;
    //@FXML
    //private Text noComportText;

    // modbus TCP/IP fx fields
    @FXML
    public JFXTextField ipAddress, portNumber, connectionTimeout;

    public NewServiceController(boolean isEditing) {
        this.isEditing = isEditing;
    }

    @FXML
    void makeService(ActionEvent event) {

        boolean isValidServiceName = serviceName.validate();
        boolean isValidServiceURI = serviceURI.validate();
        boolean isValidResponseTimeout = responseTimuout.validate();
        boolean isValidComport = true;
        boolean isValidIpAddress = true;
        boolean isValidPortNumber = true;
        boolean isValidConnectionTimeout = true;

        if (connectionType.getValue().toString().equals("Serial Port")) {
            isValidComport = comPortNumber.validate();
        } else if (connectionType.getValue().toString().equals("Modbus TCP/IP")) {
            isValidIpAddress = ipAddress.validate();
            isValidPortNumber = portNumber.validate();
            isValidConnectionTimeout = connectionTimeout.validate();
        }

        if ( isValidServiceName && isValidServiceURI && isValidResponseTimeout && isValidComport && isValidIpAddress && isValidPortNumber && isValidConnectionTimeout ) {
            this.isMake = true;
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    private void initialize(){
        System.out.println("init ok");
        //noComportText.setVisible(false);
        // Connection Type choice list and control their view
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
        //comPortNumber.setValue(comPortArray.length>0? comPortArray[0]:"");
        //comPortNumber.setItems(comPortList);

        // Converted comport dropdown to comport text
        comPortNumber.setText("COM3"); // in future it will search for free comport

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

        // validation start

        ComPortValidator comPortValidator = new ComPortValidator();
        comPortValidator.setMessage("Enter a valid COM Port");
        comPortNumber.getValidators().add(comPortValidator);

        ServiceNameValidator serviceNameValidator = new ServiceNameValidator();
        serviceNameValidator.setMessage("Enter a valid Name!");
        serviceName.getValidators().add(serviceNameValidator);

        if (isEditing) {
            serviceURI.setDisable(true);
            connectionType.setDisable(true);
        } else  {
            UriNameValidator uriNameValidator = new UriNameValidator();
            uriNameValidator.setMessage("Enter a valid URI name!");
            serviceURI.getValidators().add(uriNameValidator);
            UriCopyValidator uriCopyValidator = new UriCopyValidator();
            uriCopyValidator.setMessage("This one exists! Try another.");
            serviceURI.getValidators().add(uriCopyValidator);
        }

        TimeoutValidator timeoutValidator = new TimeoutValidator();
        timeoutValidator.setMessage("Enter a valid time!");
        responseTimuout.getValidators().add(timeoutValidator);
        connectionTimeout.getValidators().add(timeoutValidator);

        IpAddressValidator ipAddressValidator = new IpAddressValidator();
        ipAddressValidator.setMessage("Enter a valid IPV4 address!");
        ipAddress.getValidators().add(ipAddressValidator);

        HostPortValidator hostPortValidator = new HostPortValidator();
        hostPortValidator.setMessage("Enter a valid port number");
        portNumber.getValidators().add(hostPortValidator);

    }
}