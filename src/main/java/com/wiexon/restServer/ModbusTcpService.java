package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ModbusTcpService implements ModbusService{

    private TCPMasterConnection connection = null;

    public ModbusTcpService(String host, int port, int connectionTimeout, int responseTimeout) throws Exception {

        //try {
            this.connection = new TCPMasterConnection(InetAddress.getByName(host));
            this.connection.setPort(port);
            this.connection.setTimeout(connectionTimeout);
            this.connection.connect();
            //connection.close();
        /*} catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputDiscretesRequest(addressOfFirstCoil, numberOfCoils));

        transaction.execute();
        ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();

        List<ModbusCoil> modbusCoilList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusCoil modbusCoil = new ModbusCoil();

            modbusCoil.setFunction(response.getFunctionCode());
            modbusCoil.setAddress(addressOfFirstCoil+i);
            modbusCoil.setValue(response.getDiscreteStatus(i));
            modbusCoil.setEntity(String.format("1%05d",(addressOfFirstCoil+i+1)));
            modbusCoil.setNumber(1);
            modbusCoil.setOffset(addressOfFirstCoil+i+1);

            modbusCoilList.add(modbusCoil);
        }
        return modbusCoilList;
    }

    @Override
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadCoilsRequest(addressOfFirstCoil, numberOfCoils));
        transaction.execute();

        ReadCoilsResponse response = (ReadCoilsResponse) transaction.getResponse();

        List<ModbusCoil> modbusCoilList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusCoil modbusCoil = new ModbusCoil();

            modbusCoil.setFunction(response.getFunctionCode());
            modbusCoil.setAddress(addressOfFirstCoil+i);
            modbusCoil.setValue(response.getCoilStatus(i));
            modbusCoil.setEntity(String.format("0%05d",(addressOfFirstCoil+i+1)));
            modbusCoil.setNumber(0);
            modbusCoil.setOffset(addressOfFirstCoil+i+1);

            modbusCoilList.add(modbusCoil);
        }
        return modbusCoilList;
    }

    @Override
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteCoilRequest(addressOfCoil, valueToWrite));


        transaction.execute();
        WriteCoilResponse response = (WriteCoilResponse) transaction.getResponse();
        //Todo Resolve response and return




        return null;
    }

    @Override
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        transaction.execute();
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

        List<ModbusRegister> modbusRegisterList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusRegister modbusRegister = new ModbusRegister();

            modbusRegister.setFunction(response.getFunctionCode());
            modbusRegister.setAddress(addressOfFirstRegisterToRead+i);
            modbusRegister.setValue(response.getRegisterValue(i));
            modbusRegister.setEntity(String.format("3%05d", (addressOfFirstRegisterToRead+i+1)));
            modbusRegister.setNumber(3);
            modbusRegister.setOffset(addressOfFirstRegisterToRead+i+1);

            modbusRegisterList.add(modbusRegister);
        }
        return modbusRegisterList;
    }

    @Override
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        transaction.execute();
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

        List<ModbusRegister> modbusRegisterList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusRegister modbusRegister = new ModbusRegister();

            modbusRegister.setFunction(response.getFunctionCode());
            modbusRegister.setAddress(addressOfFirstRegisterToRead+i);
            modbusRegister.setValue(response.getRegisterValue(i));
            modbusRegister.setEntity(String.format("4%05d", (addressOfFirstRegisterToRead+i+1)));
            modbusRegister.setNumber(4);
            modbusRegister.setOffset(addressOfFirstRegisterToRead+i+1);

            modbusRegisterList.add(modbusRegister);
        }
        return modbusRegisterList;
    }

    @Override
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteSingleRegisterRequest(addressOfHoldingRegisterToWrite, new SimpleInputRegister(newValueOfTheHoldingRegister)));

            transaction.execute();
            WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) transaction.getResponse();
            //Todo Resolve response and return

        return null;
    }
}
