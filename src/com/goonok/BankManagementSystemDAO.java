package com.goonok;

import java.sql.*;
import java.util.Scanner;

public class BankManagementSystemDAO {
    private static final String url = "jdbc:mysql://localhost:3306/testdb";
    private static final String dbUser = "root";
    private static final String pass = "252646";
    private Connection connection;
    private Scanner scanner;
    private User user;
    private Accounts accounts;
    private AccountManager accountManager;
    private String email;
    private long acountNumber;


    public BankManagementSystemDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!");
            connection = DriverManager.getConnection(url, dbUser, pass);
            System.out.println("Connection established success!");
        }catch (ClassNotFoundException c){
            System.err.println("Driver not found!");
            System.err.println(c.getMessage());
        }catch (SQLException s){
            System.out.println("Connection Failed! - " + s.getMessage());
        }
        scanner = new Scanner(System.in);
        user = new User(connection, scanner);
        accounts = new Accounts(connection, scanner);
        accountManager = new AccountManager(connection, scanner);

    }

    public void register(){
        user.register();

    }

    public void login(){
       email = user.login();
       while (email!=null){
           System.out.println();
           System.out.println("User logged in...");
           if (!accounts.accountExist(email)){
               System.out.println();
               System.out.println("1. Open a new Bank Account");
               System.out.println("2. Exit");
               try{
                   if (scanner.nextInt() == 1){
                       acountNumber = accounts.openAccount(email);
                       System.out.println("Account Created...");
                       System.out.println("Your account Number is: " + acountNumber);
                   }else {
                       System.out.println("Entered");
                       break;
                   }
               }catch (RuntimeException r){
                   throw new RuntimeException();
               }
           }
       }
    }
}
