package com.wiexon.restServer.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModbusStatus {
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
