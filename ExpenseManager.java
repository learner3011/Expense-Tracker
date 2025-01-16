import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseManager {
    private static final String FILE_NAME = "expenses.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public void saveExpense(Expense expense) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(expense.getDate() + "," + expense.getAmount() + "," + expense.getCategory() + "," + expense.getDescription());
            bw.newLine();
        }
    }

    public List<Expense> loadExpenses() throws IOException {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return expenses;
        }

        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4); // Date, Amount, Category, Description
                if (parts.length == 4) {
                    String date = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String description = parts[3];
                    expenses.add(new Expense(amount, category, description, date));
                }
            }
        } catch (Exception e) {
            throw new IOException("File is corrupted or contains invalid data.");
        }
        return expenses;
    }

    public double getTotalForPeriod(List<Expense> expenses, String period) {
        Calendar cal = Calendar.getInstance();
        String today = DATE_FORMAT.format(cal.getTime());

        return expenses.stream()
            .filter(expense -> {
                try {
                    Date expenseDate = DATE_FORMAT.parse(expense.getDate());
                    cal.setTime(expenseDate);
                    switch (period) {
                        case "daily":
                            return expense.getDate().equals(today);
                        case "weekly":
                            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                            Date weekStart = cal.getTime();
                            cal.add(Calendar.WEEK_OF_YEAR, 1);
                            Date weekEnd = cal.getTime();
                            return expenseDate.after(weekStart) && expenseDate.before(weekEnd);
                        case "monthly":
                            return new SimpleDateFormat("yyyy-MM").format(expenseDate)
                                    .equals(today.substring(0, 7));
                        case "yearly":
                            return today.substring(0, 4).equals(expense.getDate().substring(0, 4));
                    }
                } catch (Exception ignored) {
                }
                return false;
            })
            .mapToDouble(Expense::getAmount)
            .sum();
    }
}
