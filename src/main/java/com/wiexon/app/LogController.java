package com.wiexon.app;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LogController {
    private ObservableList<LogTableData> dataList = null;

    @FXML
    private TableView<LogTableData> logTable;

    @FXML
    private TableColumn sl, dnt, message;

    public LogController(ObservableList<LogTableData> dataList) {
        this.dataList = dataList;
    }

    @FXML
    private void initialize() {
        sl.setCellValueFactory(new PropertyValueFactory<LogTableData, String>("logId"));
        dnt.setCellValueFactory(new PropertyValueFactory<LogTableData, String>("logTnD"));
        message.setCellValueFactory(new PropertyValueFactory<LogTableData, String>("logMessage"));

        logTable.setItems(dataList);
    }

}
