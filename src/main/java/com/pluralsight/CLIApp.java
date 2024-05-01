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
    public static void  Ledger()
    {
        System.out.println("\n Welcome to the Ledger");
        System.out.println("-------------------------");
        System.out.println("  1. All");
        System.out.println("  2. Deposits");
        System.out.println("  3. Payments");
        System.out.println("  4. Reports");
        System.out.println("  5. Home");
        System.out.print("choose: ");

        int choice = scanner.nextInt();

        switch (choice)
        {
            case 1:
                displayAllEntries();
                break;
            case 2:
                deposits();
                break;
            case 3:
                payments();
                break;
            case 4:
                reports();
                break;
            case 5:
                homeScreen();
                break;
        }

    }

    public static void reports()
    {
        System.out.println("Custom Search: ");
        System.out.println("-------------------");
        System.out.println("  1. Month To Date");
        System.out.println("  2. Previous Month");
        System.out.println("  3. Year To Date");
        System.out.println("  4. Previous Year");
        System.out.println("  5. Search by Vendor");
        System.out.println("  0. Back");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();

        switch (choice)
        {
            case 1:
                monthToDate();
                break;
            case 2:
                previousMonth();
                break;
            case 3:
                yearToDate();
                break;
            case 4:
                previousYear();
                break;
            case 5:
                searchByVendor();
                break;
            case 0:
                Ledger();
                break;
        }
    }

    public static void addDeposit()
    {

        try
        {
            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            String input;

            scanner.nextLine();
            System.out.println("Make A Deposit ");
            System.out.println("----------------");
            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Vendor: ");
            String vendor = scanner.nextLine();
            System.out.print("Amount: ");
            double amount = scanner.nextDouble();

            inventory.add(new Log(null, null, description, vendor, amount));

            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss|");
            String formattedDate = time.format(fmt);

            input = String.format("\n" + formattedDate + description + "|" + vendor + "|" + amount);
            bufWriter.write(input);

            bufWriter.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        backToHomeScreen();
    }

    public static void addDebitCard()
    {
        try
        {
            FileWriter fileWriter = new FileWriter("src/main/resources/debitInformation.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);

            System.out.println("Enter your Credit Card information: ");
            System.out.println("-------------------------------------");
            System.out.print("Enter the card holder name: ");
            scanner.nextLine();
            String name = scanner.nextLine();
            System.out.print("Credit Card Number(ex: 1234 5678 9876 5432): ");
            String creditCard = scanner.nextLine();
            System.out.print("Enter expiration date: ");
            String expireDate = scanner.nextLine();
            System.out.print("Enter CVC(Three digit number): ");
            int CVCInput = scanner.nextInt();

            String CVC = Integer.toString(CVCInput);

            bufWriter.write(name + "|");
            bufWriter.write(creditCard + "|");
            bufWriter.write(expireDate + "|");
            bufWriter.write(CVC + "\n");

            bufWriter.close();

            backToHomeScreen();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void displayAllEntries()
    {
        Collections.reverse(inventory);

        for (Log object : inventory)
        {
            System.out.println(object);
        }
        backToLedger();
    }


    public static ArrayList<Log> getInventory()
    {
        try
        {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);
            String readerInput;

            while ((readerInput = bufReader.readLine()) != null)
            {
                String[] barIndex = readerInput.split(Pattern.quote("|"));
                String date = barIndex[0];
                String time = barIndex[1];
                String description = barIndex[2];
                String vendor = barIndex[3];
                double amount = Double.parseDouble(barIndex[4]);

                inventory.add(new Log(date, time, description, vendor, amount));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return inventory;
    }


    public static void deposits()
    {
        double sum = 0;
        for(Log p : inventory)
        {
            sum += p.getAmount();
        }
        System.out.println("------------------------------------------");
        System.out.printf("The Total Deposits in your account are: %.2f \n", sum);

        backToLedger();
    }


    public static void payments()
    {
        for(Log i : inventory)
        {
            if(i.getAmount() < 0 )
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToLedger();
    }


    public static void monthToDate()
    {
        LocalDate date = LocalDate.now();

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            if(time.getMonth() == date.getMonth())
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToReports();
    }


    public static void previousMonth()
    {
        LocalDate date = LocalDate.now();
        LocalDate previousMonth = date.minusMonths(1);

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            if(time.isBefore(previousMonth))
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToReports();
    }


    public static void yearToDate()
    {
        LocalDate date = LocalDate.now();

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            if(time.getYear() == date.getYear())
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToReports();
    }


    public static void previousYear()
    {
        LocalDate date = LocalDate.now();

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            int i1 = date.getYear() - 1;
            if (i1 == time.getYear())
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToReports();
    }


    public static void searchByVendor()
    {
        scanner.nextLine();

        System.out.println("Who's the Vendor? ");
        System.out.print("Answer: ");
        String name = scanner.nextLine();

        for(Log i : inventory)
        {
            if(name.equalsIgnoreCase(i.getVendor()))
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
            }
        }
        backToReports();

    }


    public static void  backToReports()
    {
        scanner.nextLine();
        System.out.println("Go back to Reports? ");
        System.out.print("Answer: ");

        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("yes"))
        {
            reports();
        }
        else
            System.exit(0);
    }


    public static void backToHomeScreen()
    {
        scanner.nextLine();
        System.out.println("Go Back to Home Screen?: yes/no");
        String choice = scanner.nextLine();

        if(choice.equalsIgnoreCase("yes"))
        {
            homeScreen();
        }
        else if (choice.equalsIgnoreCase("no")) {
            System.exit(0);
        }
    }


    public static void backToLedger()
    {
        scanner.nextLine();
        System.out.println("Go Back to Ledger?: yes/no");
        String choice = scanner.nextLine();

        if(choice.equalsIgnoreCase("yes"))
        {
            Ledger();
        }
        else if (choice.equalsIgnoreCase("no")) {
            System.exit(0);
        }
    }
}
