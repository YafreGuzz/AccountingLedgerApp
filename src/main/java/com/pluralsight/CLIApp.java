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
        while(true)
        {
            System.out.println("\nWelcome to our Account Ledger Application log:");
            System.out.println("What would you like to do? ");
            System.out.println("---------------------------\n");
            System.out.println("  [D] Add Deposit");
            System.out.println("  [P] Make Payment(Debit)");
            System.out.println("  [L] Ledger");
            System.out.println("  [X] Exit \n");
            System.out.print("Enter Choice: ");

            String choice = scanner.nextLine();

            getInventory();

            switch (choice) {
                case "D", "d":
                    addDeposit();
                    break;
                case "P", "p":
                    addDebitCard();
                    break;
                case "L", "l":
                    Ledger();
                    break;
                case "X", "x":
                    System.exit(0);
                default:
                    System.out.println("This is not a valid response, \nplease try again.");
            }
        }
    }

    public static void  Ledger()
    {
        while (true)
        {
            System.out.println("\n Welcome to the Ledger");
            System.out.println("-------------------------\n");
            System.out.println("  [A] All");
            System.out.println("  [D] Deposits");
            System.out.println("  [P] Payments");
            System.out.println("  [R] Reports");
            System.out.println("  [H] Home \n");
            System.out.print("Enter Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "A", "a":
                    displayAllEntries();
                    break;
                case "D", "d":
                    deposits();
                    break;
                case "P", "p":
                    payments();
                    break;
                case "R", "r":
                    reports();
                    break;
                case "H", "h":
                    homeScreen();
                    break;
                default:
                    System.out.println("This is not a valid answer, try again!");
            }
        }

    }

    public static void reports()
    {
        while (true) {
            System.out.println("\n Custom Search: ");
            System.out.println("-------------------\n");
            System.out.println("  1. Month To Date");
            System.out.println("  2. Previous Month");
            System.out.println("  3. Year To Date");
            System.out.println("  4. Previous Year");
            System.out.println("  5. Search by Vendor");
            System.out.println("  6. Custom Search");
            System.out.println("  0. Back \n");
            System.out.print("Enter Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
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
                case 6:
//                    customSearch();
                case 0:
                    Ledger();
                    break;
                default:
                    System.out.println("This is not a valid response, try again!");

            }
        }
    }

    public static void addDeposit()
    {

        try
        {
            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            String input;

            System.out.println("\nMake a deposip\n (Type 'x' at anytime to cancel transaction!)");
            System.out.println("---------------- \n");

            System.out.print("Enter a description: ");
            String description = scanner.nextLine();
            cancelTransaction(description);

            System.out.print("Enter Vendor: ");
            String vendor = scanner.nextLine();
            cancelTransaction(vendor);

            System.out.print("Enter Amount: ");
            double amount = scanner.nextDouble();
            String amount2 = Double.toString(amount);
            cancelTransaction(amount2);

            System.out.println("\n --------------- \n");
            System.out.println("Payment added successfully!");

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

            System.out.println("\nEnter your Credit Card information: \n(Type 'x' at anytime to cancel transaction!");
            System.out.println("------------------------------------- \n");
            System.out.print("Enter the card holder name: ");
            String name = scanner.nextLine();
            cancelTransaction(name);

            System.out.print("Credit Card Number(ex: 1234 5678 9876 5432): ");
            String creditCard = scanner.nextLine();
            cancelTransaction(creditCard);

            System.out.print("Enter Expiration Date: ");
            String expireDate = scanner.nextLine();
            cancelTransaction(expireDate);

            System.out.print("Enter CVC(Three digit number): ");
            int CVCInput = scanner.nextInt();
            String CVC = Integer.toString(CVCInput);
            cancelTransaction(CVC);

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

            if (inventory.isEmpty())
            {
                while ((readerInput = bufReader.readLine()) != null) {
                    String[] barIndex = readerInput.split(Pattern.quote("|"));
                    String date = barIndex[0];
                    String time = barIndex[1];
                    String description = barIndex[2];
                    String vendor = barIndex[3];
                    double amount = Double.parseDouble(barIndex[4]);

                    inventory.add(new Log(date, time, description, vendor, amount));
                }
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
        boolean foundNegative = false;

        for (Log i : inventory)
        {
            if (i.getAmount() < 0)
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
                foundNegative = true;
            }
        }
        if(!foundNegative)
        {
            System.out.println("No payments with negative amounts found.");
        }
        backToLedger();
    }


    public static void monthToDate()
    {
        boolean foundMonth = false;
        LocalDate date = LocalDate.now();

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            if(time.getMonth() == date.getMonth())
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
                foundMonth = true;
            }
        }
        if (!foundMonth)
        {
            System.out.println("There's nothing from this month!");
        }
        backToReports();
    }


    public static void previousMonth()
    {
        boolean foundPreviousMonth = false;

        LocalDate date = LocalDate.now();
        LocalDate previousMonth = date.minusMonths(1);

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            int month = time.getMonthValue();
            int datemonth = previousMonth.getMonthValue();

            if(month == datemonth)
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
                foundPreviousMonth = true;
            }
        }
        if (!foundPreviousMonth)
        {
            System.out.println("There's nothing from the previous month!");
        }
        backToReports();
    }


    public static void yearToDate()
    {
        boolean foundYear = false;
        LocalDate date = LocalDate.now();

        for(Log i : inventory)
        {
            LocalDate time = LocalDate.parse(i.getDate());
            if(time.getYear() == date.getYear())
            {
                System.out.println("-----------------------------");
                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
                System.out.println("-----------------------------");
                foundYear = true;
            }
        }
        if(!foundYear)
        {
            System.out.println("There's nothing from this year!");
        }
        backToReports();
    }


    public static void previousYear()
    {
        boolean foundPreviosYear = false;
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
                foundPreviosYear = true;
            }
        }
        if(!foundPreviosYear)
        {
            System.out.println("There's nothing from the previous years!");
        }
        backToReports();
    }


    public static void searchByVendor()
    {
        boolean foundVendor = false;
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
                foundVendor = true;
            }
        }
        if (!foundVendor)
        {
            System.out.println("There is no such vendor!");
        }
        backToReports();

    }

//    public static void customSearch()
//    {
//        System.out.println("Start Date (yyyy-mm-dd): ");
//        String startDate = scanner.nextLine();
//        System.out.println("End Date (yyyy-mm-dd): ");
//        String endDate = scanner.nextLine();
//        System.out.println("Description: ");
//        String description = scanner.nextLine();
//        System.out.println("Vendor: ");
//        String vendor = scanner.nextLine();
//        System.out.println("Amount: ");
//        double amount = scanner.nextDouble();
//
//        for(Log i : inventory)
//        {
//            LocalDate time = LocalDate.parse(i.getDate());
//            if(time == LocalDate.parse(startDate) && time == LocalDate.parse(endDate) && i.getDescription().equalsIgnoreCase(description) && i.getVendor().equalsIgnoreCase(vendor) && i.getAmount() == amount)
//            {
//                System.out.println("-----------------------------");
//                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
//                System.out.println("-----------------------------");
//            }
//            else if (startDate.isEmpty() && time == LocalDate.parse(endDate) && i.getDescription().equalsIgnoreCase(description) && i.getVendor().equalsIgnoreCase(vendor) && i.getAmount() == amount)
//            {
//                System.out.println("-----------------------------");
//                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
//                System.out.println("-----------------------------");
//            }
//            else if (startDate.isEmpty() && endDate.isEmpty() && description.isEmpty() && i.getVendor().equalsIgnoreCase(vendor) && i.getAmount() == amount)
//            {
//                System.out.println("-----------------------------");
//                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
//                System.out.println("-----------------------------");
//            }
//            else if (startDate.isEmpty() && endDate.isEmpty() && description.isEmpty() && vendor.isEmpty() && i.getAmount() == amount)
//            {
//                System.out.println("-----------------------------");
//                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
//                System.out.println("-----------------------------");
//            }
//            else if (startDate.isEmpty() && endDate.isEmpty() && description.isEmpty() && vendor.isEmpty() && amount == 0)
//            {
//                System.out.println("-----------------------------");
//                System.out.printf("Description: %s Vendor: %s Amount: $%.2f \n", i.getDescription(), i.getVendor(), i.getAmount());
//                System.out.println("-----------------------------");
//            }
//        }
//
//    }

    public static void  backToReports()
    {
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
        System.out.println("Go Back to Ledger?: yes/no");
        String choice = scanner.nextLine();

        if(choice.equalsIgnoreCase("yes"))
        {
            Ledger();
        }
        else
        {
            System.exit(0);
        }
        scanner.nextLine();
    }
    public static void  cancelTransaction(String d)
    {
        if(d.equalsIgnoreCase("x"))
        {
            System.out.println("Transaction Cancelled Successfully!");
            System.out.println("...............");
            homeScreen();
        }
    }
}
