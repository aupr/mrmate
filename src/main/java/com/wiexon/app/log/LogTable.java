package com.wiexon.app.log;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogTable {
    private static ObservableList<LogTableData> dataList = FXCollections.observableArrayList();

    public static ObservableList<LogTableData> getDataList() {
        return dataList;
    }
}
