package com.wiexon.tray;

import java.awt.*;
import java.net.URL;

public class TraySync extends Thread {
    @Override
    public void run() {
        /////////////////////////////////////////////////////////////////////////////////
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
        MenuItem aboutItem = new MenuItem("About");

        aboutItem.addActionListener(e -> {
            System.out.println("tray working");
            //primaryStage.show();
            TrayComBool.setNeedToOpen(true);
        });

        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        /////////////////////////////////////////////////////////////////////////////////
        while (true){
            System.out.println("running thread");

            if (TrayComBool.isNeedToOpen()) {
                TrayCom.getPrimaryStage().hide();
                TrayComBool.setNeedToOpen(false);
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
