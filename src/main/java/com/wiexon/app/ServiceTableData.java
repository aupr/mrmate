package com.wiexon.app;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ServiceTableData {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty sl;
    private final SimpleStringProperty term;
    private final SimpleStringProperty uri;
    //private final SimpleStringProperty pid;
    private final SimpleStringProperty connection;
    private final SimpleStringProperty mode;
    private final SimpleStringProperty status;

    public ServiceTableData(Integer id, Integer sl, String term, String uri, String connection, String mode, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.sl = new SimpleIntegerProperty(sl);
        this.term = new SimpleStringProperty(term);
        this.uri = new SimpleStringProperty(uri);
        //this.pid = new SimpleStringProperty(pid.toString());
        this.connection = new SimpleStringProperty(connection);
        this.mode = new SimpleStringProperty(mode);
        this.status = new SimpleStringProperty(status);
    }

    public int getSl() {
        return sl.get();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty slProperty() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl.set(sl);
    }

    public String getTerm() {
        return term.get();
    }

    public SimpleStringProperty termProperty() {
        return term;
    }

    public void setTerm(String term) {
        this.term.set(term);
    }

    public String getUri() {
        return uri.get();
    }

    public SimpleStringProperty uriProperty() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri.set(uri);
    }

    /*public String getPid() {
        return pid.get();
    }

    public SimpleStringProperty pidProperty() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid.set(pid);
    }*/

    public String getConnection() {
        return connection.get();
    }

    public SimpleStringProperty connectionProperty() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection.set(connection);
    }

    public String getMode() {
        return mode.get();
    }

    public SimpleStringProperty modeProperty() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
