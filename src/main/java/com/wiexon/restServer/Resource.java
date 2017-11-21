package com.wiexon.restServer;

import javafx.scene.control.Button;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

@Path("/mrmate")
public class Resource {
    public static Map<String, String> modbusServiceMap = null;

    private static int count = 0;

    public void setModbusServiceMap(Map<String, String> modbusServiceMap) {
        this.modbusServiceMap = modbusServiceMap;
    }

    @GET
    public String getData() {

        this.count++;
        new ServiceThread("aman_"+count, new Button());

        return "Thread started "+count+" StoreData: "+modbusServiceMap.get("aval");
    }
}
