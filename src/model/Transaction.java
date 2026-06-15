package model;
public abstract class Transaction {
    protected double amount;
    protected String category;
    protected String description;
    protected String date;
    public Transaction(double amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public abstract String getTransactionType();
}