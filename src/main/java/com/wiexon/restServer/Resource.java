package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import javafx.scene.control.Button;
import net.wimpi.modbus.ModbusException;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/mrmate")
public class Resource {
    private static Map<String, ModbusService> modbusServiceMap = null;
    private static boolean firstLoad = true;

   // private static int count = 0;

    /*public void setModbusServiceMap(Map<String, ModbusService> modbusServiceMap) {
        this.modbusServiceMap = modbusServiceMap;
    }*/

    public static void setModbusServiceMap(Map<String, ModbusService> modbusServiceMap) {
        Resource.modbusServiceMap = modbusServiceMap;
    }

    public static Map<String, ModbusService> getModbusServiceMap() {
        return modbusServiceMap;
    }

    @GET
    @Path("/read/{serviceUri}/{type:[0-1]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusCoil> getModbusCoil(@PathParam("serviceUri") String serviceUri, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {
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
    public List<ModbusRegister> getModbusRegister(@PathParam("serviceUri") String serviceUri, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {

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
    @Path("/write")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String writeSingleCoil(String data) {

        return data;
    }

    private List executeModbusReadService(String serviceUri, String type, int reference, int count) throws ModbusException {
        List list = null;
        if (type.equals("0")) {
            list = modbusServiceMap.get(serviceUri).readCoils(reference, count);
        } else if (type.equals("1")) {
            list = modbusServiceMap.get(serviceUri).readDiscreteInputs(reference, count);
        } else if (type.equals("3")) {
            list = modbusServiceMap.get(serviceUri).readInputRegisters(reference, count);
        } else if (type.equals("4")) {
            list = modbusServiceMap.get(serviceUri).readMultipleHoldingRegisters(reference, count);
        }
        return list;
    }

}
