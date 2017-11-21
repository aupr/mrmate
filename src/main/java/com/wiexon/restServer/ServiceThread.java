package com.wiexon.restServer;

import javafx.scene.control.Button;

public class ServiceThread extends Thread{

    String name;
    Button btn;

    public ServiceThread(String name, Button btn) {
        this.btn = btn;
        this.name = name;
        this.start();
    }

    @Override
    public void run() {
        int count = 1;
        while (!btn.isDisable()) {
            System.out.println(name+":"+count+" "+btn.isDisable());
            count++;
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
