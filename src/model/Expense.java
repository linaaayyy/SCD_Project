package model;
public class Expense extends Transaction {
    public Expense(double amount, String category, String description, String date) {
        super(amount, category, description, date);
    }
    @Override
    public String getTransactionType() {
        return "EXPENSE";
    }
}