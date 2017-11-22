package com.wiexon.restServer;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ModbusServiceMap {

    public static Map<String,ModbusService> load() {
        Connection con = null;
        Statement state = null;
        ResultSet res = null;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Base.db");
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM service");

            Map<String, ModbusService> serviceMap = new HashMap<>();

            while (res.next()) {
                if (res.getString("modeView").equals("Enabled")){
                    if (res.getString("connectionType").equals("Modbus TCP/IP")){
                        try {
                            serviceMap.put(res.getString("uri"), new ModbusTcpService(res.getString("host"), Integer.parseInt(res.getString("port")), res.getInt("connectionTimeout"), res.getInt("responseTimeout")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (res.getString("connectionType").equals("Serial Port")){
                        //todo for serial port modbus
                    }

                }
            }
            return serviceMap;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
