package com.wiexon.app;

import com.wiexon.tray.TrayCom;
import com.wiexon.tray.TrayComBool;
import com.wiexon.tray.TraySync;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/main.fxml"));
        fxmlLoader.setController(new Controller(primaryStage));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Modbus REST Mate");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            //Platform.exit();
        });

        primaryStage.show();

        TrayCom.setPrimaryStage(primaryStage);
        new TraySync().start();




    }


    public static void main(String[] args) {
        launch(args);
    }
}
