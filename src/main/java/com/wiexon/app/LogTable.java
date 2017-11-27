package com.wiexon.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogTable {
    private static ObservableList<LogTableData> dataList = FXCollections.observableArrayList();

    public static ObservableList<LogTableData> getDataList() {
        return dataList;
    }
}
