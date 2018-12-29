package ru.mirea.BalanceService;

public class Currency {
    //Коэффициент от основной валюты. Например, для рубля это 1/70
    private double multiplicator;
    private String type;

    public Currency(double multiplicator, String type) {
        this.multiplicator = multiplicator;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public double getMultiplicator() {
        return multiplicator;
    }
}