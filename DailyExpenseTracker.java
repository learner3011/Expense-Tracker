import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DailyExpenseTracker extends JFrame {
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField descriptionField;
    private JTextArea outputArea;

    private ExpenseManager manager;

    public DailyExpenseTracker() {
        manager = new ExpenseManager();
        setTitle("Daily Expense Tracker");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(e -> addExpense());
        inputPanel.add(addButton);

        JButton viewButton = new JButton("View All Expenses");
        viewButton.addActionListener(e -> viewAllExpenses());
        inputPanel.add(viewButton);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 4));
        JButton dailyButton = new JButton("Daily Total");
        dailyButton.addActionListener(e -> viewSummary("daily"));
        summaryPanel.add(dailyButton);

        JButton weeklyButton = new JButton("Weekly Total");
        weeklyButton.addActionListener(e -> viewSummary("weekly"));
        summaryPanel.add(weeklyButton);

        JButton monthlyButton = new JButton("Monthly Total");
        monthlyButton.addActionListener(e -> viewSummary("monthly"));
        summaryPanel.add(monthlyButton);

        JButton yearlyButton = new JButton("Yearly Total");
        yearlyButton.addActionListener(e -> viewSummary("yearly"));
        summaryPanel.add(yearlyButton);

        add(inputPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Load and display all expenses on startup
        viewAllExpenses();
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            String description = descriptionField.getText();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            Expense expense = new Expense(amount, category, description, date);
            manager.saveExpense(expense);

            outputArea.setText("Expense added successfully!");
            clearFields();
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewAllExpenses() {
        try {
            List<Expense> expenses = manager.loadExpenses();
            if (expenses.isEmpty()) {
                outputArea.setText("No expenses recorded yet.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Expense expense : expenses) {
                    sb.append(expense).append("\n");
                }
                outputArea.setText(sb.toString());
            }
        } catch (IOException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewSummary(String period) {
        try {
            List<Expense> expenses = manager.loadExpenses();
            double total = manager.getTotalForPeriod(expenses, period);
            outputArea.setText("Total " + period + " expenses: " + total);
        } catch (IOException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        amountField.setText("");
        categoryField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DailyExpenseTracker().setVisible(true));
    }
}
