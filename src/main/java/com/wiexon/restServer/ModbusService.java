package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import net.wimpi.modbus.ModbusException;

import java.util.List;

public interface ModbusService {
    public void close();
    public List<ModbusCoil> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public List<ModbusCoil> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public Boolean writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException;
    //public boolean writeMultipleCoils();
    public List<ModbusRegister> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public List<ModbusRegister> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public Boolean writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException;
    //public Boolean writeMultipleHoldingRegisters();

}
