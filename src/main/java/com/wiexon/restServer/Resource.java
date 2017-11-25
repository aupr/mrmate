package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusBit;
import com.wiexon.restServer.pojo.ModbusStatus;
import com.wiexon.restServer.pojo.ModbusWord;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleInputRegister;
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
    @Path("/read/{serviceUri}/{unitId}/{type:[0-1]}/{reference}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusBit getModbusCoil(@PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("type") String type, @PathParam("reference") int reference) {
        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusBit modbusBit = null;
        try {
            modbusBit = readModbusBit(serviceUri, type, unitId, reference);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    modbusBit = readModbusBit(serviceUri, type, unitId, reference);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusBit;
    }


    @GET
    @Path("/read/{serviceUri}/{unitId}/{type:[0-1]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusBit> getModbusCoils(@PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {
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
        List<ModbusBit> modbusBitList = null;
        try {
            modbusBitList = readModbusBits(serviceUri, type, unitId, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    modbusBitList = readModbusBits(serviceUri, type, unitId, reference, count);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusBitList;
    }

    @GET
    @Path("/read/{serviceUri}/{unitId}/{type:[3-4]}/{reference}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusWord getModbusRegister(@PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("type") String type, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusWord modbusWord = null;
        try {
            modbusWord = readModbusWord(serviceUri, type, unitId, reference);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    modbusWord = readModbusWord(serviceUri, type, unitId, reference);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusWord;
    }

    @GET
    @Path("/read/{serviceUri}/{unitId}/{type:[3-4]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusWord> getModbusRegisters(@PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {

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
        List<ModbusWord> modbusWordList = null;
        try {
            modbusWordList = readModbusWords(serviceUri, type, unitId, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                try {
                    modbusWordList = readModbusWords(serviceUri, type, unitId, reference, count);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusWordList;
    }

    @POST
    @Path("/write/{serviceUri}/{unitId}/0/{reference}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusBit writeSingleCoil(ModbusBit data, @PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusBit modbusBit = null;

        try {
            modbusBit = this.modbusServiceMap.get(serviceUri).writeSingleCoil(unitId, reference, data.isValue());
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusBit = this.modbusServiceMap.get(serviceUri).writeSingleCoil(unitId, reference, data.isValue());
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusBit;
    }

    @POST
    @Path("/write/{serviceUri}/{unitId}/0/{reference}/multi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusStatus writeMultipleCoil(List<ModbusBit> modbusBitList, @PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("reference") int reference) {

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        BitVector bitVector = new BitVector(modbusBitList.size()-1);

        for (int i=0; i<modbusBitList.size(); i++) {
            bitVector.setBit(i, modbusBitList.get(i).isValue());
        }
        
        boolean fault = true;
        ModbusStatus modbusStatus = null;

        try {
            modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleCoils(unitId, reference, bitVector);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleCoils(unitId, reference, bitVector);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusStatus;
    }

    @POST
    @Path("/write/{serviceUri}/{unitId}/4/{reference}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusWord writeSingleRegister(ModbusWord data, @PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("reference") int reference) {

        System.out.println(data.getClass().getName());

        if (reference < 1) reference = 1;
        else if (reference > 65536) reference = 65536;
        reference = reference-1;

        boolean fault = true;
        ModbusWord modbusWord = null;

        try {
            modbusWord = this.modbusServiceMap.get(serviceUri).writeSingleHoldingRegister(unitId, reference, data.getValue());
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusWord = this.modbusServiceMap.get(serviceUri).writeSingleHoldingRegister(unitId, reference, data.getValue());
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusWord;
    }

    @POST
    @Path("/write/{serviceUri}/{unitId}/4/{reference}/multi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ModbusStatus writeMultipleRegister(List<ModbusWord> modbusWordList, @PathParam("serviceUri") String serviceUri, @PathParam("unitId") int unitId, @PathParam("reference") int reference) {

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
            modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleHoldingRegisters(unitId, reference, registers);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                try {
                    modbusServiceMap.put(serviceUri, ModbusServiceMap.update(serviceUri));
                    modbusStatus = this.modbusServiceMap.get(serviceUri).writeMultipleHoldingRegisters(unitId, reference, registers);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return modbusStatus;
    }

    private ModbusBit readModbusBit(String serviceUri, String type, int unitId, int reference) throws ModbusException {
        ModbusBit modbusBit = null;
        if (type.equals("0")) {
            modbusBit = this.modbusServiceMap.get(serviceUri).readCoil(unitId, reference);
        } else if (type.equals("1")) {
            modbusBit = this.modbusServiceMap.get(serviceUri).readDiscreteInput(unitId, reference);
        }
        return modbusBit;
    }

    private List<ModbusBit> readModbusBits(String serviceUri, String type, int unitId, int reference, int count) throws ModbusException {
        List<ModbusBit> modbusBitList = null;
        if (type.equals("0")) {
            modbusBitList = this.modbusServiceMap.get(serviceUri).readCoils(unitId, reference, count);
        } else if (type.equals("1")) {
            modbusBitList = this.modbusServiceMap.get(serviceUri).readDiscreteInputs(unitId, reference, count);
        }
        return modbusBitList;
    }

    private ModbusWord readModbusWord(String serviceUri, String type, int unitId, int reference) throws ModbusException {
        ModbusWord modbusWord = null;
        if (type.equals("3")) {
            modbusWord = this.modbusServiceMap.get(serviceUri).readInputRegister(unitId, reference);
        } else if (type.equals("4")) {
            modbusWord = this.modbusServiceMap.get(serviceUri).readSingleHoldingRegister(unitId, reference);
        }
        return modbusWord;
    }

    private List<ModbusWord> readModbusWords(String serviceUri, String type, int unitId, int reference, int count) throws ModbusException {
        List<ModbusWord> modbusWordList = null;
        if (type.equals("3")) {
            modbusWordList = this.modbusServiceMap.get(serviceUri).readInputRegisters(unitId, reference, count);
        } else if (type.equals("4")) {
            modbusWordList = this.modbusServiceMap.get(serviceUri).readMultipleHoldingRegisters(unitId, reference, count);
        }
        return modbusWordList;
    }

}
