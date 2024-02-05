package com.goonok;

import java.sql.Connection;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public boolean accountExist(String email) {

        return false;
    }

    public long openAccount(String email) {

        return 0L;
    }
}
