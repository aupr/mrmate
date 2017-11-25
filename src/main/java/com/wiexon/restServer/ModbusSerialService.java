package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.util.BitVector;
import net.wimpi.modbus.util.SerialParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModbusSerialService implements ModbusService {

    private SerialConnection connection = null;

    public ModbusSerialService() throws Exception {
        //ModbusCoupler.createModbusCoupler(null);
        ModbusCoupler.getReference().setUnitID(1);

        SerialParameters params = new SerialParameters();
        params.setPortName("COM4");
        params.setBaudRate(9600);
        params.setDatabits(8);
        params.setParity("even");
        params.setStopbits(1);
        params.setEncoding("rtu");
        params.setEcho(false);

        connection = new SerialConnection(params);
        connection.open();
    }

    @Override
    public void close() throws IOException {
        //this.connection.
        this.connection.close();
    }

    @Override
    public ModbusBit readDiscreteInput(int unitId, int addressOfFirstCoil) throws ModbusException {
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(addressOfFirstCoil, 1);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(addressOfFirstCoil, numberOfCoils);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadCoilsRequest request = new ReadCoilsRequest(addressOfFirstCoil, 1);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadCoilsRequest request = new ReadCoilsRequest(addressOfFirstCoil, numberOfCoils);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        WriteCoilRequest request = new WriteCoilRequest(addressOfCoil, valueToWrite);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        WriteMultipleCoilsRequest request = new WriteMultipleCoilsRequest(addressOfFirstCoil, bitVector);
        request.setUnitID(unitId);
        request.setHeadless();

        transaction.setRequest(request);

        transaction.execute();
        WriteMultipleCoilsResponse response = (WriteMultipleCoilsResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        modbusStatus.setUnit(response.getUnitID());
        modbusStatus.setFunction(response.getFunctionCode());
        modbusStatus.setNumber(0);

        if (response.getBitCount() > 0) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }

    @Override
    public ModbusWord readInputRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException {
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(addressOfFirstRegisterToRead, 1);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, 1);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead);
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(addressOfHoldingRegisterToWrite, new SimpleInputRegister(newValueOfTheHoldingRegister));
        request.setUnitID(unitId);
        request.setHeadless();

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
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);

        WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(addressOfFirstRegisterToWrite, registers);
        request.setUnitID(unitId);
        request.setHeadless();

        transaction.setRequest(request);

        transaction.execute();
        WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        modbusStatus.setUnit(response.getUnitID());
        modbusStatus.setFunction(response.getFunctionCode());
        modbusStatus.setNumber(4);

        if (response.getWordCount() == registers.length) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }
}
