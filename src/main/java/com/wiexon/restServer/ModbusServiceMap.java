package com.wiexon.restServer;

import com.wiexon.app.ServiceTable;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ModbusServiceMap {
    public static Map<String, Integer> trackTableRow = new HashMap<>();

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

            int i = -1;
            while (res.next()) {
                i++;
                if (res.getString("modeView").equals("Enabled")){
                    try {
                        ServiceTable.getDataList().get(i).setStatus("Connecting");
                        if (res.getString("connectionType").equals("Modbus TCP/IP")){
                            trackTableRow.put(res.getString("uri"), i);
                            // try to dump modbus tcp/ip service object
                            serviceMap.put(res.getString("uri"), new ModbusTcpService(res.getString("host"), Integer.parseInt(res.getString("port")), res.getInt("connectionTimeout"), res.getInt("responseTimeout")));
                        } else if (res.getString("connectionType").equals("Serial Port")){
                            int selfUnit = 1;
                            String comPort = res.getString("comport");
                            String baudRateString = res.getString("baudRate");
                            String[] baudRateSplitted = baudRateString.split("\\s+");
                            int baudRate = Integer.parseInt(baudRateSplitted[0]);
                            String dataBitsString = res.getString("dataBits");
                            String[] dataBitsSplitted = dataBitsString.split("\\s+");
                            int dataBits = Integer.parseInt(dataBitsSplitted[0]);
                            String parityString = res.getString("parityBits");
                            String[] paritySplitted = parityString.split("\\s+");
                            String parity = paritySplitted[0].toLowerCase();
                            String stopBitsString = res.getString("stopBits");
                            String[] stopBitsSplitted = stopBitsString.split("\\s+");
                            int stopBits = Integer.parseInt(stopBitsSplitted[0]);
                            String encodingString = res.getString("mode");
                            String[] encodingSplitted = encodingString.split("\\s+");
                            String encoding = encodingSplitted[0].toLowerCase();

                            trackTableRow.put(res.getString("uri"), i);
                            // try to dump modbus serial service object
                            serviceMap.put(res.getString("uri"), new ModbusSerialService(selfUnit,comPort,baudRate,dataBits,parity,stopBits,encoding));
                        }
                        ServiceTable.getDataList().get(i).setStatus("Connected");
                    } catch (Exception e) {
                        ServiceTable.getDataList().get(i).setStatus("Error");
                        e.printStackTrace();
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

    public static ModbusService update(String uri) {
        ModbusService modbusService = null;
        Map<String, ModbusService> modbusServiceMap = Resource.getModbusServiceMap();
        try {
            modbusServiceMap.get(uri).close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        Connection con = null;
        PreparedStatement preps = null;
        ResultSet res = null;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Base.db");
            preps = con.prepareStatement("SELECT * FROM service WHERE uri=?");
            preps.setString(1, uri);
            res = preps.executeQuery();

            if (res.next()) {
                if (res.getString("modeView").equals("Enabled")){
                    try {
                        ServiceTable.getDataList().get(trackTableRow.get(uri)).setStatus("Connecting");
                        if (res.getString("connectionType").equals("Modbus TCP/IP")){
                            // returning the new object
                            modbusService = new ModbusTcpService(res.getString("host"), Integer.parseInt(res.getString("port")), res.getInt("connectionTimeout"), res.getInt("responseTimeout"));
                        } else if (res.getString("connectionType").equals("Serial Port")){

                            int selfUnit = 1;
                            String comPort = res.getString("comport");
                            String baudRateString = res.getString("baudRate");
                            String[] baudRateSplitted = baudRateString.split("\\s+");
                            int baudRate = Integer.parseInt(baudRateSplitted[0]);
                            String dataBitsString = res.getString("dataBits");
                            String[] dataBitsSplitted = dataBitsString.split("\\s+");
                            int dataBits = Integer.parseInt(dataBitsSplitted[0]);
                            String parityString = res.getString("parityBits");
                            String[] paritySplitted = parityString.split("\\s+");
                            String parity = paritySplitted[0].toLowerCase();
                            String stopBitsString = res.getString("stopBits");
                            String[] stopBitsSplitted = stopBitsString.split("\\s+");
                            int stopBits = Integer.parseInt(stopBitsSplitted[0]);
                            String encodingString = res.getString("mode");
                            String[] encodingSplitted = encodingString.split("\\s+");
                            String encoding = encodingSplitted[0].toLowerCase();

                            // returning the new object
                            modbusService = new ModbusSerialService(selfUnit,comPort,baudRate,dataBits,parity,stopBits,encoding);
                        }
                        ServiceTable.getDataList().get(trackTableRow.get(uri)).setStatus("Connected");
                    } catch (Exception e) {
                        ServiceTable.getDataList().get(trackTableRow.get(uri)).setStatus("Error");
                        e.printStackTrace();
                    }

                }
            }
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
                preps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return modbusService;
    }
}
