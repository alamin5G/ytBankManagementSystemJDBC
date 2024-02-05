package com.goonok;
import java.sql.Connection;
import java.util.Scanner;

public class AccountManager {
    private Scanner scanner;
    private Connection connection;

    public AccountManager(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }


}
