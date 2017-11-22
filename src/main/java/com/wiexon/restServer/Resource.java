package com.wiexon.restServer;

import com.wiexon.restServer.pojo.ModbusCoil;
import javafx.scene.control.Button;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/mrmate")
public class Resource {
    public static Map<String, ModbusService> modbusServiceMap = null;

    private static int count = 0;

    public void setModbusServiceMap(Map<String, ModbusService> modbusServiceMap) {
        this.modbusServiceMap = modbusServiceMap;
    }

    @GET
    @Path("/read")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ModbusCoil> getData() {

        return null;
    }
}
