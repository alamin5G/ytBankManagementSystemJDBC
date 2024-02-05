package com.goonok;

import java.sql.*;

public class BankManagementSystemDAO {
    private static final String url = "jdbc:mysql://localhost:3306/testdb";
    private static final String user = "root";
    private static final String pass = "252646";
    private Connection connection;

    public BankManagementSystemDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection established success!");
            connection.setAutoCommit(false);
        }catch (ClassNotFoundException c){
            System.err.println("Driver not found!");
            System.err.println(c.getMessage());
        }catch (SQLException s){
            System.out.println("Connection Failed! - " + s.getMessage());
        }
    }

    public void startBatch(){
        try {
            Statement statement = connection.createStatement();
            statement.addBatch("INSERT INTO ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
