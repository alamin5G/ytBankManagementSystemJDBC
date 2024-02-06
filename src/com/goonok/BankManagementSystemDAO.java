package com.goonok;

import java.sql.*;
import java.util.Scanner;

public class BankManagementSystemDAO {
    private static final String url = "jdbc:mysql://localhost:3306/bank_ms";
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
               if (scanner.nextInt() == 1){
                   acountNumber = accounts.openAccount(email);
                   System.out.println("Account Created...");
                   System.out.println("Your account Number is: " + acountNumber);
               }else {
                   break;
               }
           }

           acountNumber = accounts.getAccountNumber(email);
           int choice = 0;
           while (choice!=5){
               System.out.println();
               System.out.println("1. Withdraw Money");
               System.out.println("2. Deposit Money");
               System.out.println("3. Transfer Money");
               System.out.println("4. Check Balance");
               System.out.println("5. Logout");
               System.out.print("Enter your choice: ");
               choice = scanner.nextInt();
               switch (choice){
                   case 1:
                       accountManager.debitMoney(acountNumber);
                       break;
                   case 2:
                       accountManager.creditMoney(acountNumber);
                       break;
                   case 3:
                       accountManager.transferMoney(acountNumber);
                       break;
                   case 4:
                       accountManager.getBalance(acountNumber);
                       break;
                   case 5:
                       return;
                   default:
                       System.out.println("Enter valid choice!");
                       break;
               }
           }
       }
    }
}
