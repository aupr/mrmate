package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import com.wiexon.restServer.pojo.ModbusRegister;
import javafx.scene.control.Button;
import net.wimpi.modbus.ModbusException;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    public void setModbusServiceMap(Map<String, ModbusService> modbusServiceMap) {
        this.modbusServiceMap = modbusServiceMap;
    }

    @GET
    @Path("/read/{serviceUri}/{type:[0-1]}/{reference}/{count}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusCoil> getModbusCoil(@PathParam("serviceUri") String serviceUri, @PathParam("type") String type, @PathParam("reference") int reference, @PathParam("count") int count) {
/*        if (firstLoad){
            modbusServiceMap = ModbusServiceMap.load();
            this.firstLoad = false;
        }*/

        boolean fault = true;
        List list = null;
        try {
            list = executeModbusReadService(serviceUri, type, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap = ModbusServiceMap.load();
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
/*        if (firstLoad){
            modbusServiceMap = ModbusServiceMap.load();
            this.firstLoad = false;
        }*/

        boolean fault = true;
        List list = null;
        try {
            list = executeModbusReadService(serviceUri, type, reference, count);
            fault = false;
        } catch (ModbusException e) {
            e.printStackTrace();
        } finally {
            if (fault) {
                modbusServiceMap = ModbusServiceMap.load();
                try {
                    list = executeModbusReadService(serviceUri, type, reference, count);
                } catch (ModbusException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
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
