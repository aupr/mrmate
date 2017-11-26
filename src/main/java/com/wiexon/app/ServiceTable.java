package com.wiexon.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;

public class ServiceTable {
    private static ObservableList<ServiceTableData> tableDataList = null;

    public static ObservableList<ServiceTableData> getTableDataList() {
        return tableDataList;
    }

    public static int load(TableView tableView) {
        tableDataList = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            String dbUrl = "jdbc:sqlite:Base.db";


            Connection con = null;
            Statement state = null;
            ResultSet rs = null;


            try {
                con = DriverManager.getConnection(dbUrl);
                state = con.createStatement();
                rs = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='service'");

                if (!rs.next()) {
                    System.out.println("Service Table not exists! Creating New table...");
                    state.execute("CREATE TABLE service(id INTEGER PRIMARY KEY, serviceName VARCHAR(20), uri VARCHAR(20), connectionType VARCHAR(30), responseTimeout INT, host VARCHAR(30), port VARCHAR(10), connectionTimeout INT, comport VARCHAR(10), baudRate VARCHAR(30), dataBits VARCHAR(30), parityBits VARCHAR(30), stopBits VARCHAR(30), mode VARCHAR(20), modeView VARCHAR(20))");
                } else {
                    System.out.println("Table exists fetching data!");
                    ResultSet res = state.executeQuery("SELECT * FROM service");
                    int i=0;
                    while (res.next()) {
                        i++;
                        tableDataList.add(new ServiceTableData(res.getInt("id"), i, res.getString("serviceName"), res.getString("uri"), 1010, res.getString("connectionType"), res.getString("modeView"), "ok"));
                    }
                    tableView.setItems(tableDataList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
