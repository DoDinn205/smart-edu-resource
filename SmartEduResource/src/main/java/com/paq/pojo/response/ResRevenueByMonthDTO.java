package com.paq.pojo.response;

public class ResRevenueByMonthDTO {

    private int year;
    private int month;
    private long revenue;
    private long transactions;

    public ResRevenueByMonthDTO() {
    }

    public ResRevenueByMonthDTO(int year, int month, long revenue, long transactions) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
        this.transactions = transactions;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getTransactions() {
        return transactions;
    }

    public void setTransactions(long transactions) {
        this.transactions = transactions;
    }
}
