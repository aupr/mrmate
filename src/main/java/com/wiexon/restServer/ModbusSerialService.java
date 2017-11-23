package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import net.wimpi.modbus.ModbusException;

import java.util.List;

public class ModbusSerialService implements ModbusService {
    public ModbusSerialService() {
    }

    @Override
    public void close() {

    }

    @Override
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException {
        return null;
    }

    @Override
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException {
        return null;
    }

    @Override
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException {
        return null;
    }
}
