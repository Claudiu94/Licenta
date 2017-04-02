package com.mainPackage.database;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;
/**
 * Created by claudiu on 02.04.2017.
 */

public class ConnectionToDB {

    private Connection conn = null;

    @PostConstruct
    public void inti() {
        String URL = "jdbc:mysql://localhost:3306/LICENTA";
        Properties info = new Properties();

        info.put("user", "root");
        info.put("password", "root");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(URL, info);

            Statement stmt = conn.createStatement();
            String query="SELECT * FROM Users";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                System.out.print(rs.getString("PersonID") +" ");
                System.out.print(rs.getString(2) +" ");
                System.out.print(rs.getString(3) +" ");

            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Connection Created!!!");
    }

    public Connection getConnection() {
        return conn;
    }
}
