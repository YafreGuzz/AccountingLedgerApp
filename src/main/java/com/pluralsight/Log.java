package com.pluralsight;

public class Log
{
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    public Log(String date, String time, String description, String vendor, double amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString()
    {
        return "Description: " + description + "\n" + " Vendor = " + vendor + "\n" + " Amount = $" + amount + "\n----------------------" ;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
}

