package com.wiexon.app.JFValidator;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationLogic {

    public static boolean isValidRangeInt (int value, int lowerLimit, int upperLimit) {
        return (value >= lowerLimit && value <= upperLimit);
    }

    public static boolean isValidComPortAddress (String comPortName) {
        String comPortPattern = "COM([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";//"(COM(\\d|\\d{2}|\\d{3}))";
        Pattern pattern = Pattern.compile(comPortPattern);
        Matcher matcher = pattern.matcher(comPortName);
        return matcher.matches();
    }

    public static boolean isDuplicateComPortAddress (String comPortName) {
        Connection con = null;
        PreparedStatement preps = null;
        ResultSet res = null;
        boolean duplicateComport = false;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Base.db");
            preps = con.prepareStatement("SELECT * FROM service WHERE comport=?");
            preps.setString(1, comPortName);
            res = preps.executeQuery();

            if (res.next()) {
                duplicateComport = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return duplicateComport;
    }

    public static boolean isValidIpAddress (String ipAddress) {
        String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isValidInteger (String value) {
        String integerPattern = "\\d+";

        Pattern pattern = Pattern.compile(integerPattern);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

    public static boolean isValidName (String name) {
        String integerPattern = "^([A-Za-z1-9 _@+-]{3,20})$";

        Pattern pattern = Pattern.compile(integerPattern);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    public static boolean isValidHostPort (String hostPort) {
        boolean status = false;
        if (isValidInteger(hostPort)) {
            int hp = Integer.parseInt(hostPort);
            if (hp >= 0 && hp <= 65535) status = true;
        }
        return status;
    }

    public static boolean isValidTime (String time) {
        boolean status = false;
        if (isValidInteger(time)) {
            int tm = Integer.parseInt(time);
            if (tm >= 10 && tm <= 60000) status = true;
        }
        return status;
    }

    public static boolean isValidUri (String uri) {
        String integerPattern = "^([a-z1-9_]{3,15})$";

        Pattern pattern = Pattern.compile(integerPattern);
        Matcher matcher = pattern.matcher(uri);

        return matcher.matches();
    }

    public static boolean isDuplicateUri (String uri) {
        Connection con = null;
        PreparedStatement preps = null;
        ResultSet res = null;
        boolean dublicateUri = false;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Base.db");
            preps = con.prepareStatement("SELECT * FROM service WHERE uri=?");
            preps.setString(1, uri);
            res = preps.executeQuery();

            if (res.next()) {
                dublicateUri = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dublicateUri;
    }
}
