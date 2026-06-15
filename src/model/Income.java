package model;
public class Income extends Transaction {
    public Income(double amount, String category, String description, String date) {
        super(amount, category, description, date);
    }
    @Override
    public String getTransactionType() {
        return "INCOME";
    }
}