package com.goonok;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BankManagementSystemDAO systemDAO = new BankManagementSystemDAO();


        while (true){
            System.out.println("***** Welcome to BANKING SYSTEM ******");
            System.out.println("-----------------------------------------");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = input.nextInt();
            switch (choice){
                case 1:
                    systemDAO.register();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;
                case 2:
                    systemDAO.login();

                    break;
                case 3:
                    System.out.println("Thank you for using banking system..");
                    System.out.println("Exiting System");
                    return;
                default:
                    System.out.println("Enter correct choice!!");
                    break;

            }
        }

         }
}