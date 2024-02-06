package com.goonok;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.IllegalFormatCodePointException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.Stack;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }


    public long openAccount(String email) {
        if (!accountExist(email)){
            String openAccountQuery = "INSERT INTO accounts(account_number, full_name, email,balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter full name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter 4 digit PIN: ");
            String securityPin = scanner.nextLine();

            try {
                long accountNumber = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(openAccountQuery);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securityPin);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows> 0){
                    return accountNumber;
                }else {
                    System.out.println("Account creation failed!");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return 0L;
    }

    public long generateAccountNumber() {
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()){
                long lastAccountNumber = resultSet.getLong("account_number");
                return lastAccountNumber+1;
            }else {
                return 10000100;
            }
        }catch (SQLException s){
            System.err.println("Connection failed at generateAccountNumber() -" + s.getMessage());
        }
        return 10000100;
    }

    public long getAccountNumber(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException s){
            System.err.println("Connection failed at getAccountNumber() - " + s.getMessage());;
        }
        return 0;
    }

    public boolean accountExist(String email) {
        String checkQuery = "SELECT account_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException s){
            System.err.println("Connection failed at accountExist() - " + s.getMessage());
        }
        return false;
    }
}
