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
    public ModbusBit readDiscreteInput(int unitId, int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readDiscreteInputs(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public ModbusBit readCoil(int unitId, int addressOfFirstCoil) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusBit> readCoils(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public ModbusBit writeSingleCoil(int unitId, int addressOfCoil, boolean valueToWrite) throws ModbusException {
        return null;
    }

    @Override
    public ModbusStatus writeMultipleCoils(int unitId, int addressOfFirstCoil, BitVector bitVector) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord readInputRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readInputRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord readSingleHoldingRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusWord> readMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public ModbusWord writeSingleHoldingRegister(int unitId, int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {
        return null;
    }

    @Override
    public ModbusStatus writeMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException {
        return null;
    }
}
