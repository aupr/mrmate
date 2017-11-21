package com.wiexon.restServer;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;

public class ModbusTcpService {
    /* The important instances of the classes mentioned before */
    TCPMasterConnection con = null; //the connection
    ModbusTCPTransaction trans = null; //the transaction
    ReadInputDiscretesRequest req = null; //the request
    ReadInputDiscretesResponse res = null; //the response

    /* Variables for storing the parameters */
    InetAddress addr = null; //the slave's address
    int port = Modbus.DEFAULT_PORT;
    int ref = 0; //the reference; offset where to start reading from
    int count = 0; //the number of DI's to read
    int repeat = 1; //a loop for repeating the transaction

    public ModbusTcpService() {

    }
}
