package ru.mirea.BalanceService;

public class Balance {
    private int user_id;
    double balance; //количество денег в базовой валюте
    String currency_name;

    public Balance(int user_id, double balance, String current_balance) {
        this.user_id = user_id;
        this.balance = balance;
        this.currency_name = current_balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public double getBalance() {
        return balance;
    }

    public String  getCurrency_name() {
        return currency_name;
    }
}
