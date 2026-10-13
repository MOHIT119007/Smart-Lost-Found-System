package ui;

import database.DatabaseConnection;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Formal & Professional College Login Portal for Smart Lost & Found System.
 * Supports Student and Administrator authentication and quick registration.
 */
public class LoginFrame extends JFrame {

    private final UserDAO userDAO = new UserDAO();

    private JTextField emailField;
    private JPasswordField pwdField;
    private JComboBox<String> roleBox;
    private JButton loginBtn;
    private JButton registerBtn;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Smart Lost & Found System – Campus Portal Login");
        setIconImage(UITheme.getAppLogo());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                DatabaseConnection.closeConnection();
                dispose();
                System.exit(0);
            }
        });
        setSize(480, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_APP);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UITheme.BG_HEADER);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        brandRow.setBackground(UITheme.BG_HEADER);
        JLabel logoLabel = new JLabel(UITheme.getAppLogoIcon(36, 36));
        JLabel titleLabel = new JLabel("Smart Lost & Found System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        brandRow.add(logoLabel);
        brandRow.add(titleLabel);
        headerPanel.add(brandRow);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(UITheme.BG_APP);
        centerWrapper.setBorder(new EmptyBorder(12, 20, 18, 20));

        JPanel card = new JPanel();
        card.setBackground(UITheme.BG_PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1),
                new EmptyBorder(22, 24, 22, 24)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(385, 395));

        JLabel loginTitle = new JLabel("Sign In to Your Account");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        loginTitle.setForeground(UITheme.TEXT_MAIN);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginTitle);
        card.add(Box.createVerticalStrut(14));

        card.add(createLabel("Select Role"));
        card.add(Box.createVerticalStrut(4));
        roleBox = new JComboBox<>(new String[]{"STUDENT", "ADMIN"});
        UITheme.styleComboBox(roleBox);
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        roleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(roleBox);
        card.add(Box.createVerticalStrut(12));

        card.add(createLabel("College Email Address"));
        card.add(Box.createVerticalStrut(4));
        emailField = new JTextField();
        UITheme.styleTextField(emailField);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));

        card.add(createLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        pwdField = new JPasswordField();
        UITheme.stylePasswordField(pwdField);
        pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        pwdField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(pwdField);
        card.add(Box.createVerticalStrut(6));

        JCheckBox showPwd = new JCheckBox("Show password");
        showPwd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPwd.setBackground(UITheme.BG_PANEL);
        showPwd.setForeground(UITheme.TEXT_MUTED);
        showPwd.setFocusPainted(false);
        showPwd.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPwd.addActionListener(e -> pwdField.setEchoChar(showPwd.isSelected() ? (char) 0 : '•'));
        card.add(showPwd);
        card.add(Box.createVerticalStrut(16));

        loginBtn = UITheme.createPrimaryButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        registerBtn = UITheme.createSecondaryButton("Register as New Student");
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.addActionListener(e -> openRegisterDialog());
        card.add(registerBtn);

        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);

        getRootPane().setDefaultButton(loginBtn);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_MAIN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd = new String(pwdField.getPassword()).trim();
        String selectedRole = (String) roleBox.getSelectedItem();

        if (email.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both email and password.",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        loginBtn.setEnabled(false);
        loginBtn.setText("Signing In...");

        SwingUtilities.invokeLater(() -> {
            try {
                User user = userDAO.authenticate(email, pwd);

                if (user != null) {
                    if (selectedRole != null && !selectedRole.equalsIgnoreCase(user.getRole())) {
                        JOptionPane.showMessageDialog(this,
                                "Account exists, but role is " + user.getRole() + ".\n"
                                        + "Please select '" + user.getRole() + "' from the role dropdown.",
                                "Role Mismatch", JOptionPane.WARNING_MESSAGE);
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Sign In");
                        return;
                    }

                    new DashboardFrame(user).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid email or password.\nIf you don't have an account, click 'Register as New Student'.",
                            "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Sign In");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Database Error during authentication:\n" + ex.getMessage(),
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                loginBtn.setEnabled(true);
                loginBtn.setText("Sign In");
            }
        });
    }

    private void openRegisterDialog() {
        new RegisterDialog(this).setVisible(true);
    }
}
