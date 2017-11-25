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
    public ModbusBit readDiscreteInput(int unitId, int addressOfFirstCoil) throws ModbusException;
    public List<ModbusBit> readDiscreteInputs(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public ModbusBit readCoil(int unitId, int addressOfFirstCoil) throws ModbusException;
    public List<ModbusBit> readCoils(int unitId, int addressOfFirstCoil, int numberOfCoils) throws ModbusException;
    public ModbusBit writeSingleCoil(int unitId, int addressOfCoil, boolean valueToWrite) throws ModbusException;
    public ModbusStatus writeMultipleCoils(int unitId, int addressOfFirstCoil, BitVector bitVector) throws ModbusException;
    public ModbusWord readInputRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException;
    public List<ModbusWord> readInputRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public ModbusWord readSingleHoldingRegister(int unitId, int addressOfFirstRegisterToRead) throws ModbusException;
    public List<ModbusWord> readMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToRead, int numberOfRegistersToRead) throws ModbusException;
    public ModbusWord writeSingleHoldingRegister(int unitId, int addressOfHoldingRegisterToWrite, int newValueOfTheHoldingRegister) throws ModbusException;
    public ModbusStatus writeMultipleHoldingRegisters(int unitId, int addressOfFirstRegisterToWrite, Register[] registers) throws ModbusException;
}
