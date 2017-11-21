package com.wiexon.restServer;

import java.util.List;

public interface ModbusService {
    //public void connect();
    public List ReadDiscreteInputs(int AddressOfFirstCoil, int NumberOfCoils);
    public List ReadCoils(int AddressOfFirstCoil, int NumberOfCoils);
    public Boolean WriteSingleCoil(int AddressOfCoil, boolean ValueToWrite);
    //public boolean WriteMultipleCoils();
    public List ReadInputRegisters(int AddressOfFirstRegisterToRead, int NumberOfRegistersToRead);
    public List ReadMultipleHoldingRegisters(int AddressOfFirstRegisterToRead, int NumberOfRegistersToRead);
    public Boolean WriteSingleHoldingRegister(int AddressOfHoldingRegisterToWrite, int NewValueOfTheHoldingRegister);
    //public Boolean WriteMultipleHoldingRegisters();

}
