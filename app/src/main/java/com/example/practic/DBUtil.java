package com.example.practic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil{
    private static Connection conn;
    private static Statement stmt;
    private static final String URL = "jdbc: mysql: //185.251.91.163:3306/Practic";
    private static final String USERNAME = "dedman";
    private static final String PASSWORD = "Hdleo#-Yeb45";
    static{
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
