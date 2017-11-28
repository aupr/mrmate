package com.wiexon.tray;

import javafx.stage.Stage;

public class TrayCom {
    private static Stage primaryStage = null;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        TrayCom.primaryStage = primaryStage;
    }

}
