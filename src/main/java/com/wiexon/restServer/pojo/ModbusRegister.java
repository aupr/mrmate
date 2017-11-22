package com.wiexon.restServer.pojo;

public class ModbusRegister {
    private int function;
    private int address;
    private int value;
    private String entity;
    private int entitynumber;
    private int entityaddress;

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntitynumber() {
        return entitynumber;
    }

    public void setEntitynumber(int entitynumber) {
        this.entitynumber = entitynumber;
    }

    public int getEntityaddress() {
        return entityaddress;
    }

    public void setEntityaddress(int entityaddress) {
        this.entityaddress = entityaddress;
    }
}
