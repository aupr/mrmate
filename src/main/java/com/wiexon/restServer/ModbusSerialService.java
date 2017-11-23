package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;

import java.util.List;

public class ModbusSerialService implements ModbusService {
    public ModbusSerialService() {
    }


    @Override
    public void close() {

    }

    @Override
    public ModbusBit readDiscreteInput(int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public ModbusBit readCoil(int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public ModbusBit writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException {
        return null;
    }

    @Override
    public ModbusStatus writeMultipleCoils(int addressOfFirstCoil, BitVector bitVector) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord readInputRegister(int addressOfFirstRegisterToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord readMultipleHoldingRegister(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {
        return null;
    }

    @Override
    public ModbusStatus writeMultipleHoldingRegisters(int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException {
        return null;
    }
}
