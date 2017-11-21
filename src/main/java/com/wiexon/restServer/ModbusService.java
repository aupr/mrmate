package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;

import java.util.List;

public interface ModbusService {
    //public void connect();
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils);
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils);
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite);
    //public boolean writeMultipleCoils();
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead);
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead);
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister);
    //public Boolean writeMultipleHoldingRegisters();

}
