package com.goonok;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Scanner scanner;
    private Connection connection;

    public AccountManager(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void debitMoney(long accountNumber){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security PIN: ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accountNumber != 0){
                String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(1, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    double currentBalance = resultSet.getDouble("balance");
                    if (amount<=currentBalance){
                        String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debitQuery);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setDouble(2, accountNumber);
                        int rowsAffected = preparedStatement1.executeUpdate();

                        if (rowsAffected > 0){
                            System.out.println("Taka. " + amount + " debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance!");
                    }
                }else {
                    System.out.println("Invalid PIN!");
                }
            }
        }catch (SQLException s){
            System.err.println("Connection Problem at debitMoneyI() - " + s.getMessage());
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void creditMoney(long accountNumber){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security PIN: ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accountNumber != 0) {
                String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    try{
                        PreparedStatement preparedStatement1 = connection.prepareStatement(creditQuery);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, accountNumber);
                        int rowAffected = preparedStatement1.executeUpdate();

                        if (rowAffected > 0){
                            System.out.println("Taka " +  amount +  " credited successfully!");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    }catch (SQLException s){
                        System.err.println("Connection Failed at - creditMoney() - " + s.getMessage() );
                    }
                }else {
                    System.out.println("Invalid Security PIN");
                }
            }
        }catch (SQLException s){
            System.err.println("Connection failed at creditMoney()  - " + s.getMessage());
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

public void transferMoney(long senderAccountNumber){
        scanner.nextLine();
    System.out.print("Enter beneficiary Account No: ");
    long beneficiaryAccountNumber = scanner.nextLong();
    System.out.print("Enter amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.print("Enter Security PIN: ");
    String securityPIN = scanner.nextLine();

    try{
        connection.setAutoCommit(false);
        if (senderAccountNumber != 0 && beneficiaryAccountNumber!=0){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setDouble(1, senderAccountNumber);
            preparedStatement.setString(2, securityPIN);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                double currentBalance = resultSet.getDouble("balance");
                if (amount<=currentBalance){
                    String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement creditStatement = connection.prepareStatement(creditQuery);
                    PreparedStatement debitStatement  = connection.prepareStatement(debitQuery);

                    creditStatement.setDouble(1, amount);
                    creditStatement.setLong(2, beneficiaryAccountNumber);
                    debitStatement.setDouble(1, amount);
                    debitStatement.setLong(2, senderAccountNumber);
                    int rowAffectedInDebit = debitStatement.executeUpdate();
                    int rowAffectedInCredit = creditStatement.executeUpdate();

                    if (rowAffectedInDebit > 0 && rowAffectedInCredit >0){
                        System.out.println("Transaction Success!");
                        System.out.println("Taka " + amount + " Transferred Successfully!");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else {
                    System.out.println("Insufficient Balance!");
                }
            }else {
                System.out.println("Invalid security PIN!");
            }
        }else {
            System.out.println("No Account Found!");
        }
    }catch (SQLException s){
        System.err.println("Connection Failed at transferMoney() - " + s.getMessage());
    }

    try {
        connection.setAutoCommit(true);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}


    public void getBalance(long accountNumber){
        scanner.nextLine();
        System.out.print("Enter Security PIN: ");
        String securityPIN = scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPIN);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance = " + balance);
            }else {
                System.out.println("Invalid PIN!");
            }
        }catch (SQLException s){
            System.err.println("Connection Failed at getBalance() - " + s.getMessage());
        }
    }



}
