package com.wiexon.app;

import com.wiexon.app.settings.SettingsHolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
            if (SettingsHolder.isExitOnClose()) {
                Platform.exit();
                System.exit(0);
            }
        });

        if (!SettingsHolder.isAutoMinimize()) {
            primaryStage.show();
        }

        //System tray start///////////////////////////////////////////////////////////////////////////////
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        URL url = System.class.getResource("/images/new.png");

        Image image = Toolkit.getDefaultToolkit().getImage(url);

        final TrayIcon trayIcon = new TrayIcon(image);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem openItem = new MenuItem("Open");

        openItem.addActionListener(e -> {
            Platform.runLater(() -> primaryStage.show());
        });

        MenuItem closeItem = new MenuItem("Close");

        closeItem.addActionListener(e -> {
            Platform.runLater(() -> {
                primaryStage.fireEvent(
                        new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)
                );
            });
        });

        MenuItem exitItem = new MenuItem("Exit");

        exitItem.addActionListener(e -> {
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });
        });

        //Add components to pop-up menu
        popup.add(openItem);
        popup.add(closeItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        //System tray end///////////////////////////////////////////////////////////////////////////////
    }

    public static void main(String[] args) {
        launch(args);
    }
}
