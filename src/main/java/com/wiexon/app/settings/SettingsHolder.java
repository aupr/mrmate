package com.wiexon.app.settings;

import java.sql.*;

public class SettingsHolder {
    private static int port;
    private static boolean autoStartService;
    private static boolean autoMinimize;
    private static boolean exitOnClose;

    public static int getPort() {
        return port;
    }

    public static boolean isAutoStartService() {
        return autoStartService;
    }

    public static boolean isAutoMinimize() {
        return autoMinimize;
    }

    public static boolean isExitOnClose() {
        return exitOnClose;
    }

    public static void loadSettings() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbUrl = "jdbc:sqlite:Base.db";

            Connection con = null;
            Statement state = null;
            ResultSet rs = null;

            try {
                con = DriverManager.getConnection(dbUrl);
                state = con.createStatement();
                rs = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='settings'");

                if (!rs.next()) {
                    System.out.println("Settings Table not exists! Creating New table...");
                    state.execute("CREATE TABLE settings(port INT, autoStartService INT, autoMinimize INT, exitOnClose INT)");
                    state.execute("INSERT INTO settings VALUES (1983, 0, 0, 0)");
                }

                System.out.println("Settings Table exists fetching data!");
                ResultSet res = state.executeQuery("SELECT * FROM settings");
                if (res.next()) {
                    SettingsHolder.port = res.getInt("port");
                    SettingsHolder.autoStartService = res.getBoolean("autoStartService");
                    SettingsHolder.autoMinimize = res.getBoolean("autoMinimize");
                    SettingsHolder.exitOnClose = res.getBoolean("exitOnClose");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void updateSettings(int port, boolean autoStartService, boolean autoMinimize, boolean exitOnClose) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbUrl = "jdbc:sqlite:Base.db";


            Connection con = null;
            Statement state = null;

            try {
                con = DriverManager.getConnection(dbUrl);
                state = con.createStatement();

                int _autoStartService = autoStartService?1:0;
                int _autoMinimize = autoMinimize?1:0;
                int _exitOnClose = exitOnClose?1:0;

                state.execute("UPDATE settings SET port="+port+", autoStartService="+_autoStartService+", autoMinimize="+_autoMinimize+", exitOnClose="+_exitOnClose);
                loadSettings();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
