package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Clean & Simple Student Registration Dialog.
 * Allows students to register with Name, Roll Number, Phone, Email, and Password.
 */
public class RegisterDialog extends JDialog {

    private final UserDAO userDAO = new UserDAO();

    private JTextField nameField;
    private JTextField collegeIdField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterDialog(JFrame parent) {
        super(parent, "New Student Registration", true);
        initComponents();
    }

    private void initComponents() {
        setSize(540, 650);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_APP);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setBackground(UITheme.BG_HEADER);
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("Student Registration");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);

        JLabel subLabel = new JLabel("Create an account to report and recover campus belongings");
        subLabel.setFont(UITheme.FONT_SMALL);
        subLabel.setForeground(new Color(203, 213, 225));

        headerPanel.add(titleLabel);
        headerPanel.add(subLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Card
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(UITheme.BG_PANEL);
        formCard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1),
                new EmptyBorder(16, 20, 16, 20)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(4, 4, 4, 4);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1.0;

        // Full Name
        formCard.add(createLabel("Full Name *"), gc);
        gc.gridy++;
        nameField = new JTextField();
        UITheme.styleTextField(nameField);
        formCard.add(nameField, gc);

        // College Roll Number
        gc.gridy++;
        formCard.add(createLabel("College Roll Number / ID *"), gc);
        gc.gridy++;
        collegeIdField = new JTextField();
        UITheme.styleTextField(collegeIdField);
        formCard.add(collegeIdField, gc);

        // Mobile Number with +91 fixed label
        gc.gridy++;
        formCard.add(createLabel("Mobile Number (10 Digits) *"), gc);
        gc.gridy++;
        JPanel phonePanel = new JPanel(new BorderLayout(6, 0));
        phonePanel.setBackground(UITheme.BG_PANEL);
        JLabel codeLabel = new JLabel("+91 ");
        codeLabel.setFont(UITheme.FONT_BOLD);
        codeLabel.setForeground(UITheme.TEXT_MUTED);
        codeLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
        phonePanel.add(codeLabel, BorderLayout.WEST);

        phoneField = new JTextField();
        UITheme.styleTextField(phoneField);
        phonePanel.add(phoneField, BorderLayout.CENTER);
        formCard.add(phonePanel, gc);

        // Email
        gc.gridy++;
        formCard.add(createLabel("College Email Address *"), gc);
        gc.gridy++;
        emailField = new JTextField();
        UITheme.styleTextField(emailField);
        formCard.add(emailField, gc);

        // Password
        gc.gridy++;
        formCard.add(createLabel("Account Password *"), gc);
        gc.gridy++;
        passwordField = new JPasswordField();
        UITheme.stylePasswordField(passwordField);
        formCard.add(passwordField, gc);

        // Confirm Password
        gc.gridy++;
        formCard.add(createLabel("Confirm Password *"), gc);
        gc.gridy++;
        confirmPasswordField = new JPasswordField();
        UITheme.stylePasswordField(confirmPasswordField);
        formCard.add(confirmPasswordField, gc);

        // Buttons
        gc.gridy++;
        gc.insets = new Insets(14, 4, 4, 4);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(UITheme.BG_PANEL);

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton registerBtn = UITheme.createPrimaryButton("Register Account");
        registerBtn.addActionListener(e -> handleRegister());

        btnPanel.add(cancelBtn);
        btnPanel.add(registerBtn);
        formCard.add(btnPanel, gc);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UITheme.BG_APP);
        contentWrapper.setBorder(new EmptyBorder(12, 16, 14, 16));
        contentWrapper.add(formCard, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_MAIN);
        return lbl;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String roll = collegeIdField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pwd = new String(passwordField.getPassword()).trim();
        String confirmPwd = new String(confirmPasswordField.getPassword()).trim();

        if (name.isEmpty() || roll.isEmpty() || phone.isEmpty() || email.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (*).",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Strip +91 if user happened to type it
        if (phone.startsWith("+91")) {
            phone = phone.substring(3).trim();
        }

        // Mobile Number validation: EXACTLY 10 DIGITS (Numbers only)
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Mobile number must contain exactly 10 digits.",
                    "Invalid Mobile Number", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this,
                    "Password and Confirm Password do not match.",
                    "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.requestFocus();
            return;
        }

        User user = new User(name, email, pwd, "STUDENT", phone, roll);
        boolean success = userDAO.register(user);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Student account created successfully!\nYou can now sign in with your email and password.",
                    "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Email may already be registered.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
