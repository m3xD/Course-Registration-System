package com.example.crs_fx;

import java.sql.*;
public class Database {

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "devuser", "28101996Aa@");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
