package ui;

import database.FileHandler;
import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class Dashboard extends JFrame {
    private JTextField txtAmount, txtCategory, txtDesc, txtDate, txtSearch;
    private JComboBox<String> cmbType;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblBalance;

    private final Color PRIMARY_DARK = new Color(20, 33, 41);
    private final Color ACCENT_TEAL = new Color(0, 105, 92);
    private final Color BORDER_SOLID = new Color(120, 144, 156);

    public Dashboard() {
        setTitle("Personal Expense Tracker (L1F23BSSE0361)");
        setSize(950, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(235, 240, 245));
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlSearch = new JPanel(new BorderLayout(10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtSearch = new JTextField();
        JButton btnSearch = new JButton("Filter Records");
        btnSearch.setBackground(PRIMARY_DARK);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlSearch.add(new JLabel("Search Category / Description: "), BorderLayout.WEST);
        pnlSearch.add(txtSearch, BorderLayout.CENTER);
        pnlSearch.add(btnSearch, BorderLayout.EAST);
        btnSearch.addActionListener(e -> refreshTransactionHistoryGrid(txtSearch.getText().trim()));

        JPanel pnlForm = new JPanel(new GridLayout(2, 5, 12, 12));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SOLID, 2), new EmptyBorder(15, 15, 15, 15)
        ));

        txtAmount = new JTextField(); txtCategory = new JTextField(); txtDesc = new JTextField(); txtDate = new JTextField("2026-06-17");
        cmbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});

        pnlForm.add(new JLabel("Amount (Rs.)")); pnlForm.add(new JLabel("Type")); pnlForm.add(new JLabel("Category")); pnlForm.add(new JLabel("Description")); pnlForm.add(new JLabel("Date"));
        pnlForm.add(txtAmount); pnlForm.add(cmbType); pnlForm.add(txtCategory); pnlForm.add(txtDesc); pnlForm.add(txtDate);

        JPanel pnlNorthCombine = new JPanel(new BorderLayout(12, 12));
        pnlNorthCombine.setOpaque(false);
        pnlNorthCombine.add(pnlSearch, BorderLayout.NORTH);
        pnlNorthCombine.add(pnlForm, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Category", "Description", "Date"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setRowHeight(35);
        table.setGridColor(BORDER_SOLID);

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_SOLID, 2));

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);
        lblBalance = new JLabel("Net Cash Holdings: Rs. 0.00");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnAdd = new JButton("SAVE TRANSACTION");
        btnAdd.setBackground(ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pnlBottom.add(lblBalance, BorderLayout.WEST);
        pnlBottom.add(btnAdd, BorderLayout.EAST);

        add(pnlNorthCombine, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> processEntryLogging());
        refreshTransactionHistoryGrid("");
    }

    private void processEntryLogging() {
        try {
            if (txtAmount.getText().isEmpty() || txtCategory.getText().isEmpty() || txtDesc.getText().isEmpty()) {
                throw new IllegalArgumentException("Fields cannot be left blank!");
            }
            double amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) throw new NumberFormatException();

            Transaction t = cmbType.getSelectedItem().toString().equals("EXPENSE") ?
                    new Expense(amount, txtCategory.getText().trim(), txtDesc.getText().trim(), txtDate.getText().trim()) :
                    new Income(amount, txtCategory.getText().trim(), txtDesc.getText().trim(), txtDate.getText().trim());

            FileHandler.saveTransactionToFile(t);
            JOptionPane.showMessageDialog(this, "Logged and saved to text file successfully!");

            txtAmount.setText(""); txtCategory.setText(""); txtDesc.setText("");
            refreshTransactionHistoryGrid("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input Validation Failed! Check values.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTransactionHistoryGrid(String keyword) {
        tableModel.setRowCount(0);
        List<Transaction> history = FileHandler.loadTransactionsFromFile(keyword);
        double balance = 0;
        for (Transaction item : history) {
            tableModel.addRow(new Object[]{item.getTransactionType(), "Rs. " + item.getAmount(), item.getCategory(), item.getDescription(), item.getDate()});
            balance += item.getTransactionType().equals("EXPENSE") ? -item.getAmount() : item.getAmount();
        }
        lblBalance.setText("Net Cash Holdings: Rs. " + balance);
        lblBalance.setForeground(balance < 0 ? new Color(198, 40, 40) : ACCENT_TEAL);
    }
}