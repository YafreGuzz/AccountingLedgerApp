package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class CLIApp
{
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Log> inventory = new ArrayList<>();

    public static void main(String[] args)
    {
        homeScreen();
    }

    public static void homeScreen()
    {
        System.out.println("Welcome to our Financial Transactions log: \n");
        System.out.println("What would you like to do? ");
        System.out.println("---------------------------");
        System.out.println("  1. Add Deposit");
        System.out.println("  2. Make Payment(Debit)");
        System.out.println("  3. Ledger");
        System.out.println("  4. Exit");
        System.out.print("Choice: ");

        int choice = scanner.nextInt();

        getInventory();

        switch (choice)
        {
            case 1:
                addDeposit();
                break;
            case 2:
                addDebitCard();
                break;
            case 3:
                Ledger();
                break;
            case 4:
                System.exit(0);
        }
    }
}
