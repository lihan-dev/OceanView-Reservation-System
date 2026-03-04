package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    // ✅ CHANGE THESE 3 THINGS TO MATCH YOUR MYSQL
    private static final String URL  = "jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "admin123";  // if your MySQL has password, put it here

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL driver
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed: " + e.getMessage(), e);
        }
    }
}
