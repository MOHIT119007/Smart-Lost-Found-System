package ui;

import dao.FoundItemDAO;
import dao.LostItemDAO;
import dao.UserDAO;
import model.FoundItem;
import model.LostItem;
import model.User;
import service.MatchingService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Smart Lost & Found System – Main Application Dashboard.
 *
 * Designed for simplicity, clean architecture, and easy viva explanation.
 *
 * Student Modules:
 *   1. Report Lost Item (Status: PENDING)
 *   2. Report Found Item (Status: PENDING)
 *   3. Search Items (Simple name & category match)
 *
 * Admin Modules:
 *   1. Verify / Approve Items (Quick pending verification)
 *   2. Lost Items (Full table + Details + Approve/Reject)
 *   3. Found Items (Full table + Details + Approve/Reject)
 *   4. Registered Users (Directory of students & admins)
 */
public class DashboardFrame extends JFrame {

    private final User currentUser;
    private final boolean isAdmin;

    // DAOs
    private final UserDAO userDAO = new UserDAO();
    private final LostItemDAO lostItemDAO = new LostItemDAO();
    private final FoundItemDAO foundItemDAO = new FoundItemDAO();
    private final MatchingService matchingService = new MatchingService();

    // Layout
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);
    private final List<JButton> navButtons = new ArrayList<>();

    // ── Admin Components ────────────────────────────────────────────────────
    // 1. Registered Users Section
    private JTable adminUsersTable;
    private DefaultTableModel adminUsersModel;
    private JTextArea adminUsersDetailArea;

    // 2. Register Found Items Section (Approval, List & Desk Entry)
    private JTable adminFoundTable;
    private DefaultTableModel adminFoundModel;
    private JTextArea adminFoundDetailArea;
    private JButton adminFoundApproveBtn;
    private JButton adminFoundRejectBtn;
    private FoundItem selectedFoundItem = null;

    // 3. Lost Items Approval Section
    private JTable adminLostTable;
    private DefaultTableModel adminLostModel;
    private JTextArea adminLostDetailArea;
    private JButton adminLostApproveBtn;
    private JButton adminLostRejectBtn;
    private JComboBox<String> adminLostFilterBox;
    private LostItem selectedLostItem = null;

    // 4. Claimed Items Details Section
    private JTable adminClaimedTable;
    private DefaultTableModel adminClaimedModel;
    private JTextArea adminClaimedDetailArea;
    private JComboBox<String> adminClaimedFilterBox;
    private final List<Object> adminClaimedRecordList = new ArrayList<>();

    // ── Student Components ──────────────────────────────────────────────────
    // 3. Notifications & Approvals Section
    private JTable studentNotificationsTable;
    private DefaultTableModel studentNotificationsModel;
    private JTextArea studentNotificationsDetailArea;
    private JComboBox<String> studentNotificationsFilterBox;
    private JButton studentAcceptClaimBtn;
    private JButton studentRejectClaimBtn;
    private JLabel studentActionHintLabel;
    private final List<Object> studentNotificationsRecordList = new ArrayList<>();

    // 4. My Claimed Items Section
    private JTable studentClaimedTable;
    private DefaultTableModel studentClaimedModel;
    private JTextArea studentClaimedDetailArea;
    private JComboBox<String> studentClaimedFilterBox;
    private final List<Object> studentClaimedRecordList = new ArrayList<>();

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());
        initComponents();
    }

    private void initComponents() {
        setTitle(isAdmin
                ? "Smart Lost & Found System – Campus Administration Portal"
                : "Smart Lost & Found System – Student Portal (" + currentUser.getFullName() + ")");
        setIconImage(UITheme.getAppLogo());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 750);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_APP);
        setLayout(new BorderLayout());

        // Left Navigation Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Center Content Cards
        contentCards.setBackground(UITheme.BG_APP);
        if (isAdmin) {
            contentCards.add(createAdminUsersCard(), "ADMIN_USERS");
            contentCards.add(createAdminFoundCard(), "ADMIN_FOUND");
            contentCards.add(createAdminLostCard(), "ADMIN_LOST");
            contentCards.add(createAdminClaimedCard(), "ADMIN_CLAIMED");
        } else {
            contentCards.add(createStudentReportLostCard(), "STUDENT_LOST");
            contentCards.add(createStudentReportFoundCard(), "STUDENT_FOUND");
            contentCards.add(createStudentNotificationsCard(), "STUDENT_NOTIFICATIONS");
            contentCards.add(createStudentClaimedCard(), "STUDENT_CLAIMED");
        }
        add(contentCards, BorderLayout.CENTER);

        // Default Tab Activation
        if (!navButtons.isEmpty()) {
            if (isAdmin) {
                setActiveTab("ADMIN_USERS", navButtons.get(0));
                refreshAdminUsers();
            } else {
                setActiveTab("STUDENT_LOST", navButtons.get(0));
                checkStudentNotifications();
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LEFT SIDEBAR NAVIGATION (Matches existing professional dark styling)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UITheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(285, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, UITheme.BORDER));

        // Top: Logo & User Badge
        JPanel profileBox = new JPanel();
        profileBox.setLayout(new BoxLayout(profileBox, BoxLayout.Y_AXIS));
        profileBox.setBackground(UITheme.BG_SIDEBAR);
        profileBox.setBorder(new EmptyBorder(18, 10, 14, 10));

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        brandRow.setBackground(UITheme.BG_SIDEBAR);
        brandRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel logo = new JLabel(UITheme.getAppLogoIcon(32, 32));
        JLabel appTitle = new JLabel("Smart Lost & Found");
        appTitle.setFont(UITheme.FONT_HEADER);
        appTitle.setForeground(UITheme.TEXT_WHITE);
        brandRow.add(logo);
        brandRow.add(appTitle);
        profileBox.add(brandRow);

        profileBox.add(Box.createVerticalStrut(14));

        // User Details Badge
        JPanel userBadge = new JPanel(new GridLayout(3, 1, 0, 3));
        userBadge.setBackground(UITheme.BG_PANEL);
        userBadge.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(10, 8, 10, 8)));
        userBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        userBadge.setMaximumSize(new Dimension(265, 88));

        JLabel nameLabel = new JLabel(currentUser.getFullName(), SwingConstants.CENTER);
        nameLabel.setFont(UITheme.FONT_BOLD);
        nameLabel.setForeground(UITheme.TEXT_WHITE);

        String idText = isAdmin ? currentUser.getEmail() : "Roll: " + currentUser.getCollegeId();
        JLabel idLabel = new JLabel(idText, SwingConstants.CENTER);
        idLabel.setFont(UITheme.FONT_SMALL);
        idLabel.setForeground(UITheme.TEXT_MUTED);

        JLabel roleLabel = new JLabel(isAdmin ? "• Campus Administrator •" : "• Student Member •", SwingConstants.CENTER);
        roleLabel.setFont(UITheme.FONT_SMALL);
        roleLabel.setForeground(isAdmin ? UITheme.WARNING : UITheme.SUCCESS);

        userBadge.add(nameLabel);
        userBadge.add(idLabel);
        userBadge.add(roleLabel);
        profileBox.add(userBadge);

        sidebar.add(profileBox, BorderLayout.NORTH);

        // Center: Navigation Menu Buttons
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(UITheme.BG_SIDEBAR);
        menuPanel.setBorder(new EmptyBorder(6, 14, 14, 14));

        navButtons.clear();
        if (isAdmin) {
            // ADMIN: 1. Registered Users, 2. Register Found Items, 3. Lost Items Approval, 4. Claimed Items Details
            JButton b1 = createSidebarNavButton("Registered Users Details", "ADMIN_USERS", () -> refreshAdminUsers());
            JButton b2 = createSidebarNavButton("Register Found Items", "ADMIN_FOUND", () -> refreshAdminFound());
            JButton b3 = createSidebarNavButton("Lost Items Approval", "ADMIN_LOST", () -> refreshAdminLost());
            JButton b4 = createSidebarNavButton("Claimed Items Details", "ADMIN_CLAIMED", () -> refreshAdminClaimed());

            menuPanel.add(b1);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b2);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b3);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b4);
        } else {
            // STUDENT: 1. Report Lost, 2. Report Found, 3. Notifications & Approvals, 4. My Claimed Items
            JButton b1 = createSidebarNavButton("Report Lost Item", "STUDENT_LOST", null);
            JButton b2 = createSidebarNavButton("Report Found Item", "STUDENT_FOUND", null);
            JButton b3 = createSidebarNavButton("Notifications & Approvals", "STUDENT_NOTIFICATIONS", () -> refreshStudentNotifications());
            JButton b4 = createSidebarNavButton("My Claimed Items", "STUDENT_CLAIMED", () -> refreshStudentClaimed());

            menuPanel.add(b1);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b2);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b3);
            menuPanel.add(Box.createVerticalStrut(8));
            menuPanel.add(b4);
        }

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Bottom: Logout Button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UITheme.BG_SIDEBAR);
        bottomPanel.setBorder(new EmptyBorder(12, 14, 16, 14));

        JButton logoutBtn = UITheme.createSecondaryButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());
        bottomPanel.add(logoutBtn, BorderLayout.CENTER);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        return sidebar;
    }

    private JButton createSidebarNavButton(String text, String cardKey, Runnable onSelect) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_LABEL);
        btn.setBackground(UITheme.BG_PANEL);
        btn.setForeground(UITheme.TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addActionListener(e -> {
            setActiveTab(cardKey, btn);
            if (onSelect != null) {
                onSelect.run();
            }
        });

        navButtons.add(btn);
        return btn;
    }

    private void setActiveTab(String cardKey, JButton activeBtn) {
        cardLayout.show(contentCards, cardKey);
        contentCards.revalidate();
        contentCards.repaint();
        for (JButton b : navButtons) {
            b.setBackground(UITheme.BG_PANEL);
            b.setForeground(UITheme.TEXT_WHITE);
        }
        activeBtn.setBackground(UITheme.PRIMARY);
        activeBtn.setForeground(UITheme.TEXT_WHITE);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN SECTION 1: VERIFY / APPROVE ITEMS (Quick Pending Verification)
    // ════════════════════════════════════════════════════════════════════════
    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN SECTION 4: CLAIMED ITEMS DETAILS (Only Items Claimed by Users)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createAdminClaimedCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("Claimed Items Details",
                "Official records of items claimed by students from Campus Administration."), BorderLayout.CENTER);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topControls.setBackground(UITheme.BG_APP);

        adminClaimedFilterBox = new JComboBox<>(new String[]{"All Claimed Items", "Claimed Lost Items", "Claimed Found Items"});
        UITheme.styleComboBox(adminClaimedFilterBox);
        adminClaimedFilterBox.setPreferredSize(new Dimension(190, 36));
        adminClaimedFilterBox.addActionListener(e -> refreshAdminClaimed());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshAdminClaimed());

        topControls.add(adminClaimedFilterBox);
        topControls.add(refreshBtn);
        topBar.add(topControls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        String[] cols = {"ID", "Item Name", "Category", "Report Type", "Location", "Claimed By", "Roll / ID", "Mobile", "Date", "Status"};
        adminClaimedModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminClaimedTable = new JTable(adminClaimedModel);
        UITheme.styleTable(adminClaimedTable);
        adminClaimedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminClaimedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) handleAdminClaimedRowSelection();
        });

        JScrollPane tableScroll = new JScrollPane(adminClaimedTable);
        tableScroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        tableScroll.getViewport().setBackground(UITheme.BG_INPUT);
        tableScroll.setPreferredSize(new Dimension(0, 270));
        center.add(tableScroll, BorderLayout.CENTER);

        // Details below
        JPanel bottomBox = new JPanel(new BorderLayout(0, 10));
        bottomBox.setBackground(UITheme.BG_APP);

        JPanel detailCard = UITheme.createCardPanel();
        detailCard.setLayout(new BorderLayout(0, 8));
        detailCard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel detLabel = new JLabel("Claimed Item & Student Verification Details:");
        detLabel.setFont(UITheme.FONT_LABEL);
        detLabel.setForeground(UITheme.TEXT_WHITE);
        detailCard.add(detLabel, BorderLayout.NORTH);

        adminClaimedDetailArea = new JTextArea("Select a Claimed Item above to view complete handover and student details.");
        UITheme.styleTextArea(adminClaimedDetailArea);
        adminClaimedDetailArea.setEditable(false);
        adminClaimedDetailArea.setRows(5);
        JScrollPane detScroll = new JScrollPane(adminClaimedDetailArea);
        detScroll.setBorder(null);
        detScroll.getViewport().setBackground(UITheme.BG_INPUT);
        detailCard.add(detScroll, BorderLayout.CENTER);

        bottomBox.add(detailCard, BorderLayout.CENTER);
        center.add(bottomBox, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        refreshAdminClaimed();
        return panel;
    }

    private void refreshAdminClaimed() {
        if (adminClaimedModel == null) return;
        adminClaimedModel.setRowCount(0);
        adminClaimedRecordList.clear();
        if (adminClaimedDetailArea != null) {
            adminClaimedDetailArea.setText("Select a Claimed Item above to view complete handover and student details.");
        }

        int filterIdx = adminClaimedFilterBox != null ? adminClaimedFilterBox.getSelectedIndex() : 0;

        if (filterIdx == 0 || filterIdx == 1) {
            List<LostItem> lost = lostItemDAO.getAllLostItems();
            for (LostItem l : lost) {
                if ("CLAIMED".equalsIgnoreCase(l.getStatus()) || "CONFIRMED".equalsIgnoreCase(l.getStatus())) {
                    User u = userDAO.getUserById(l.getUserId());
                    String roll = (u != null && u.getCollegeId() != null) ? u.getCollegeId() : "-";
                    String name = (u != null) ? u.getFullName() : l.getReporterName();
                    adminClaimedModel.addRow(new Object[]{
                            l.getId(), l.getItemName(), l.getCategory(), "LOST",
                            l.getLocation(), name, roll, l.getContactPhone(), l.getLostDate(), "CLAIMED"
                    });
                    adminClaimedRecordList.add(l);
                }
            }
        }

        if (filterIdx == 0 || filterIdx == 2) {
            List<FoundItem> found = foundItemDAO.getAllFoundItems();
            for (FoundItem f : found) {
                if ("CLAIMED".equalsIgnoreCase(f.getStatus()) || "CONFIRMED".equalsIgnoreCase(f.getStatus())) {
                    User u = userDAO.getUserById(f.getUserId());
                    String roll = (u != null && u.getCollegeId() != null) ? u.getCollegeId() : "-";
                    String name = (u != null) ? u.getFullName() : f.getFinderName();
                    adminClaimedModel.addRow(new Object[]{
                            f.getId(), f.getItemName(), f.getCategory(), "FOUND",
                            f.getFoundLocation(), name, roll, f.getContactPhone(), f.getFoundDate(), "CLAIMED"
                    });
                    adminClaimedRecordList.add(f);
                }
            }
        }

        if (adminClaimedModel.getRowCount() > 0) {
            adminClaimedTable.setRowSelectionInterval(0, 0);
        } else if (adminClaimedDetailArea != null) {
            adminClaimedDetailArea.setText("No claimed items found.\nWhen students approve their item notifications, the claimed items will appear here.");
        }
    }

    private void handleAdminClaimedRowSelection() {
        int row = adminClaimedTable.getSelectedRow();
        if (row < 0 || row >= adminClaimedRecordList.size()) return;

        Object obj = adminClaimedRecordList.get(row);
        if (obj instanceof LostItem) {
            LostItem l = (LostItem) obj;
            User u = userDAO.getUserById(l.getUserId());
            adminClaimedDetailArea.setText(
                    "Item Type: LOST ITEM (Claimed by Student)\n"
                    + "Item Name: " + l.getItemName() + " | Category: " + l.getCategory() + "\n"
                    + "Lost Location: " + l.getLocation() + " | Date Reported: " + l.getLostDate() + "\n"
                    + "Claimed By Student: " + (u != null ? u.getFullName() : l.getReporterName()) + "\n"
                    + "Student Roll / ID: " + (u != null ? u.getCollegeId() : "N/A") + " | Email: " + (u != null ? u.getEmail() : "N/A") + "\n"
                    + "Contact Mobile: +91 " + l.getContactPhone() + "\n"
                    + "Status: CLAIMED (Handed over & Confirmed by Student)\n"
                    + "Description: " + (l.getDescription() != null ? l.getDescription() : "None")
            );
        } else if (obj instanceof FoundItem) {
            FoundItem f = (FoundItem) obj;
            User u = userDAO.getUserById(f.getUserId());
            adminClaimedDetailArea.setText(
                    "Item Type: FOUND ITEM (Claimed by Student)\n"
                    + "Item Name: " + f.getItemName() + " | Category: " + f.getCategory() + "\n"
                    + "Found Location: " + f.getFoundLocation() + " | Desk Storage: " + (f.getStorageLocation() != null ? f.getStorageLocation() : "Campus Desk") + "\n"
                    + "Claimed By: " + (u != null ? u.getFullName() : f.getFinderName()) + "\n"
                    + "College ID: " + (u != null ? u.getCollegeId() : "N/A") + " | Email: " + (u != null ? u.getEmail() : "N/A") + "\n"
                    + "Contact Mobile: +91 " + f.getContactPhone() + "\n"
                    + "Status: CLAIMED (Handed over & Confirmed by Student)\n"
                    + "Description: " + (f.getDescription() != null ? f.getDescription() : "None")
            );
        }
    }

    /**
     * Campus Desk Entry dialog for Admin to directly register found items.
     */
    private void showAdminRegisterFoundDialog() {
        JDialog dialog = new JDialog(this, "Register Found Item – Campus Desk Entry", true);
        dialog.setSize(520, 580);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BG_APP);
        dialog.setLayout(new BorderLayout());

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(UITheme.BG_APP);
        root.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("Campus Desk Found Item Entry");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_WHITE);
        root.add(title);
        root.add(Box.createVerticalStrut(4));

        JLabel subtitle = new JLabel("Directly record an item handed over to the administration desk.");
        subtitle.setFont(UITheme.FONT_SMALL);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        root.add(subtitle);
        root.add(Box.createVerticalStrut(14));

        JTextField nameField = new JTextField();
        UITheme.styleTextField(nameField);

        JComboBox<String> catBox = new JComboBox<>(new String[]{
                "Electronics", "Books & Stationery", "ID & Cards", "Bags & Wallets", "Keys & Locks", "Clothing & Accessories", "Other"
        });
        UITheme.styleComboBox(catBox);

        JTextField locField = new JTextField();
        UITheme.styleTextField(locField);

        JTextField storageField = new JTextField("Campus Administration Desk");
        UITheme.styleTextField(storageField);

        JTextField dateField = new JTextField(LocalDate.now().toString());
        UITheme.styleTextField(dateField);

        JTextField finderField = new JTextField("Campus Desk / Handed In");
        UITheme.styleTextField(finderField);

        JTextField phoneField = new JTextField("9876543210");
        UITheme.styleTextField(phoneField);

        JTextArea descArea = new JTextArea(3, 20);
        UITheme.styleTextArea(descArea);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        root.add(createFormRow("Item Name *", nameField));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Category *", catBox));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Found Location *", locField));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Desk Storage Location", storageField));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Date Found (YYYY-MM-DD)", dateField));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Finder Name", finderField));
        root.add(Box.createVerticalStrut(8));
        root.add(createFormRow("Contact Phone (10 Digits) *", phoneField));
        root.add(Box.createVerticalStrut(8));

        JPanel descRow = new JPanel(new BorderLayout(0, 4));
        descRow.setBackground(UITheme.BG_APP);
        JLabel descLabel = new JLabel("Description / Identifying Features:");
        descLabel.setFont(UITheme.FONT_SMALL);
        descLabel.setForeground(UITheme.TEXT_MUTED);
        descRow.add(descLabel, BorderLayout.NORTH);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        descRow.add(descScroll, BorderLayout.CENTER);
        root.add(descRow);

        dialog.add(root, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnBar.setBackground(UITheme.BG_SIDEBAR);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = UITheme.createPrimaryButton("Register Item");
        saveBtn.addActionListener(e -> {
            String itemName = nameField.getText().trim();
            String location = locField.getText().trim();
            String storage = storageField.getText().trim();
            String dateStr = dateField.getText().trim();
            String finder = finderField.getText().trim();
            String phone = phoneField.getText().trim();
            String desc = descArea.getText().trim();
            String category = (String) catBox.getSelectedItem();

            if (itemName.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in Item Name and Found Location.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateMobile(phone)) {
                JOptionPane.showMessageDialog(dialog, "Contact phone must be exactly 10 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date foundDate;
            try {
                foundDate = Date.valueOf(dateStr);
            } catch (Exception ex) {
                foundDate = Date.valueOf(LocalDate.now());
            }

            FoundItem item = new FoundItem();
            item.setUserId(currentUser.getId());
            item.setItemName(itemName);
            item.setCategory(category);
            item.setFoundLocation(location);
            item.setStorageLocation(storage.isEmpty() ? "Campus Administration Desk" : storage);
            item.setFoundDate(foundDate);
            item.setFinderName(finder.isEmpty() ? "Campus Desk" : finder);
            item.setContactPhone(phone);
            item.setDescription(desc);
            item.setStatus("APPROVED");

            if (foundItemDAO.addFoundItem(item)) {
                JOptionPane.showMessageDialog(dialog, "Found item registered successfully at campus desk!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshAdminFound();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to register found item in database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBar.add(cancelBtn);
        btnBar.add(saveBtn);
        dialog.add(btnBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createFormRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setBackground(UITheme.BG_APP);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_MUTED);
        row.add(lbl, BorderLayout.NORTH);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN SECTION 3: LOST ITEMS APPROVAL (Pending/Approved reports; removed when claimed)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createAdminLostCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("Lost Items Approval",
                "Review lost item reports and approve or reject them. When approved, a student notification is sent. When claimed, items move to Claimed Items."), BorderLayout.CENTER);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topControls.setBackground(UITheme.BG_APP);

        adminLostFilterBox = new JComboBox<>(new String[]{"All Unclaimed Reports", "Pending Admin Approval", "Waiting for Student Claim"});
        UITheme.styleComboBox(adminLostFilterBox);
        adminLostFilterBox.setPreferredSize(new Dimension(200, 36));
        adminLostFilterBox.addActionListener(e -> refreshAdminLost());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshAdminLost());

        topControls.add(adminLostFilterBox);
        topControls.add(refreshBtn);
        topBar.add(topControls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        String[] cols = {"ID", "Item Name", "Category", "Lost Location", "Date", "Student Name", "Mobile", "Status"};
        adminLostModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminLostTable = new JTable(adminLostModel);
        UITheme.styleTable(adminLostTable);
        adminLostTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminLostTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) handleAdminLostRowSelection();
        });

        JScrollPane tableScroll = new JScrollPane(adminLostTable);
        tableScroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        tableScroll.getViewport().setBackground(UITheme.BG_INPUT);
        tableScroll.setPreferredSize(new Dimension(0, 260));
        center.add(tableScroll, BorderLayout.CENTER);

        // Detail panel below
        JPanel bottomBox = new JPanel(new BorderLayout(0, 10));
        bottomBox.setBackground(UITheme.BG_APP);

        JPanel detailCard = UITheme.createCardPanel();
        detailCard.setLayout(new BorderLayout(0, 8));
        detailCard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel detLabel = new JLabel("Lost Item Report Details:");
        detLabel.setFont(UITheme.FONT_LABEL);
        detLabel.setForeground(UITheme.TEXT_WHITE);
        detailCard.add(detLabel, BorderLayout.NORTH);

        adminLostDetailArea = new JTextArea("Select a Lost Item above to view full details.");
        UITheme.styleTextArea(adminLostDetailArea);
        adminLostDetailArea.setEditable(false);
        adminLostDetailArea.setRows(5);
        JScrollPane detScroll = new JScrollPane(adminLostDetailArea);
        detScroll.setBorder(null);
        detScroll.getViewport().setBackground(UITheme.BG_INPUT);
        detailCard.add(detScroll, BorderLayout.CENTER);

        bottomBox.add(detailCard, BorderLayout.CENTER);

        // Action Buttons: [ APPROVE ]   [ REJECT ]
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnBar.setBackground(UITheme.BG_APP);

        adminLostApproveBtn = UITheme.createSuccessButton("APPROVE");
        adminLostApproveBtn.setEnabled(false);
        adminLostApproveBtn.addActionListener(e -> {
            if (selectedLostItem != null) {
                if (lostItemDAO.updateStatus(selectedLostItem.getId(), "APPROVED")) {
                    JOptionPane.showMessageDialog(this,
                            "Lost Item #" + selectedLostItem.getId() + " approved!\nNotification sent to student for confirmation. When student claims it, it will automatically move to Claimed Items Details.",
                            "Approved", JOptionPane.INFORMATION_MESSAGE);
                    refreshAdminLost();
                    refreshAdminClaimed();
                }
            }
        });

        adminLostRejectBtn = UITheme.createDangerButton("REJECT");
        adminLostRejectBtn.setEnabled(false);
        adminLostRejectBtn.addActionListener(e -> {
            if (selectedLostItem != null) {
                if (lostItemDAO.updateStatus(selectedLostItem.getId(), "REJECTED")) {
                    JOptionPane.showMessageDialog(this,
                            "Lost Item #" + selectedLostItem.getId() + " marked as REJECTED.",
                            "Status Updated", JOptionPane.INFORMATION_MESSAGE);
                    refreshAdminLost();
                }
            }
        });

        btnBar.add(adminLostApproveBtn);
        btnBar.add(adminLostRejectBtn);
        bottomBox.add(btnBar, BorderLayout.SOUTH);

        center.add(bottomBox, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);
        refreshAdminLost();
        return panel;
    }

    private void refreshAdminLost() {
        if (adminLostModel == null) return;
        adminLostModel.setRowCount(0);
        selectedLostItem = null;
        if (adminLostDetailArea != null) {
            adminLostDetailArea.setText("Select a Lost Item above to view full details.");
        }
        if (adminLostApproveBtn != null) adminLostApproveBtn.setEnabled(false);
        if (adminLostRejectBtn != null) adminLostRejectBtn.setEnabled(false);

        int filterIdx = adminLostFilterBox != null ? adminLostFilterBox.getSelectedIndex() : 0;

        List<LostItem> list = lostItemDAO.getAllLostItems();
        for (LostItem l : list) {
            // Once claimed by student, it is REMOVED from this section and appears in Claimed Items Details!
            if ("CLAIMED".equalsIgnoreCase(l.getStatus()) || "CONFIRMED".equalsIgnoreCase(l.getStatus())) {
                continue;
            }
            if (filterIdx == 1 && !"PENDING".equalsIgnoreCase(l.getStatus())) continue;
            if (filterIdx == 2 && !"APPROVED".equalsIgnoreCase(l.getStatus())) continue;

            adminLostModel.addRow(new Object[]{
                    l.getId(), l.getItemName(), l.getCategory(), l.getLocation(),
                    l.getLostDate(), l.getReporterName(), l.getContactPhone(), l.getStatus()
            });
        }
        if (adminLostModel.getRowCount() > 0) {
            adminLostTable.setRowSelectionInterval(0, 0);
        }
    }

    private void handleAdminLostRowSelection() {
        int row = adminLostTable.getSelectedRow();
        if (row < 0) {
            selectedLostItem = null;
            if (adminLostApproveBtn != null) adminLostApproveBtn.setEnabled(false);
            if (adminLostRejectBtn != null) adminLostRejectBtn.setEnabled(false);
            return;
        }

        int id = (int) adminLostModel.getValueAt(row, 0);
        selectedLostItem = lostItemDAO.getLostItemById(id);
        if (selectedLostItem != null) {
            String note = "PENDING".equalsIgnoreCase(selectedLostItem.getStatus())
                    ? "⏳ Awaiting Admin Approval. Click [ APPROVE ] to send notification to student."
                    : ("APPROVED".equalsIgnoreCase(selectedLostItem.getStatus())
                            ? "🔔 Approved by Admin! Notification sent to student. Awaiting student acceptance."
                            : "✕ Rejected by Admin.");

            adminLostDetailArea.setText(
                    "Item Name: " + selectedLostItem.getItemName() + " | Category: " + selectedLostItem.getCategory() + "\n"
                    + "Lost Location: " + selectedLostItem.getLocation() + " | Date Reported: " + selectedLostItem.getLostDate() + "\n"
                    + "Student Reporter: " + selectedLostItem.getReporterName() + " | Mobile: " + selectedLostItem.getContactPhone() + "\n"
                    + "Current Status: " + selectedLostItem.getStatus() + "\n"
                    + "Verification Note: " + note + "\n"
                    + "Description: " + (selectedLostItem.getDescription() != null ? selectedLostItem.getDescription() : "N/A")
            );
            adminLostApproveBtn.setEnabled(true);
            adminLostRejectBtn.setEnabled(true);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN SECTION 2: REGISTER FOUND ITEMS & APPROVAL (List + Approval + Desk Entry)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createAdminFoundCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("Register Found Items & Approval",
                "Campus desk entry, found items directory, and verification approval."), BorderLayout.CENTER);

        JButton registerDeskBtn = UITheme.createPrimaryButton("+ Register Found Item (Desk Entry)");
        registerDeskBtn.addActionListener(e -> showAdminRegisterFoundDialog());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshAdminFound());

        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tr.setBackground(UITheme.BG_APP);
        tr.add(registerDeskBtn);
        tr.add(refreshBtn);
        topBar.add(tr, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        String[] cols = {"ID", "Item Name", "Category", "Found Location", "Date", "Finder Name", "Mobile", "Status"};
        adminFoundModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminFoundTable = new JTable(adminFoundModel);
        UITheme.styleTable(adminFoundTable);
        adminFoundTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminFoundTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) handleAdminFoundRowSelection();
        });

        JScrollPane tableScroll = new JScrollPane(adminFoundTable);
        tableScroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        tableScroll.getViewport().setBackground(UITheme.BG_INPUT);
        tableScroll.setPreferredSize(new Dimension(0, 260));
        center.add(tableScroll, BorderLayout.CENTER);

        // Detail panel below
        JPanel bottomBox = new JPanel(new BorderLayout(0, 10));
        bottomBox.setBackground(UITheme.BG_APP);

        JPanel detailCard = UITheme.createCardPanel();
        detailCard.setLayout(new BorderLayout(0, 8));
        detailCard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel detLabel = new JLabel("Found Item Details:");
        detLabel.setFont(UITheme.FONT_LABEL);
        detLabel.setForeground(UITheme.TEXT_WHITE);
        detailCard.add(detLabel, BorderLayout.NORTH);

        adminFoundDetailArea = new JTextArea("Select a Found Item above to view full details.");
        UITheme.styleTextArea(adminFoundDetailArea);
        adminFoundDetailArea.setEditable(false);
        adminFoundDetailArea.setRows(5);
        JScrollPane detScroll = new JScrollPane(adminFoundDetailArea);
        detScroll.setBorder(null);
        detScroll.getViewport().setBackground(UITheme.BG_INPUT);
        detailCard.add(detScroll, BorderLayout.CENTER);

        bottomBox.add(detailCard, BorderLayout.CENTER);

        // Action Buttons: [ APPROVE ]   [ REJECT ]
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnBar.setBackground(UITheme.BG_APP);

        adminFoundApproveBtn = UITheme.createSuccessButton("APPROVE");
        adminFoundApproveBtn.setEnabled(false);
        adminFoundApproveBtn.addActionListener(e -> {
            if (selectedFoundItem != null) {
                if (foundItemDAO.updateStatus(selectedFoundItem.getId(), "APPROVED")) {
                    JOptionPane.showMessageDialog(this,
                            "Found Item #" + selectedFoundItem.getId() + " approved and marked as verified on campus.",
                            "Approved", JOptionPane.INFORMATION_MESSAGE);
                    refreshAdminFound();
                }
            }
        });

        adminFoundRejectBtn = UITheme.createDangerButton("REJECT");
        adminFoundRejectBtn.setEnabled(false);
        adminFoundRejectBtn.addActionListener(e -> {
            if (selectedFoundItem != null) {
                if (foundItemDAO.updateStatus(selectedFoundItem.getId(), "REJECTED")) {
                    JOptionPane.showMessageDialog(this,
                            "Found Item #" + selectedFoundItem.getId() + " marked as REJECTED.",
                            "Status Updated", JOptionPane.INFORMATION_MESSAGE);
                    refreshAdminFound();
                }
            }
        });

        btnBar.add(adminFoundApproveBtn);
        btnBar.add(adminFoundRejectBtn);
        bottomBox.add(btnBar, BorderLayout.SOUTH);

        center.add(bottomBox, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);
        refreshAdminFound();
        return panel;
    }

    private void refreshAdminFound() {
        adminFoundModel.setRowCount(0);
        selectedFoundItem = null;
        adminFoundDetailArea.setText("Select a Found Item above to view full details.");
        adminFoundApproveBtn.setEnabled(false);
        adminFoundRejectBtn.setEnabled(false);

        List<FoundItem> list = foundItemDAO.getAllFoundItems();
        for (FoundItem f : list) {
            adminFoundModel.addRow(new Object[]{
                    f.getId(), f.getItemName(), f.getCategory(), f.getFoundLocation(),
                    f.getFoundDate(), f.getFinderName(), f.getContactPhone(), f.getStatus()
            });
        }
    }

    private void handleAdminFoundRowSelection() {
        int row = adminFoundTable.getSelectedRow();
        if (row < 0) {
            selectedFoundItem = null;
            adminFoundApproveBtn.setEnabled(false);
            adminFoundRejectBtn.setEnabled(false);
            return;
        }

        int id = (int) adminFoundModel.getValueAt(row, 0);
        selectedFoundItem = foundItemDAO.getFoundItemById(id);
        if (selectedFoundItem != null) {
            adminFoundDetailArea.setText(
                    "Item Name: " + selectedFoundItem.getItemName() + "\n"
                    + "Category: " + selectedFoundItem.getCategory() + "\n"
                    + "Found Location: " + selectedFoundItem.getFoundLocation() + "\n"
                    + "Date: " + selectedFoundItem.getFoundDate() + "\n"
                    + "Finder Name: " + selectedFoundItem.getFinderName() + "\n"
                    + "Mobile: " + selectedFoundItem.getContactPhone() + "\n"
                    + "Status: " + selectedFoundItem.getStatus() + "\n"
                    + "Description: " + (selectedFoundItem.getDescription() != null ? selectedFoundItem.getDescription() : "N/A")
            );
            adminFoundApproveBtn.setEnabled(true);
            adminFoundRejectBtn.setEnabled(true);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN SECTION 4: REGISTERED USERS (Section 8: ID | Name | Roll No | Email | Mobile)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createAdminUsersCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("Registered Users",
                "Campus directory of all registered student and administrator accounts."), BorderLayout.CENTER);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh Users");
        refreshBtn.addActionListener(e -> refreshAdminUsers());
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tr.setBackground(UITheme.BG_APP);
        tr.add(refreshBtn);
        topBar.add(tr, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        // Columns required by Section 8: ID | Name | Roll No | Email | Mobile
        String[] cols = {"ID", "Name", "Roll No", "Email", "Mobile"};
        adminUsersModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminUsersTable = new JTable(adminUsersModel);
        UITheme.styleTable(adminUsersTable);

        JScrollPane sp = new JScrollPane(adminUsersTable);
        sp.setBorder(new LineBorder(UITheme.BORDER, 1));
        sp.getViewport().setBackground(UITheme.BG_INPUT);
        center.add(sp, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        refreshAdminUsers();
        return panel;
    }

    private void refreshAdminUsers() {
        adminUsersModel.setRowCount(0);
        List<User> list = userDAO.getAllUsers();
        for (User u : list) {
            adminUsersModel.addRow(new Object[]{
                    u.getId(),
                    u.getFullName(),
                    u.getCollegeId() != null ? u.getCollegeId() : "-",
                    u.getEmail(),
                    u.getPhone()
            });
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STUDENT SECTION 1: REPORT LOST ITEM (Section 13)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createStudentReportLostCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UITheme.BG_APP);
        wrapper.setBorder(new EmptyBorder(22, 28, 22, 28));

        wrapper.add(createHeaderBox("Report Lost Item",
                "Enter details of your missing campus belonging to register it into the system."), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UITheme.BG_APP);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        formCard.setPreferredSize(new Dimension(620, 560));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 6, 2, 6);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1.0;

        JTextField nameField = new JTextField();
        UITheme.styleTextField(nameField);

        JComboBox<String> catBox = new JComboBox<>(new String[]{
                "-- Select Category --", "Electronics", "Accessories", "Documents / ID Card", "Wallet / Bag",
                "Books / Stationery", "Keys", "Clothing", "Other"
        });
        UITheme.styleComboBox(catBox);

        JTextField locField = new JTextField();
        UITheme.styleTextField(locField);

        JTextField dateField = new JTextField(LocalDate.now().toString());
        UITheme.styleTextField(dateField);

        // Mobile Field with +91 fixed label
        JPanel phonePanel = new JPanel(new BorderLayout(6, 0));
        phonePanel.setBackground(UITheme.BG_PANEL);
        JLabel codeLabel = new JLabel("+91 ");
        codeLabel.setFont(UITheme.FONT_BOLD);
        codeLabel.setForeground(UITheme.TEXT_MUTED);
        codeLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
        phonePanel.add(codeLabel, BorderLayout.WEST);

        JTextField phoneField = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        UITheme.styleTextField(phoneField);
        phonePanel.add(phoneField, BorderLayout.CENTER);

        JTextArea descArea = new JTextArea(3, 20);
        UITheme.styleTextArea(descArea);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(UITheme.createFieldBorder());
        descScroll.getViewport().setBackground(UITheme.BG_INPUT);

        // Build Form Layout
        formCard.add(UITheme.createLabel("Item Name *"), gc);
        gc.gridy++;
        formCard.add(nameField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Category *"), gc);
        gc.gridy++;
        formCard.add(catBox, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Where Lost (Campus Location) *"), gc);
        gc.gridy++;
        formCard.add(locField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Lost Date (YYYY-MM-DD) *"), gc);
        gc.gridy++;
        formCard.add(dateField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Contact Mobile Number (10 Digits) *"), gc);
        gc.gridy++;
        formCard.add(phonePanel, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Description / Distinguishing Features"), gc);
        gc.gridy++;
        formCard.add(descScroll, gc);

        // Buttons
        gc.gridy++;
        gc.insets = new Insets(14, 6, 6, 6);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.BG_PANEL);

        JButton clearBtn = UITheme.createSecondaryButton("Reset Form");
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            catBox.setSelectedIndex(0);
            locField.setText("");
            descArea.setText("");
            dateField.setText(LocalDate.now().toString());
        });

        JButton submitBtn = UITheme.createPrimaryButton("Submit Lost Report");
        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String cat = (String) catBox.getSelectedItem();
            String loc = locField.getText().trim();
            String dateStr = dateField.getText().trim();
            String phone = validateMobileInput(phoneField);
            String desc = descArea.getText().trim();

            if (name.isEmpty() || catBox.getSelectedIndex() == 0 || loc.isEmpty() || dateStr.isEmpty() || phone == null) {
                if (phone != null) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (*).", "Input Required", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            Date lostDate;
            try {
                lostDate = Date.valueOf(dateStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in format YYYY-MM-DD.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LostItem item = new LostItem(currentUser.getId(), name, cat, loc, lostDate, desc, phone);
            if (lostItemDAO.addLostItem(item)) {
                JOptionPane.showMessageDialog(this,
                        "Lost item reported successfully!\nIt has been recorded into the campus database with status: PENDING.",
                        "Report Submitted", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                catBox.setSelectedIndex(0);
                locField.setText("");
                descArea.setText("");
                refreshStudentNotifications();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit report. Please check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRow.add(clearBtn);
        btnRow.add(submitBtn);
        formCard.add(btnRow, gc);

        centerPanel.add(formCard);

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UITheme.BG_APP);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STUDENT SECTION 2: REPORT FOUND ITEM (Section 13)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createStudentReportFoundCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UITheme.BG_APP);
        wrapper.setBorder(new EmptyBorder(22, 28, 22, 28));

        wrapper.add(createHeaderBox("Report Found Item",
                "Help return lost belongings to their rightful owner by turning them in to campus desk."), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UITheme.BG_APP);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        formCard.setPreferredSize(new Dimension(620, 560));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 6, 2, 6);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1.0;

        JTextField nameField = new JTextField();
        UITheme.styleTextField(nameField);

        JComboBox<String> catBox = new JComboBox<>(new String[]{
                "-- Select Category --", "Electronics", "Accessories", "Documents / ID Card", "Wallet / Bag",
                "Books / Stationery", "Keys", "Clothing", "Other"
        });
        UITheme.styleComboBox(catBox);

        JTextField foundLocField = new JTextField();
        UITheme.styleTextField(foundLocField);

        JTextField dateField = new JTextField(LocalDate.now().toString());
        UITheme.styleTextField(dateField);

        // Mobile Field with +91 fixed label
        JPanel phonePanel = new JPanel(new BorderLayout(6, 0));
        phonePanel.setBackground(UITheme.BG_PANEL);
        JLabel codeLabel = new JLabel("+91 ");
        codeLabel.setFont(UITheme.FONT_BOLD);
        codeLabel.setForeground(UITheme.TEXT_MUTED);
        codeLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
        phonePanel.add(codeLabel, BorderLayout.WEST);

        JTextField phoneField = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        UITheme.styleTextField(phoneField);
        phonePanel.add(phoneField, BorderLayout.CENTER);

        JTextArea descArea = new JTextArea(3, 20);
        UITheme.styleTextArea(descArea);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(UITheme.createFieldBorder());
        descScroll.getViewport().setBackground(UITheme.BG_INPUT);

        // Build Form Layout
        formCard.add(UITheme.createLabel("Item Name *"), gc);
        gc.gridy++;
        formCard.add(nameField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Category *"), gc);
        gc.gridy++;
        formCard.add(catBox, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Where Found (Campus Location) *"), gc);
        gc.gridy++;
        formCard.add(foundLocField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Found Date (YYYY-MM-DD) *"), gc);
        gc.gridy++;
        formCard.add(dateField, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Contact Mobile Number (10 Digits) *"), gc);
        gc.gridy++;
        formCard.add(phonePanel, gc);

        gc.gridy++;
        formCard.add(UITheme.createLabel("Description / Storage Location"), gc);
        gc.gridy++;
        formCard.add(descScroll, gc);

        // Buttons
        gc.gridy++;
        gc.insets = new Insets(14, 6, 6, 6);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.BG_PANEL);

        JButton clearBtn = UITheme.createSecondaryButton("Reset Form");
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            catBox.setSelectedIndex(0);
            foundLocField.setText("");
            descArea.setText("");
            dateField.setText(LocalDate.now().toString());
        });

        JButton submitBtn = UITheme.createPrimaryButton("Submit Found Report");
        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String cat = (String) catBox.getSelectedItem();
            String foundLoc = foundLocField.getText().trim();
            String dateStr = dateField.getText().trim();
            String phone = validateMobileInput(phoneField);
            String desc = descArea.getText().trim();

            if (name.isEmpty() || catBox.getSelectedIndex() == 0 || foundLoc.isEmpty() || dateStr.isEmpty() || phone == null) {
                if (phone != null) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (*).", "Input Required", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            Date foundDate;
            try {
                foundDate = Date.valueOf(dateStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in format YYYY-MM-DD.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            FoundItem item = new FoundItem(currentUser.getId(), name, cat, foundLoc, "Campus Helpdesk", foundDate, desc, phone);
            if (foundItemDAO.addFoundItem(item)) {
                JOptionPane.showMessageDialog(this,
                        "Found item recorded successfully!\nThank you for assisting your campus community. Status: PENDING.",
                        "Report Submitted", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                catBox.setSelectedIndex(0);
                foundLocField.setText("");
                descArea.setText("");
                refreshStudentNotifications();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit report. Please check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRow.add(clearBtn);
        btnRow.add(submitBtn);
        formCard.add(btnRow, gc);

        centerPanel.add(formCard);

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UITheme.BG_APP);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }    // ════════════════════════════════════════════════════════════════════════
    //  STUDENT SECTION 3: NOTIFICATIONS & APPROVALS (Accept items approved by Admin)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createStudentNotificationsCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("Notifications & Approvals",
                "Review admin-approved items, accept and claim your belongings, or return to admin."), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(UITheme.BG_APP);

        studentNotificationsFilterBox = new JComboBox<>(new String[]{
                "All Notifications & Reports",
                "Admin Approved (Ready to Claim)",
                "Claimed by Me",
                "Pending Admin Review"
        });
        UITheme.styleComboBox(studentNotificationsFilterBox);
        studentNotificationsFilterBox.setPreferredSize(new Dimension(210, 36));
        studentNotificationsFilterBox.addActionListener(e -> refreshStudentNotifications());
        controls.add(studentNotificationsFilterBox);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(95, 36));
        refreshBtn.addActionListener(e -> refreshStudentNotifications());
        controls.add(refreshBtn);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        // Results Table
        String[] cols = {"ID", "Item Name", "Category", "Report Type", "Location", "Date", "Status"};
        studentNotificationsModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentNotificationsTable = new JTable(studentNotificationsModel);
        UITheme.styleTable(studentNotificationsTable);
        studentNotificationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentNotificationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStudentNotificationsDetails();
            }
        });

        JScrollPane scrollTable = new JScrollPane(studentNotificationsTable);
        scrollTable.setBorder(new LineBorder(UITheme.BORDER, 1));
        scrollTable.getViewport().setBackground(UITheme.BG_INPUT);
        center.add(scrollTable, BorderLayout.CENTER);

        // Details & Confirmation Panel below
        JPanel detailBox = UITheme.createCardPanel();
        detailBox.setLayout(new BorderLayout(0, 8));
        detailBox.setPreferredSize(new Dimension(0, 215));
        detailBox.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel detailTitle = new JLabel("Notification Details & Item Verification Status:");
        detailTitle.setFont(UITheme.FONT_HEADER);
        detailTitle.setForeground(UITheme.TEXT_WHITE);
        detailBox.add(detailTitle, BorderLayout.NORTH);

        studentNotificationsDetailArea = new JTextArea("Select an item row above to view complete details.");
        studentNotificationsDetailArea.setFont(UITheme.FONT_REGULAR);
        studentNotificationsDetailArea.setForeground(UITheme.TEXT_MUTED);
        studentNotificationsDetailArea.setBackground(UITheme.BG_APP);
        studentNotificationsDetailArea.setEditable(false);
        studentNotificationsDetailArea.setLineWrap(true);
        studentNotificationsDetailArea.setWrapStyleWord(true);
        studentNotificationsDetailArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane scrollDetails = new JScrollPane(studentNotificationsDetailArea);
        scrollDetails.setBorder(new LineBorder(UITheme.BORDER, 1));
        detailBox.add(scrollDetails, BorderLayout.CENTER);

        // Action Bar: Hint label on left, Action Buttons on right
        JPanel actionRow = new JPanel(new BorderLayout(10, 0));
        actionRow.setBackground(UITheme.BG_PANEL);

        studentActionHintLabel = new JLabel("");
        studentActionHintLabel.setFont(UITheme.FONT_BOLD);
        studentActionHintLabel.setForeground(new Color(56, 189, 248));
        actionRow.add(studentActionHintLabel, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnGroup.setBackground(UITheme.BG_PANEL);

        studentAcceptClaimBtn = UITheme.createSuccessButton("Accept & Claim Item");
        studentAcceptClaimBtn.setEnabled(false);
        studentAcceptClaimBtn.addActionListener(e -> handleStudentDecision("CLAIMED"));

        studentRejectClaimBtn = UITheme.createDangerButton("Reject / Return to Admin");
        studentRejectClaimBtn.setEnabled(false);
        studentRejectClaimBtn.addActionListener(e -> handleStudentDecision("PENDING"));

        btnGroup.add(studentAcceptClaimBtn);
        btnGroup.add(studentRejectClaimBtn);
        actionRow.add(btnGroup, BorderLayout.EAST);
        detailBox.add(actionRow, BorderLayout.SOUTH);

        center.add(detailBox, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        refreshStudentNotifications();
        return panel;
    }

    private void refreshStudentNotifications() {
        if (studentNotificationsModel == null) return;
        studentNotificationsModel.setRowCount(0);
        studentNotificationsRecordList.clear();

        int filterIdx = studentNotificationsFilterBox != null ? studentNotificationsFilterBox.getSelectedIndex() : 0;

        List<LostItem> myLost = lostItemDAO.getLostItemsByUser(currentUser.getId());
        for (LostItem l : myLost) {
            String st = l.getStatus();
            if (filterIdx == 1 && !"APPROVED".equalsIgnoreCase(st)) continue;
            if (filterIdx == 2 && !"CLAIMED".equalsIgnoreCase(st) && !"CONFIRMED".equalsIgnoreCase(st)) continue;
            if (filterIdx == 3 && !"PENDING".equalsIgnoreCase(st)) continue;

            studentNotificationsModel.addRow(new Object[]{
                    l.getId(), l.getItemName(), l.getCategory(), "LOST",
                    l.getLocation(), l.getLostDate(), l.getStatus()
            });
            studentNotificationsRecordList.add(l);
        }

        List<FoundItem> myFound = foundItemDAO.getFoundItemsByUser(currentUser.getId());
        for (FoundItem f : myFound) {
            String st = f.getStatus();
            if (filterIdx == 1 && !"APPROVED".equalsIgnoreCase(st)) continue;
            if (filterIdx == 2 && !"CLAIMED".equalsIgnoreCase(st) && !"CONFIRMED".equalsIgnoreCase(st)) continue;
            if (filterIdx == 3 && !"PENDING".equalsIgnoreCase(st)) continue;

            studentNotificationsModel.addRow(new Object[]{
                    f.getId(), f.getItemName(), f.getCategory(), "FOUND",
                    f.getFoundLocation(), f.getFoundDate(), f.getStatus()
            });
            studentNotificationsRecordList.add(f);
        }

        if (studentNotificationsModel.getRowCount() > 0) {
            studentNotificationsTable.setRowSelectionInterval(0, 0);
        } else {
            if (studentNotificationsDetailArea != null) {
                studentNotificationsDetailArea.setText("No notification records matching the selected filter.\nWhen Campus Admin approves your lost or found items, you can accept and claim them here.");
            }
            if (studentAcceptClaimBtn != null) studentAcceptClaimBtn.setEnabled(false);
            if (studentRejectClaimBtn != null) studentRejectClaimBtn.setEnabled(false);
            if (studentActionHintLabel != null) studentActionHintLabel.setText("");
        }
    }

    private void updateStudentNotificationsDetails() {
        int row = studentNotificationsTable.getSelectedRow();
        if (row < 0 || row >= studentNotificationsRecordList.size()) {
            if (studentNotificationsDetailArea != null) {
                studentNotificationsDetailArea.setText("Select an item row above to view complete details.");
            }
            if (studentAcceptClaimBtn != null) studentAcceptClaimBtn.setEnabled(false);
            if (studentRejectClaimBtn != null) studentRejectClaimBtn.setEnabled(false);
            if (studentActionHintLabel != null) studentActionHintLabel.setText("");
            return;
        }

        Object obj = studentNotificationsRecordList.get(row);
        String status = "";

        if (obj instanceof LostItem) {
            LostItem l = (LostItem) obj;
            status = l.getStatus();
            String statusNote = "PENDING".equalsIgnoreCase(status)
                    ? "PENDING (Under Administration Review - Waiting for Approval)"
                    : ("APPROVED".equalsIgnoreCase(status)
                            ? "APPROVED (Verified by Admin - Click [ Accept & Claim Item ] to claim)"
                            : ("CLAIMED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status)
                                    ? "CLAIMED (Successfully Claimed & Added to 'My Claimed Items')"
                                    : "REJECTED (Report Not Approved by Administration)"));

            studentNotificationsDetailArea.setText(
                    "Report Type: LOST ITEM REPORT\n"
                    + "Item Name: " + l.getItemName() + "\n"
                    + "Category: " + l.getCategory() + "\n"
                    + "Lost Location: " + l.getLocation() + "\n"
                    + "Date Reported: " + l.getLostDate() + "\n"
                    + "Contact Mobile: +91 " + l.getContactPhone() + "\n"
                    + "Current Status: " + statusNote + "\n"
                    + "Description: " + (l.getDescription() != null && !l.getDescription().isEmpty() ? l.getDescription() : "None provided")
            );
        } else if (obj instanceof FoundItem) {
            FoundItem f = (FoundItem) obj;
            status = f.getStatus();
            String statusNote = "PENDING".equalsIgnoreCase(status)
                    ? "PENDING (Under Administration Review - Waiting for Approval)"
                    : ("APPROVED".equalsIgnoreCase(status)
                            ? "APPROVED (Verified by Admin - Click [ Accept & Claim Item ] to claim)"
                            : ("CLAIMED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status)
                                    ? "CLAIMED (Successfully Claimed & Added to 'My Claimed Items')"
                                    : "REJECTED (Report Not Approved by Administration)"));

            studentNotificationsDetailArea.setText(
                    "Report Type: FOUND ITEM REPORT\n"
                    + "Item Name: " + f.getItemName() + "\n"
                    + "Category: " + f.getCategory() + "\n"
                    + "Found Location: " + f.getFoundLocation() + "\n"
                    + "Storage Location: " + (f.getStorageLocation() != null ? f.getStorageLocation() : "Campus Administration Desk") + "\n"
                    + "Date Reported: " + f.getFoundDate() + "\n"
                    + "Contact Mobile: +91 " + f.getContactPhone() + "\n"
                    + "Current Status: " + statusNote + "\n"
                    + "Description: " + (f.getDescription() != null && !f.getDescription().isEmpty() ? f.getDescription() : "None provided")
            );
        }

        // Enable action buttons if item was approved by admin
        if ("APPROVED".equalsIgnoreCase(status)) {
            studentAcceptClaimBtn.setEnabled(true);
            studentRejectClaimBtn.setEnabled(true);
            studentActionHintLabel.setText("● Admin Approved! Ready to claim.");
        } else if ("CLAIMED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status)) {
            studentAcceptClaimBtn.setEnabled(false);
            studentRejectClaimBtn.setEnabled(false);
            studentActionHintLabel.setText("✓ Claimed by you (Recorded in 'My Claimed Items')");
        } else if ("PENDING".equalsIgnoreCase(status)) {
            studentAcceptClaimBtn.setEnabled(false);
            studentRejectClaimBtn.setEnabled(false);
            studentActionHintLabel.setText("● Awaiting Admin Review");
        } else {
            studentAcceptClaimBtn.setEnabled(false);
            studentRejectClaimBtn.setEnabled(false);
            studentActionHintLabel.setText("✕ Report Rejected by Admin");
        }
    }

    private void handleStudentDecision(String newStatus) {
        int row = studentNotificationsTable.getSelectedRow();
        if (row < 0 || row >= studentNotificationsRecordList.size()) return;

        Object obj = studentNotificationsRecordList.get(row);
        boolean success = false;
        String name = "";

        if (obj instanceof LostItem) {
            LostItem l = (LostItem) obj;
            name = l.getItemName();
            success = lostItemDAO.updateStatus(l.getId(), newStatus);
        } else if (obj instanceof FoundItem) {
            FoundItem f = (FoundItem) obj;
            name = f.getItemName();
            success = foundItemDAO.updateStatus(f.getId(), newStatus);
        }

        if (success) {
            if ("CLAIMED".equalsIgnoreCase(newStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Item '" + name + "' accepted and claimed!\n"
                        + "It has been officially claimed from Admin, removed from Admin's Lost Items Approval queue,\n"
                        + "and added to your 'My Claimed Items' section as well as Admin's 'Claimed Items Details'.",
                        "Item Claimed Successfully", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Item '" + name + "' rejected by you.\nIt has been returned back to the Administration queue for re-verification.",
                        "Returned to Admin", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshStudentNotifications();
            refreshStudentClaimed();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkStudentNotifications() {
        if (isAdmin) return;
        List<LostItem> myLost = lostItemDAO.getLostItemsByUser(currentUser.getId());
        List<FoundItem> myFound = foundItemDAO.getFoundItemsByUser(currentUser.getId());
        int approvedCount = 0;
        for (LostItem l : myLost) {
            if ("APPROVED".equalsIgnoreCase(l.getStatus())) approvedCount++;
        }
        for (FoundItem f : myFound) {
            if ("APPROVED".equalsIgnoreCase(f.getStatus())) approvedCount++;
        }

        if (approvedCount > 0) {
            final int count = approvedCount;
            if (Boolean.getBoolean("test.preview")) return;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "🔔 New Notification from Campus Admin:\n\n"
                        + "You have " + count + " item report(s) approved by Administration!\n"
                        + "Please open 'Notifications & Approvals' to accept and claim your item.",
                        "Admin Verification Notification", JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STUDENT SECTION 4: MY CLAIMED ITEMS (Items Claimed by Student from Admin)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel createStudentClaimedCard() {
        JPanel panel = createPageRoot();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_APP);
        topBar.add(createHeaderBox("My Claimed Items",
                "Official records of items claimed by you from campus administration."), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(UITheme.BG_APP);

        studentClaimedFilterBox = new JComboBox<>(new String[]{"All My Claimed Items", "Claimed Lost Items", "Claimed Found Items"});
        UITheme.styleComboBox(studentClaimedFilterBox);
        studentClaimedFilterBox.setPreferredSize(new Dimension(180, 36));
        studentClaimedFilterBox.addActionListener(e -> refreshStudentClaimed());
        controls.add(studentClaimedFilterBox);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(95, 36));
        refreshBtn.addActionListener(e -> refreshStudentClaimed());
        controls.add(refreshBtn);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_APP);

        // Table
        String[] cols = {"ID", "Item Name", "Category", "Report Type", "Location", "Date", "Contact", "Status"};
        studentClaimedModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentClaimedTable = new JTable(studentClaimedModel);
        UITheme.styleTable(studentClaimedTable);
        studentClaimedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentClaimedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStudentClaimedDetails();
            }
        });

        JScrollPane scrollTable = new JScrollPane(studentClaimedTable);
        scrollTable.setBorder(new LineBorder(UITheme.BORDER, 1));
        scrollTable.getViewport().setBackground(UITheme.BG_INPUT);
        center.add(scrollTable, BorderLayout.CENTER);

        // Details Panel below
        JPanel detailBox = UITheme.createCardPanel();
        detailBox.setLayout(new BorderLayout(0, 10));
        detailBox.setPreferredSize(new Dimension(0, 180));
        detailBox.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel detailTitle = new JLabel("Claimed Item Handover Details:");
        detailTitle.setFont(UITheme.FONT_HEADER);
        detailTitle.setForeground(UITheme.TEXT_WHITE);
        detailBox.add(detailTitle, BorderLayout.NORTH);

        studentClaimedDetailArea = new JTextArea("Select an item row above to view complete claimed details.");
        studentClaimedDetailArea.setFont(UITheme.FONT_REGULAR);
        studentClaimedDetailArea.setForeground(UITheme.TEXT_MUTED);
        studentClaimedDetailArea.setBackground(UITheme.BG_APP);
        studentClaimedDetailArea.setEditable(false);
        studentClaimedDetailArea.setLineWrap(true);
        studentClaimedDetailArea.setWrapStyleWord(true);
        studentClaimedDetailArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane scrollDetails = new JScrollPane(studentClaimedDetailArea);
        scrollDetails.setBorder(new LineBorder(UITheme.BORDER, 1));
        detailBox.add(scrollDetails, BorderLayout.CENTER);

        center.add(detailBox, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        refreshStudentClaimed();
        return panel;
    }

    private void refreshStudentClaimed() {
        if (studentClaimedModel == null) return;
        studentClaimedModel.setRowCount(0);
        studentClaimedRecordList.clear();

        int filterIdx = studentClaimedFilterBox != null ? studentClaimedFilterBox.getSelectedIndex() : 0;

        if (filterIdx == 0 || filterIdx == 1) {
            List<LostItem> myLost = lostItemDAO.getLostItemsByUser(currentUser.getId());
            for (LostItem l : myLost) {
                if ("CLAIMED".equalsIgnoreCase(l.getStatus()) || "CONFIRMED".equalsIgnoreCase(l.getStatus())) {
                    studentClaimedModel.addRow(new Object[]{
                            l.getId(), l.getItemName(), l.getCategory(), "LOST",
                            l.getLocation(), l.getLostDate(), l.getContactPhone(), "CLAIMED"
                    });
                    studentClaimedRecordList.add(l);
                }
            }
        }

        if (filterIdx == 0 || filterIdx == 2) {
            List<FoundItem> myFound = foundItemDAO.getFoundItemsByUser(currentUser.getId());
            for (FoundItem f : myFound) {
                if ("CLAIMED".equalsIgnoreCase(f.getStatus()) || "CONFIRMED".equalsIgnoreCase(f.getStatus())) {
                    studentClaimedModel.addRow(new Object[]{
                            f.getId(), f.getItemName(), f.getCategory(), "FOUND",
                            f.getFoundLocation(), f.getFoundDate(), f.getContactPhone(), "CLAIMED"
                    });
                    studentClaimedRecordList.add(f);
                }
            }
        }

        if (studentClaimedModel.getRowCount() > 0) {
            studentClaimedTable.setRowSelectionInterval(0, 0);
        } else if (studentClaimedDetailArea != null) {
            studentClaimedDetailArea.setText("No claimed items found.\nWhen you accept an admin-approved notification, the item will appear in this list.");
        }
    }

    private void updateStudentClaimedDetails() {
        int row = studentClaimedTable.getSelectedRow();
        if (row < 0 || row >= studentClaimedRecordList.size()) {
            if (studentClaimedDetailArea != null) {
                studentClaimedDetailArea.setText("Select an item row above to view complete claimed details.");
            }
            return;
        }

        Object obj = studentClaimedRecordList.get(row);
        if (obj instanceof LostItem) {
            LostItem l = (LostItem) obj;
            studentClaimedDetailArea.setText(
                    "Item Type: LOST ITEM (Claimed & Confirmed by You)\n"
                    + "Item Name: " + l.getItemName() + "\n"
                    + "Category: " + l.getCategory() + "\n"
                    + "Lost Location: " + l.getLocation() + "\n"
                    + "Date Reported: " + l.getLostDate() + "\n"
                    + "Your Registered Mobile: +91 " + l.getContactPhone() + "\n"
                    + "Status: CLAIMED (Officially handed over and claimed from Campus Admin)\n"
                    + "Description: " + (l.getDescription() != null && !l.getDescription().isEmpty() ? l.getDescription() : "None provided")
            );
        } else if (obj instanceof FoundItem) {
            FoundItem f = (FoundItem) obj;
            studentClaimedDetailArea.setText(
                    "Item Type: FOUND ITEM (Claimed & Confirmed by You)\n"
                    + "Item Name: " + f.getItemName() + "\n"
                    + "Category: " + f.getCategory() + "\n"
                    + "Found Location: " + f.getFoundLocation() + "\n"
                    + "Desk Storage: " + (f.getStorageLocation() != null ? f.getStorageLocation() : "Campus Administration Desk") + "\n"
                    + "Date Reported: " + f.getFoundDate() + "\n"
                    + "Your Registered Mobile: +91 " + f.getContactPhone() + "\n"
                    + "Status: CLAIMED (Officially handed over and claimed from Campus Admin)\n"
                    + "Description: " + (f.getDescription() != null && !f.getDescription().isEmpty() ? f.getDescription() : "None provided")
            );
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MOBILE NUMBER VALIDATION & PAGE HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private boolean validateMobile(String phone) {
        if (phone == null) return false;
        String clean = phone.trim();
        if (clean.startsWith("+91")) clean = clean.substring(3).trim();
        return clean.matches("\\d{10}");
    }

    /**
     * Validates that the entered mobile number contains EXACTLY 10 digits (Section 11 & 12).
     */
    private String validateMobileInput(JTextField field) {
        String phone = field.getText().trim();
        if (phone.startsWith("+91")) {
            phone = phone.substring(3).trim();
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Mobile number must contain exactly 10 digits.",
                    "Invalid Mobile Number", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            return null;
        }
        return phone;
    }

    private JPanel createPageRoot() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(UITheme.BG_APP);
        p.setBorder(new EmptyBorder(22, 28, 22, 28));
        return p;
    }

    private JPanel createHeaderBox(String title, String subtitle) {
        JPanel box = new JPanel(new BorderLayout(0, 4));
        box.setBackground(UITheme.BG_APP);

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_TITLE);
        t.setForeground(UITheme.TEXT_WHITE);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(UITheme.FONT_SUBTITLE);
        sub.setForeground(UITheme.TEXT_MUTED);

        box.add(t, BorderLayout.NORTH);
        box.add(sub, BorderLayout.CENTER);
        return box;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out of the campus portal?",
                "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
