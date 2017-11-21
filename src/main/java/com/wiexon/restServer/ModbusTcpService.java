package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ModbusTcpService implements ModbusService{

    private TCPMasterConnection connection = null;

    private InetAddress inetAddress = null; //the slave's address
    private int port;

    public ModbusTcpService() {
        this.port = 80;
        try {
            this.inetAddress = InetAddress.getByName("192.168.0.254");
            this.connection = new TCPMasterConnection(inetAddress);
            this.connection.setPort(port);
            //this.connection.setTimeout(10);
            //Todo get connection timeout and then set connection timeout
            this.connection.connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputDiscretesRequest(addressOfFirstCoil, numberOfCoils));

        try {
            transaction.execute();
            ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadCoilsRequest(addressOfFirstCoil, numberOfCoils));

        try {
            transaction.execute();
            ReadCoilsResponse response = (ReadCoilsResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteCoilRequest(addressOfCoil, valueToWrite));

        try {
            transaction.execute();
            WriteCoilResponse response = (WriteCoilResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        try {
            transaction.execute();
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        try {
            transaction.execute();
            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteSingleRegisterRequest(addressOfHoldingRegisterToWrite, new SimpleInputRegister(newValueOfTheHoldingRegister)));

        try {
            transaction.execute();
            WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) transaction.getResponse();
            //Todo Resolve response and return

        } catch (ModbusException e) {
            e.printStackTrace();
        }
        return null;
    }
}
