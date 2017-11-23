package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.BitVector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/mrmate")
public class Resource {
    private static Map<String, ModbusService> modbusServiceMap = null;

    public static void setModbusServiceMap(Map<String, ModbusService> modbusServiceMap) {
        Resource.modbusServiceMap = modbusServiceMap;
    }

    public static Map<String, ModbusService> getModbusServiceMap() {
        return modbusServiceMap;
    }

    @GET
    @Path("/read/{serviceUri}/{type:[0-1]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusBit> getModbusCoil(@PathParam("serviceUri") String serviceUri, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {
        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        if (count < 1) count =1;
        else if (count > 125) count = 125;

        if (reference > 65411) {
            int pcount = 65536-reference;
            if (pcount < count) count = pcount;
        }

        boolean fault = true;
        List list = null;
        try {
            list = executeModbusReadService(serviceUri, type, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    list = executeModbusReadService(serviceUri, type, reference, count);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @GET
    @Path("/read/{serviceUri}/{type:[3-4]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusWord> getModbusRegister(@PathParam("serviceUri") String serviceUri, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        if (count < 1) count =1;
        else if (count > 125) count = 125;

        if (reference > 65411) {
            int pcount = 65536-reference;
            if (pcount < count) count = pcount;
        }

        boolean fault = true;
        List list = null;
        try {
            list = executeModbusReadService(serviceUri, type, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    list = executeModbusReadService(serviceUri, type, reference, count);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @POST
    @Path("/write/{serviceUri}/0/{reference}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusBit writeSingleCoil(ModbusBit data, @PathParam("serviceUri") String serviceUri, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusBit modbusBit = null;

        try {
            modbusBit = this.modbusServiceMap.get(serviceUri).writeSingleCoil(reference, data.isValue());
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusBit = this.modbusServiceMap.get(serviceUri).writeSingleCoil(reference, data.isValue());
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusBit;
    }

    @POST
    @Path("/write/{serviceUri}/0/{reference}/multi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusStatus writeMultipleCoil(List<ModbusBit> modbusBitList, @PathParam("serviceUri") String serviceUri, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        BitVector bitVector = new BitVector(modbusBitList.size());

        for (int i=0; i<modbusBitList.size(); i++) {
            bitVector.setBit(i, modbusBitList.get(i).isValue());
        }

        //todo debug the bit vector

        boolean fault = true;
        ModbusStatus modbusStatus = null;

        try {
            modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleCoils(reference, bitVector);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleCoils(reference, bitVector);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusStatus;
    }

    @POST
    @Path("/write/{serviceUri}/4/{reference}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusWord writeSingleRegister(ModbusWord data, @PathParam("serviceUri") String serviceUri, @PathParam("reference") int reference) {

        System.out.println(data.getClass().getName());

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusWord modbusWord = null;

        try {
            modbusWord = this.modbusServiceMap.get(serviceUri).writeSingleHoldingRegister(reference, data.getValue());
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusWord = this.modbusServiceMap.get(serviceUri).writeSingleHoldingRegister(reference, data.getValue());
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusWord;
    }

    @POST
    @Path("/write/{serviceUri}/4/{reference}/multi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusStatus writeMultipleRegister(List<ModbusWord> modbusWordList, @PathParam("serviceUri") String serviceUri, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        Register[] registers = new SimpleInputRegister[modbusWordList.size()];

        for (int i=0; i<modbusWordList.size(); i++) {
            registers[i] = new SimpleInputRegister(modbusWordList.get(i).getValue());
        }

        boolean fault = true;
        ModbusStatus modbusStatus = null;

        try {
            modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleHoldingRegisters(reference, registers);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleHoldingRegisters(reference, registers);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusStatus;
    }

    private List executeModbusReadService(String serviceUri, String type, int reference, int count) throws ModbusException {
        List list = null;
        if (type.equals("0")) {
            list = this.modbusServiceMap.get(serviceUri).readCoils(reference, count);
        } else if (type.equals("1")) {
            list = this.modbusServiceMap.get(serviceUri).readDiscreteInputs(reference, count);
        } else if (type.equals("3")) {
            list = this.modbusServiceMap.get(serviceUri).readInputRegisters(reference, count);
        } else if (type.equals("4")) {
            list = this.modbusServiceMap.get(serviceUri).readMultipleHoldingRegisters(reference, count);
        }
        return list;
    }

}
