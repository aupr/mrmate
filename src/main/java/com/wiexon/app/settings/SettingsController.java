package com.wiexon.app.settings;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class SettingsController {
    public boolean isMake = false;

    @FXML
    public JFXTextField settingsPort;

    @FXML
    public JFXCheckBox settingsAutoStartService;

    @FXML
    public JFXCheckBox settingsAutoMinimize;

    @FXML
    public JFXCheckBox settingsExitOnClose;

    @FXML
    void saveAndHide(ActionEvent event) {
        this.isMake = true;
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void initialize(){
        settingsPort.setText(String.format("%d", SettingsHolder.getPort()));
        settingsAutoStartService.setSelected(SettingsHolder.isAutoStartService());
        settingsAutoMinimize.setSelected(SettingsHolder.isAutoMinimize());
        settingsExitOnClose.setSelected(SettingsHolder.isExitOnClose());

        // validation code start

    }
}
