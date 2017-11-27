package com.wiexon.app;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LogTableData {
    private static int trackLogId = 0;
    private final SimpleIntegerProperty logId;
    private final SimpleStringProperty logTnD;
    private final SimpleStringProperty logMessage;

    public LogTableData(String logMessage) {
        this.logId = new SimpleIntegerProperty(++trackLogId);
        this.logTnD = new SimpleStringProperty("time");
        this.logMessage = new SimpleStringProperty(logMessage);
    }

    public int getLogId() {
        return logId.get();
    }

    public SimpleIntegerProperty logIdProperty() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId.set(logId);
    }

    public String getLogTnD() {
        return logTnD.get();
    }

    public SimpleStringProperty logTnDProperty() {
        return logTnD;
    }

    public void setLogTnD(String logTnD) {
        this.logTnD.set(logTnD);
    }

    public String getLogMessage() {
        return logMessage.get();
    }

    public SimpleStringProperty logMessageProperty() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage.set(logMessage);
    }
}
