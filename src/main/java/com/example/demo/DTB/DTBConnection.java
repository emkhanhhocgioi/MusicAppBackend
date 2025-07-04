package com.example.demo.DTB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DTBConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=YourDB";
        String user = "yourUsername";
        String password = "yourPassword";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            // Thực hiện truy vấn...
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
