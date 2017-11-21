package com.wiexon.restServer.pojo;

public class ModbusRegister {
    private int functioncode;
    private int offset;
    private int value;

    public int getFunctioncode() {
        return functioncode;
    }

    public void setFunctioncode(int functioncode) {
        this.functioncode = functioncode;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
