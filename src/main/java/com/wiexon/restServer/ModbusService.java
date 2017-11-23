package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;

import java.util.List;

public interface ModbusService {
    public void close();
    public ModbusBit readDiscreteInput(int addressOfFirstCoil) throws ModbusException;
    public List<ModbusBit> readDiscreteInputs(int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public ModbusBit readCoil(int addressOfFirstCoil) throws ModbusException;
    public List<ModbusBit> readCoils(int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public ModbusBit writeSingleCoil(int addressOfCoil, boolean valueToWrite) throws ModbusException;
    public ModbusStatus writeMultipleCoils(int addressOfFirstCoil, BitVector bitVector) throws ModbusException;
    public ModbusWord readInputRegister(int addressOfFirstRegisterToRead) throws ModbusException;
    public List<ModbusWord> readInputRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public ModbusWord readMultipleHoldingRegister(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public List<ModbusWord> readMultipleHoldingRegisters(int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public ModbusWord writeSingleHoldingRegister(int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException;
    public ModbusStatus writeMultipleHoldingRegisters(int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException;
}
