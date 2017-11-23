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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ModbusTcpService implements ModbusService{

    private TCPMasterConnection connection = null;

    public ModbusTcpService(String host, int port, int connectionTimeout, int responseTimeout) throws Exception {
            this.connection = new TCPMasterConnection(InetAddress.getByName(host));
            this.connection.setPort(port);
            this.connection.setTimeout(connectionTimeout);
    }

    @Override
    public void close() {
        this.connection.close();
    }

    @Override
    public ModbusBit readDiscreteInput(int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputDiscretesRequest(addressOfFirstCoil, numberOfCoils));

        transaction.execute();
        ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();

        List<ModbusBit> modbusBitList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusBit modbusBit = new ModbusBit();

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
    public ModbusBit readCoil(int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadCoilsRequest(addressOfFirstCoil, numberOfCoils));
        transaction.execute();

        ReadCoilsResponse response = (ReadCoilsResponse) transaction.getResponse();

        List<ModbusBit> modbusBitList = new ArrayList<>();

        for (int i=0; i<numberOfCoils; i++) {
            ModbusBit modbusBit = new ModbusBit();

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
    public ModbusBit writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteCoilRequest(addressOfCoil, valueToWrite));

        transaction.execute();
        WriteCoilResponse response = (WriteCoilResponse) transaction.getResponse();

        ModbusBit modbusBit = new ModbusBit();

        modbusBit.setFunction(response.getFunctionCode());
        modbusBit.setAddress(addressOfCoil);
        modbusBit.setValue(response.getCoil());
        modbusBit.setEntity(String.format("0%05d", (addressOfCoil+1)));
        modbusBit.setNumber(0);
        modbusBit.setOffset(addressOfCoil+1);

        return modbusBit;
    }

    @Override
    public ModbusStatus writeMultipleCoils(int addressOfFirstCoil, BitVector bitVector) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteMultipleCoilsRequest(addressOfFirstCoil, bitVector));

        transaction.execute();
        WriteMultipleCoilsResponse response = (WriteMultipleCoilsResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        if (response.getBitCount() > 0) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }

    @Override
    public ModbusWord readInputRegister(int addressOfFirstRegisterToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadInputRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        transaction.execute();
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

        List<ModbusWord> modbusWordList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusWord modbusWord = new ModbusWord();

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
    public ModbusWord readMultipleHoldingRegister(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new ReadMultipleRegistersRequest(addressOfFirstRegisterToRead, numberOfRegistersToRead));

        transaction.execute();
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

        List<ModbusWord> modbusWordList = new ArrayList<>();

        for (int i=0; i<response.getWordCount(); i++){
            ModbusWord modbusWord = new ModbusWord();

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
    public ModbusWord writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {

        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteSingleRegisterRequest(addressOfHoldingRegisterToWrite, new SimpleInputRegister(newValueOfTheHoldingRegister)));

        transaction.execute();
        WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) transaction.getResponse();

        ModbusWord modbusWord = new ModbusWord();

        modbusWord.setFunction(response.getFunctionCode());
        modbusWord.setAddress(addressOfHoldingRegisterToWrite);
        modbusWord.setValue(response.getRegisterValue());
        modbusWord.setEntity(String.format("4%05d", (addressOfHoldingRegisterToWrite+1)));
        modbusWord.setNumber(4);
        modbusWord.setOffset(addressOfHoldingRegisterToWrite+1);

        return modbusWord;
    }

    @Override
    public ModbusStatus writeMultipleHoldingRegisters(int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(new WriteMultipleRegistersRequest(addressOfFirstRegisterToWrite, registers));

        transaction.execute();
        WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) transaction.getResponse();

        ModbusStatus modbusStatus = new ModbusStatus();

        if (response.getWordCount() == registers.length) modbusStatus.setStatus(true);
        else modbusStatus.setStatus(false);

        return modbusStatus;
    }
}
