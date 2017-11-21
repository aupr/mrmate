package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;

import java.util.List;

public class ModbusSerialService implements ModbusService {
    public ModbusSerialService() {
    }

    @Override
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) {
        return null;
    }

    @Override
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils) {
        return null;
    }

    @Override
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite) {
        return null;
    }

    @Override
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) {
        return null;
    }

    @Override
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) {
        return null;
    }

    @Override
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) {
        return null;
    }
}
