package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.util.BitVector;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ModbusTcpService implements ModbusService{

    private TCPMasterConnection connection = null;

    public ModbusTcpService(String host, int port, int connectionTimeout, int responseTimeout) throws Exception {
            this.connection = new TCPMasterConnection(InetAddress.getByName(host));
            this.connection.setPort(port);
            this.connection.setTimeout(connectionTimeout);
            this.connection.connect();
    }

    @Override
    public void close() throws IOException {
        this.connection.close();
    }

    @Override
    public ModbusBit readDiscreteInput(int unitId, int addressOfFirstCoil) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(addressOfFirstCoil, 1);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();

        ModbusBit modbusBit = null;

        if (response != null) {
            modbusBit = new ModbusBit();

            modbusBit.setUnit(request.getUnitID());
            modbusBit.setFunction(response.getFunctionCode());
            modbusBit.setAddress(addressOfFirstCoil);
            modbusBit.setValue(response.getDiscreteStatus(0));
            modbusBit.setEntity(String.format("1%05d",(addressOfFirstCoil+1)));
            modbusBit.setNumber(1);
            modbusBit.setOffset(addressOfFirstCoil+1);
        }
        return modbusBit;
    }

    @Override
    public List<ModbusBit> readDiscreteInputs(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(addressOfFirstCoil, numberOfCoils);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();

        List<ModbusBit> modbusBitList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusBit modbusBit = new ModbusBit();

            modbusBit.setUnit(response.getUnitID());
            modbusBit.setFunction(response.getFunctionCode());
            modbusBit.setAddress(addressOfFirstCoil+i);
            modbusBit.setValue(response.getDiscreteStatus(i));
            modbusBit.setEntity(String.format("1%05d",(addressOfFirstCoil+i+1)));
            modbusBit.setNumber(1);
            modbusBit.setOffset(addressOfFirstCoil+i+1);

            modbusBitList.add(modbusBit);
        }
        return modbusBitList;
    }

    @Override
    public ModbusBit readCoil(int unitId, int addressOfFirstCoil) throws ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadCoilsRequest request = new ReadCoilsRequest(addressOfFirstCoil, 1);
        request.setUnitID(unitId);

        transaction.setRequest(request);
        transaction.execute();

        ReadCoilsResponse response = (ReadCoilsResponse) transaction.getResponse();

        ModbusBit modbusBit = null;

        if (response != null) {
            modbusBit = new ModbusBit();

            modbusBit.setUnit(response.getUnitID());
            modbusBit.setFunction(response.getFunctionCode());
            modbusBit.setAddress(addressOfFirstCoil);
            modbusBit.setValue(response.getCoilStatus(0));
            modbusBit.setEntity(String.format("0%05d",(addressOfFirstCoil+1)));
            modbusBit.setNumber(0);
            modbusBit.setOffset(addressOfFirstCoil+1);
        }
        return modbusBit;
    }

    @Override
    public List<ModbusBit> readCoils(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadCoilsRequest request = new ReadCoilsRequest(addressOfFirstCoil, numberOfCoils);
        request.setUnitID(unitId);

        transaction.setRequest(request);
        transaction.execute();

        ReadCoilsResponse response = (ReadCoilsResponse) transaction.getResponse();

        List<ModbusBit> modbusBitList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusBit modbusBit = new ModbusBit();

            modbusBit.setUnit(response.getUnitID());
            modbusBit.setFunction(response.getFunctionCode());
            modbusBit.setAddress(addressOfFirstCoil+i);
            modbusBit.setValue(response.getCoilStatus(i));
            modbusBit.setEntity(String.format("0%05d",(addressOfFirstCoil+i+1)));
            modbusBit.setNumber(0);
            modbusBit.setOffset(addressOfFirstCoil+i+1);

            modbusBitList.add(modbusBit);
        }
        return modbusBitList;
    }

    @Override
    public ModbusBit writeSingleCoil(int unitId, int addressOfCoil, boolean valueToWrite) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        WriteCoilRequest request = new WriteCoilRequest(addressOfCoil, valueToWrite);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        WriteCoilResponse response = (WriteCoilResponse) transaction.getResponse();

        ModbusBit modbusBit = new ModbusBit();

        modbusBit.setUnit(response.getUnitID());
        modbusBit.setFunction(response.getFunctionCode());
        modbusBit.setAddress(addressOfCoil);
        modbusBit.setValue(response.getCoil());
        modbusBit.setEntity(String.format("0%05d", (addressOfCoil+1)));
        modbusBit.setNumber(0);
        modbusBit.setOffset(addressOfCoil+1);

        return modbusBit;
    }

    @Override
    public ModbusStatus writeMultipleCoils(int unitId, int addressOfFirstCoil, BitVector bitVector) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        WriteMultipleCoilsRequest request = new WriteMultipleCoilsRequest(addressOfFirstCoil, bitVector);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        WriteMultipleCoilsResponse response = (WriteMultipleCoilsResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        modbusStatus.setUnit(response.getUnitID());
        modbusStatus.setFunction(response.getFunctionCode());
        modbusStatus.setNumber(0);
        modbusStatus.setCount(response.getBitCount()+1);
        modbusStatus.setEntity(String.format("0%05d", (addressOfFirstCoil+1)));
        modbusStatus.setAddress(addressOfFirstCoil);
        modbusStatus.setOffset(addressOfFirstCoil+1);

        if (response.getBitCount() > 0) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }

    @Override
    public ModbusWord readInputRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(addressOfFirstRegisterToRead, 1);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

        ModbusWord modbusWord = null;

        if (response != null){
            modbusWord = new ModbusWord();

            modbusWord.setUnit(response.getUnitID());
            modbusWord.setFunction(response.getFunctionCode());
            modbusWord.setAddress(addressOfFirstRegisterToRead);
            modbusWord.setValue(response.getRegisterValue(0));
            modbusWord.setEntity(String.format("3%05d", (addressOfFirstRegisterToRead+1)));
            modbusWord.setNumber(3);
            modbusWord.setOffset(addressOfFirstRegisterToRead+1);
        }
        return modbusWord;
    }

    @Override
    public List<ModbusWord> readInputRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

        List<ModbusWord> modbusWordList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusWord modbusWord = new ModbusWord();

            modbusWord.setUnit(response.getUnitID());
            modbusWord.setFunction(response.getFunctionCode());
            modbusWord.setAddress(addressOfFirstRegisterToRead+i);
            modbusWord.setValue(response.getRegisterValue(i));
            modbusWord.setEntity(String.format("3%05d", (addressOfFirstRegisterToRead+i+1)));
            modbusWord.setNumber(3);
            modbusWord.setOffset(addressOfFirstRegisterToRead+i+1);

            modbusWordList.add(modbusWord);
        }
        return modbusWordList;
    }

    @Override
    public ModbusWord readSingleHoldingRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, 1);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

        ModbusWord modbusWord = null;

        if (response != null){
            modbusWord = new ModbusWord();

            modbusWord.setUnit(response.getUnitID());
            modbusWord.setFunction(response.getFunctionCode());
            modbusWord.setAddress(addressOfFirstRegisterToRead);
            modbusWord.setValue(response.getRegisterValue(0));
            modbusWord.setEntity(String.format("4%05d", (addressOfFirstRegisterToRead+1)));
            modbusWord.setNumber(4);
            modbusWord.setOffset(addressOfFirstRegisterToRead+1);
        }
        return modbusWord;
    }

    @Override
    public List<ModbusWord> readMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

        List<ModbusWord> modbusWordList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusWord modbusWord = new ModbusWord();

            modbusWord.setUnit(response.getUnitID());
            modbusWord.setFunction(response.getFunctionCode());
            modbusWord.setAddress(addressOfFirstRegisterToRead+i);
            modbusWord.setValue(response.getRegisterValue(i));
            modbusWord.setEntity(String.format("4%05d", (addressOfFirstRegisterToRead+i+1)));
            modbusWord.setNumber(4);
            modbusWord.setOffset(addressOfFirstRegisterToRead+i+1);

            modbusWordList.add(modbusWord);
        }
        return modbusWordList;
    }

    @Override
    public ModbusWord writeSingleHoldingRegister(int unitId, int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(addressOfHoldingRegisterToWrite, new SimpleInputRegister(newValueOfTheHoldingRegister));
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) transaction.getResponse();

        ModbusWord modbusWord = new ModbusWord();

        modbusWord.setUnit(response.getUnitID());
        modbusWord.setFunction(response.getFunctionCode());
        modbusWord.setAddress(addressOfHoldingRegisterToWrite);
        modbusWord.setValue(response.getRegisterValue());
        modbusWord.setEntity(String.format("4%05d", (addressOfHoldingRegisterToWrite+1)));
        modbusWord.setNumber(4);
        modbusWord.setOffset(addressOfHoldingRegisterToWrite+1);

        return modbusWord;
    }

    @Override
    public ModbusStatus writeMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

        WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(addressOfFirstRegisterToWrite, registers);
        request.setUnitID(unitId);

        transaction.setRequest(request);

        transaction.execute();
        WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        modbusStatus.setUnit(response.getUnitID());
        modbusStatus.setFunction(response.getFunctionCode());
        modbusStatus.setNumber(4);
        modbusStatus.setCount(response.getWordCount());
        modbusStatus.setEntity(String.format("4%05d", (addressOfFirstRegisterToWrite+1)));
        modbusStatus.setAddress(addressOfFirstRegisterToWrite);
        modbusStatus.setOffset(addressOfFirstRegisterToWrite+1);

        if (response.getWordCount() == registers.length) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }
}
