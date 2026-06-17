package ui;

import database.FileHandler;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginFrame() {
        setTitle("Secure Login - Expense Tracker");
        setSize(420, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(235, 240, 245));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        add(mainPanel);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color primaryDark = new Color(20, 33, 41);
        Color borderSolid = new Color(120, 144, 156);

        JLabel lblUser = new JLabel("Username (use 'admin'):");
        lblUser.setFont(labelFont);
        lblUser.setForeground(primaryDark);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUser = new JTextField();
        txtUser.setFont(inputFont);
        txtUser.setMaximumSize(new Dimension(350, 35));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSolid, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JLabel lblPass = new JLabel("Password (use 'password123'):");
        lblPass.setFont(labelFont);
        lblPass.setForeground(primaryDark);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPass = new JPasswordField();
        txtPass.setFont(inputFont);
        txtPass.setMaximumSize(new Dimension(350, 35));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSolid, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(primaryDark);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(150, 40)); // Button size compact aur clean
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // BUTTON CENTER ALIGNMENT
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mainPanel.add(lblUser);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txtUser);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(lblPass);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txtPass);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (FileHandler.validateLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome to Dashboard.");
                new Dashboard().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials! Please try again.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}