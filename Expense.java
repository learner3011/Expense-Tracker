import java.io.Serializable;

public class Expense implements Serializable {
    private double amount;
    private String category;
    private String description;
    private String date; // Format: yyyy-MM-dd

    public Expense(double amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Amount: " + amount + ", Category: " + category + ", Description: " + description;
    }
}
