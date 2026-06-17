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
    private JTextField txtAmount, txtCategory, txtDesc, txtDate;
    private JComboBox<String> cmbType;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblBalance;

    // Custom Color Palette (Modern Dark Teal Theme)
    private final Color PRIMARY_COLOR = new Color(24, 44, 57);     // Dark Slate
    private final Color ACCENT_COLOR = new Color(38, 166, 154);    // Fresh Teal
    private final Color BACKGROUND_COLOR = new Color(244, 246, 248); // Off-White
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color CARD_BG = Color.WHITE;

    public Dashboard() {
        // Window Window Setup
        setTitle("Personal Expense Tracker (L1F23BSSE0361)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));

        // Global Padding for the whole Window
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TOP PANEL: Beautiful Glass-Style Input Card ---
        JPanel pnlForm = new JPanel(new GridLayout(2, 5, 12, 12));
        pnlForm.setBackground(CARD_BG);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Styling Inputs Styles
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        txtAmount = createStyledTextField("e.g. 5000", inputFont);
        txtCategory = createStyledTextField("e.g. Food, Salary", inputFont);
        txtDesc = createStyledTextField("Short description", inputFont);
        txtDate = createStyledTextField("2026-06-16", inputFont);

        cmbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        cmbType.setFont(inputFont);
        cmbType.setBackground(Color.WHITE);

        // Labels Style Properties
        JLabel lbl1 = new JLabel("Amount (Rs.)"); lbl1.setFont(labelFont); lbl1.setForeground(PRIMARY_COLOR);
        JLabel lbl2 = new JLabel("Transaction Type"); lbl2.setFont(labelFont); lbl2.setForeground(PRIMARY_COLOR);
        JLabel lbl3 = new JLabel("Category"); lbl3.setFont(labelFont); lbl3.setForeground(PRIMARY_COLOR);
        JLabel lbl4 = new JLabel("Description"); lbl4.setFont(labelFont); lbl4.setForeground(PRIMARY_COLOR);
        JLabel lbl5 = new JLabel("Date"); lbl5.setFont(labelFont); lbl5.setForeground(PRIMARY_COLOR);

        pnlForm.add(lbl1); pnlForm.add(lbl2); pnlForm.add(lbl3); pnlForm.add(lbl4); pnlForm.add(lbl5);
        pnlForm.add(txtAmount); pnlForm.add(cmbType); pnlForm.add(txtCategory); pnlForm.add(txtDesc); pnlForm.add(txtDate);

        // --- CENTER PANEL: Clean Modern Minimalist Grid ---
        tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Category", "Description", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Read-only grid
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(224, 242, 241));
        table.setSelectionForeground(TEXT_DARK);

        // Table Header Styling Configuration
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 235, 240)));

        // --- BOTTOM PANEL: Status Summary Deck Cards ---
        JPanel pnlBottom = new JPanel(new BorderLayout(15, 0));
        pnlBottom.setOpaque(false);

        // Net Balance Visual Label
        lblBalance = new JLabel("Net Cash Holdings: Rs. 0.00");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBalance.setForeground(PRIMARY_COLOR);
        lblBalance.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Styled Action Button
        JButton btnAdd = new JButton("Save Transaction");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(ACCENT_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect Engine for Button
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnAdd.setBackground(ACCENT_COLOR.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnAdd.setBackground(ACCENT_COLOR); }
        });

        pnlBottom.add(lblBalance, BorderLayout.WEST);
        pnlBottom.add(btnAdd, BorderLayout.EAST);

        // Inject layouts structure setup
        add(pnlForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        // Action Listener Binding
        btnAdd.addActionListener(e -> processEntryLogging());

        // Refresh view content lists mapping setup
        refreshTransactionHistoryGrid();
    }

    // Helper to generate beautifully styled flat fields
    private JTextField createStyledTextField(String placeholder, Font font) {
        JTextField tf = new JTextField();
        tf.setFont(font);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 219, 233), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
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

            JOptionPane.showMessageDialog(this, "Transaction logged successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            txtAmount.setText("");
            txtCategory.setText("");
            txtDesc.setText("");

            refreshTransactionHistoryGrid();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Please enter a valid positive numeric amount.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Missing", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshTransactionHistoryGrid() {
        tableModel.setRowCount(0);
        List<Transaction> loadedHistoryList = FileHandler.loadTransactionsFromFile();
        double balance = 0.00;

        for (Transaction item : loadedHistoryList) {
            tableModel.addRow(new Object[]{
                    item.getTransactionType(),
                    "Rs. " + item.getAmount(),
                    item.getCategory(),
                    item.getDescription(),
                    item.getDate()
            });

            if (item.getTransactionType().equals("EXPENSE")) {
                balance -= item.getAmount();
            } else {
                balance += item.getAmount();
            }
        }

        lblBalance.setText("Net Cash Holdings: Rs. " + balance);
        if (balance < 0) {
            lblBalance.setForeground(new Color(211, 47, 47)); // Red alert color if negative balance
        } else {
            lblBalance.setForeground(new Color(38, 166, 154)); // Soft corporate green if positive
        }
    }

    public static void main(String[] args) {
        // Apply System Look and Feel if supported safely
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}