package com.wiexon.tray;

public class TrayComBool {
    private static boolean needToOpen = false;

    public static boolean isNeedToOpen() {
        return needToOpen;
    }

    public static void setNeedToOpen(boolean needToOpen) {
        TrayComBool.needToOpen = needToOpen;
    }
}
