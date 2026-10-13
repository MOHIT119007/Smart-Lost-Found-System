package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * UI Theme and styling configurations for Smart Lost & Found System.
 */
public class UITheme {

    public static final Color BG_APP          = new Color(11, 19, 43);
    public static final Color BG_SIDEBAR      = new Color(10, 17, 36);
    public static final Color BG_HEADER       = new Color(10, 17, 36);
    public static final Color BG_PANEL        = new Color(22, 34, 56);
    public static final Color BG_PANEL_ALT    = new Color(28, 41, 65);
    public static final Color BG_INPUT        = new Color(15, 23, 42);
    public static final Color BG_TABLE_HEADER = new Color(30, 41, 59);

    public static final Color BORDER          = new Color(44, 62, 96);
    public static final Color BORDER_ACTIVE   = new Color(59, 130, 246);

    public static final Color PRIMARY         = new Color(37, 99, 235);
    public static final Color PRIMARY_HOVER   = new Color(29, 78, 216);
    public static final Color SECONDARY       = new Color(51, 65, 85);
    public static final Color SECONDARY_HOVER = new Color(71, 85, 105);
    public static final Color SUCCESS         = new Color(16, 185, 129);
    public static final Color SUCCESS_HOVER   = new Color(5, 150, 105);
    public static final Color WARNING         = new Color(245, 158, 11);
    public static final Color WARNING_HOVER   = new Color(217, 119, 6);
    public static final Color DANGER          = new Color(239, 68, 68);
    public static final Color DANGER_HOVER    = new Color(220, 38, 38);

    public static final Color TEXT_MAIN       = new Color(255, 255, 255);
    public static final Color TEXT_MUTED      = new Color(148, 163, 184);
    public static final Color TEXT_WHITE      = new Color(255, 255, 255);

    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);

    public static JLabel createTitleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_TITLE);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADER);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    public static JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    public static JLabel createMutedLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SUBTITLE);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, TEXT_WHITE, PRIMARY_HOVER);
    }

    public static JButton createSecondaryButton(String text) {
        return createButton(text, SECONDARY, TEXT_WHITE, SECONDARY_HOVER);
    }

    public static JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS, TEXT_WHITE, SUCCESS_HOVER);
    }

    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER, TEXT_WHITE, DANGER_HOVER);
    }

    private static JButton createButton(String text, Color bg, Color fg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        Dimension pref = btn.getPreferredSize();
        int minW = Math.max(pref.width + 24, 100);
        btn.setPreferredSize(new Dimension(minW, 36));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(bg);
            }
        });
        return btn;
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
        field.setBorder(createFieldBorder());
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_REGULAR);
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
        field.setBorder(createFieldBorder());
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_REGULAR);
        area.setBackground(BG_INPUT);
        area.setForeground(TEXT_WHITE);
        area.setCaretColor(TEXT_WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(8, 10, 8, 10));
    }

    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_REGULAR);
        box.setBackground(BG_INPUT);
        box.setForeground(TEXT_WHITE);
        box.setPreferredSize(new Dimension(box.getPreferredSize().width, 36));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? PRIMARY : BG_INPUT);
                c.setForeground(TEXT_WHITE);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return c;
            }
        });
    }

    public static Border createFieldBorder() {
        return new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(7, 12, 7, 12));
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(34);
        table.setBackground(BG_INPUT);
        table.setForeground(TEXT_WHITE);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(37, 99, 235));
        table.setSelectionForeground(TEXT_WHITE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(new Color(24, 38, 64));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean isSelected, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, isSelected, hasFocus, r, c);
                l.setBackground(new Color(24, 38, 64));
                l.setForeground(Color.WHITE);
                l.setFont(FONT_LABEL);
                l.setOpaque(true);
                l.setHorizontalAlignment(SwingConstants.LEFT);
                l.setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 2, 1, new Color(44, 62, 96)),
                        new EmptyBorder(8, 12, 8, 12)));
                return l;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean isSelected, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, r, c);
                String text = val != null ? val.toString().trim() : "";

                if (!isSelected) {
                    comp.setBackground(r % 2 == 0 ? BG_INPUT : new Color(20, 31, 52));
                    comp.setForeground(TEXT_WHITE);
                }

                if ("CLAIMED".equalsIgnoreCase(text) || "CONFIRMED".equalsIgnoreCase(text) || "RESOLVED".equalsIgnoreCase(text) || "RETURNED".equalsIgnoreCase(text)) {
                    comp.setForeground(SUCCESS);
                    setFont(FONT_BUTTON);
                } else if ("APPROVED".equalsIgnoreCase(text)) {
                    comp.setForeground(new Color(56, 189, 248));
                    setFont(FONT_BUTTON);
                } else if ("PENDING".equalsIgnoreCase(text)) {
                    comp.setForeground(WARNING);
                    setFont(FONT_BUTTON);
                } else if ("REJECTED".equalsIgnoreCase(text)) {
                    comp.setForeground(DANGER);
                    setFont(FONT_BUTTON);
                } else if ("ACTIVE".equalsIgnoreCase(text)) {
                    comp.setForeground(new Color(96, 165, 250));
                    setFont(FONT_BUTTON);
                }

                setBorder(new EmptyBorder(0, 10, 0, 10));
                return comp;
            }
        });
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(BG_PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)));
        return card;
    }

    public static Image getAppLogo() {
        try {
            InputStream is = UITheme.class.getResourceAsStream("/resources/logo.png");
            if (is == null) {
                is = UITheme.class.getClassLoader().getResourceAsStream("resources/logo.png");
            }
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (Exception ignored) {}

        File[] candidateFiles = {
                new File("src/resources/logo.png"),
                new File("resources/logo.png")
        };
        for (File f : candidateFiles) {
            if (f.exists()) {
                try {
                    return ImageIO.read(f);
                } catch (Exception ignored) {}
            }
        }
        // Programmatic Fallback
        BufferedImage img = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(PRIMARY);
        g.fillRoundRect(0, 0, 48, 48, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.drawString("SL", 11, 32);
        g.dispose();
        return img;
    }

    public static ImageIcon getAppLogoIcon(int width, int height) {
        Image img = getAppLogo();
        if (img != null) {
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        return new ImageIcon();
    }
}
