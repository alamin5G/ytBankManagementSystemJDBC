package com.goonok;

import javax.security.auth.login.CredentialNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full-Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("New Password: ");
        String password = scanner.nextLine();
        if (userExist(email)){
            System.out.println("User already exists for this account!");
            return;
        }

        String registerQuery = "INSERT INTO User(full_name, email, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                System.out.println("Registration Successful!");
            }else {
                System.out.println("Registration Failed!");
            }
        }catch (SQLException s){
            System.out.println("Connection failed at - User.register() method + " + s.getMessage());
        }

    }

   ////TODO - https://www.youtube.com/watch?v=p_-GZTStkoI&list=PL9q3G4cgja6cgxDbpg3cdvnqM9iWwQtNG&index=14
    /// TIME - 14:01

    public String login() {

        return "";
    }

    private boolean userExist(String email) {

        return false;
    }
}
