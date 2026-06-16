package ui;

import database.FileHandler;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class Dashboard extends JFrame {
    private JTextField txtAmount, txtCategory, txtDesc, txtDate;
    private JComboBox<String> cmbType;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblBalance;
    public Dashboard() {
        setTitle("Personal Expense Tracker (L1F23BSSE0361)");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        JPanel pnlForm = new JPanel(new GridLayout(2, 5, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Log New Financial Transaction"));
        txtAmount = new JTextField();
        txtCategory = new JTextField();
        txtDesc = new JTextField();
        txtDate = new JTextField("2026-06-16");
        cmbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        JButton btnAdd = new JButton("Save Transaction");
        pnlForm.add(new JLabel("Amount:"));
        pnlForm.add(new JLabel("Type:"));
        pnlForm.add(new JLabel("Category:"));
        pnlForm.add(new JLabel("Description:"));
        pnlForm.add(new JLabel("Date:"));
        pnlForm.add(txtAmount);
        pnlForm.add(cmbType);
        pnlForm.add(txtCategory);
        pnlForm.add(txtDesc);
        pnlForm.add(txtDate);
        tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Category", "Description", "Date"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel pnlBottom = new JPanel(new BorderLayout());
        lblBalance = new JLabel("Net Cash Holdings: Rs. 0.00");
        lblBalance.setFont(new Font("Arial", Font.BOLD, 16));
        lblBalance.setForeground(new Color(0, 128, 64));
        pnlBottom.add(lblBalance, BorderLayout.WEST);
        pnlBottom.add(btnAdd, BorderLayout.EAST);
        add(pnlForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> processEntryLogging());
        refreshTransactionHistoryGrid();
    }
    private void processEntryLogging() {
        try {
            String rawAmount = txtAmount.getText().trim();
            String category = txtCategory.getText().trim();
            String description = txtDesc.getText().trim();
            String date = txtDate.getText().trim();
            if (rawAmount.isEmpty() || category.isEmpty() || description.isEmpty() || date.isEmpty()) {
                throw new IllegalArgumentException("All fields must be completely filled out!");
            }
            double amount = Double.parseDouble(rawAmount);
            if (amount <= 0) {
                throw new NumberFormatException("Transaction amount must be strictly greater than zero.");
            }
            Transaction transaction;
            if (cmbType.getSelectedItem().toString().equals("EXPENSE")) {
                transaction = new Expense(amount, category, description, date);
            } else {
                transaction = new Income(amount, category, description, date);
            }
            FileHandler.saveTransactionToFile(transaction);
            JOptionPane.showMessageDialog(this, "Transaction logged and saved to file successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtAmount.setText("");
            txtCategory.setText("");
            txtDesc.setText("");
            refreshTransactionHistoryGrid();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Please enter a valid positive numeric amount.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: " + ex.getMessage(), "Input Missing", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void refreshTransactionHistoryGrid() {
        tableModel.setRowCount(0);
        List<Transaction> loadedHistoryList = FileHandler.loadTransactionsFromFile();
        double dynamicNetHoldingCalculatedBalance = 0.00;
        for (Transaction item : loadedHistoryList) {
            tableModel.addRow(new Object[]{
                    item.getTransactionType(),
                    "Rs. " + item.getAmount(),
                    item.getCategory(),
                    item.getDescription(),
                    item.getDate()
            });
            if (item.getTransactionType().equals("EXPENSE")) {
                dynamicNetHoldingCalculatedBalance -= item.getAmount();
            } else {
                dynamicNetHoldingCalculatedBalance += item.getAmount();
            }
        }
        lblBalance.setText("Net Cash Holdings: Rs. " + dynamicNetHoldingCalculatedBalance);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}