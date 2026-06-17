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
    private final Color PRIMARY_DARK = new Color(20, 33, 41);
    private final Color ACCENT_TEAL = new Color(0, 105, 92);
    private final Color ACCENT_HOVER = new Color(0, 77, 64);
    private final Color BG_SOFT = new Color(235, 240, 245);
    private final Color TEXT_MAIN = new Color(10, 15, 20);
    private final Color BORDER_SOLID = new Color(120, 144, 156);
    public Dashboard() {
        setTitle("Personal Expense Tracker (L1F23BSSE0361)");
        setSize(920, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_SOFT);
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel pnlForm = new JPanel(new GridLayout(2, 5, 12, 12));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SOLID, 2),
                new EmptyBorder(18, 18, 18, 18)
        ));
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        txtAmount = createStyledTextField(inputFont);
        txtCategory = createStyledTextField(inputFont);
        txtDesc = createStyledTextField(inputFont);
        txtDate = createStyledTextField(inputFont);
        txtDate.setText("2026-06-16");
        cmbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        cmbType.setFont(inputFont);
        cmbType.setForeground(TEXT_MAIN);
        cmbType.setBackground(Color.WHITE);
        cmbType.setBorder(BorderFactory.createLineBorder(BORDER_SOLID, 1));
        JLabel lbl1 = new JLabel("Amount (Rs.)"); lbl1.setFont(labelFont); lbl1.setForeground(PRIMARY_DARK);
        JLabel lbl2 = new JLabel("Transaction Type"); lbl2.setFont(labelFont); lbl2.setForeground(PRIMARY_DARK);
        JLabel lbl3 = new JLabel("Category"); lbl3.setFont(labelFont); lbl3.setForeground(PRIMARY_DARK);
        JLabel lbl4 = new JLabel("Description"); lbl4.setFont(labelFont); lbl4.setForeground(PRIMARY_DARK);
        JLabel lbl5 = new JLabel("Date"); lbl5.setFont(labelFont); lbl5.setForeground(PRIMARY_DARK);
        pnlForm.add(lbl1); pnlForm.add(lbl2); pnlForm.add(lbl3); pnlForm.add(lbl4); pnlForm.add(lbl5);
        pnlForm.add(txtAmount); pnlForm.add(cmbType); pnlForm.add(txtCategory); pnlForm.add(txtDesc); pnlForm.add(txtDate);
        tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Category", "Description", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setForeground(TEXT_MAIN);
        table.setRowHeight(38);
        table.setGridColor(BORDER_SOLID);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.setSelectionForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 42));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_SOLID, 2));
        JPanel pnlBottom = new JPanel(new BorderLayout(15, 0));
        pnlBottom.setOpaque(false);
        lblBalance = new JLabel("Net Cash Holdings: Rs. 0.00");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBalance.setForeground(ACCENT_TEAL);
        lblBalance.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton btnAdd = new JButton("SAVE TRANSACTION");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnAdd.setBackground(ACCENT_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnAdd.setBackground(ACCENT_TEAL); }
        });
        pnlBottom.add(lblBalance, BorderLayout.WEST);
        pnlBottom.add(btnAdd, BorderLayout.EAST);
        add(pnlForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> processEntryLogging());
        refreshTransactionHistoryGrid();
    }
    private JTextField createStyledTextField(Font font) {
        JTextField tf = new JTextField();
        tf.setFont(font);
        tf.setForeground(TEXT_MAIN);
        tf.setCaretColor(TEXT_MAIN);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SOLID, 1),
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
            lblBalance.setForeground(new Color(198, 40, 40));
        } else {
            lblBalance.setForeground(new Color(0, 105, 92));
        }
    }
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}