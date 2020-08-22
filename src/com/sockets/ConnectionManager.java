package com.sockets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String urlstring = "jdbc:mysql://localhost:3306/server";
    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String username = "meave";
    private static final String password = "test";
    private static Connection con = null;
    public static Connection getConnection(){
        System.out.println("Connecting to database...");
        try {
            con = DriverManager.getConnection(urlstring, username, password);
            System.out.println("Connection established.");
            return con;
        } catch (SQLException ex) {
            System.out.println("Failed to create the database connection: " + ex.getMessage());
            ex.getStackTrace();

            return null;
        }
    }
}