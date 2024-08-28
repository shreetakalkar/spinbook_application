package com.example.spinbook;
public class ExpenseEntry {
    private String expenseName;
    private double expenseCost;

    public ExpenseEntry(String expenseName, double expenseCost) {
        this.expenseName = expenseName;
        this.expenseCost = expenseCost;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public double getExpenseCost() {
        return expenseCost;
    }
}
