import ui.DashboardFrame;
import ui.RegisterDialog;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class RenderDashboardsPreview {
    public static void main(String[] args) {
        System.setProperty("test.preview", "true");
        SwingUtilities.invokeLater(() -> {
            try {
                Field navField = DashboardFrame.class.getDeclaredField("navButtons");
                navField.setAccessible(true);

                // ── 1. Admin Dashboard Previews ─────────────────────────────────────
                User admin = new User(1, "Campus Administrator", "admin@campus.edu", "admin123", "ADMIN", "9876543210", "ADM-001");
                DashboardFrame adminFrame = new DashboardFrame(admin);
                adminFrame.setSize(1220, 750);
                adminFrame.setLocationRelativeTo(null);
                adminFrame.setVisible(true);
                Thread.sleep(400);

                List<JButton> aNavs = (List<JButton>) navField.get(adminFrame);

                // Admin Tab 0: Registered Users Details
                if (aNavs.size() > 0) {
                    aNavs.get(0).doClick();
                    Thread.sleep(300);
                    saveFrame(adminFrame, "scratch/admin_users_preview.png");
                }

                // Admin Tab 1: Register Found Items
                if (aNavs.size() > 1) {
                    aNavs.get(1).doClick();
                    Thread.sleep(300);
                    Field foundTableField = DashboardFrame.class.getDeclaredField("adminFoundTable");
                    foundTableField.setAccessible(true);
                    JTable fTable = (JTable) foundTableField.get(adminFrame);
                    if (fTable.getRowCount() > 0) {
                        fTable.setRowSelectionInterval(0, 0);
                        Thread.sleep(200);
                    }
                    saveFrame(adminFrame, "scratch/admin_found_preview.png");
                }

                // Admin Tab 2: Lost Items Approval
                if (aNavs.size() > 2) {
                    aNavs.get(2).doClick();
                    Thread.sleep(300);
                    Field lostTableField = DashboardFrame.class.getDeclaredField("adminLostTable");
                    lostTableField.setAccessible(true);
                    JTable lTable = (JTable) lostTableField.get(adminFrame);
                    if (lTable.getRowCount() > 0) {
                        lTable.setRowSelectionInterval(0, 0);
                        Thread.sleep(200);
                    }
                    saveFrame(adminFrame, "scratch/admin_lost_preview.png");
                }

                // Admin Tab 3: Claimed Items Details
                if (aNavs.size() > 3) {
                    aNavs.get(3).doClick();
                    Thread.sleep(300);
                    Field claimedTableField = DashboardFrame.class.getDeclaredField("adminClaimedTable");
                    claimedTableField.setAccessible(true);
                    JTable cTable = (JTable) claimedTableField.get(adminFrame);
                    if (cTable.getRowCount() > 0) {
                        cTable.setRowSelectionInterval(0, 0);
                        Thread.sleep(200);
                    }
                    saveFrame(adminFrame, "scratch/admin_claimed_preview.png");
                }
                adminFrame.dispose();

                // ── 2. Student Dashboard Previews ───────────────────────────────────
                User student = new User(11, "mohit", "m@k.s", "12345", "STUDENT", "0987654321", "102");
                DashboardFrame studentFrame = new DashboardFrame(student);
                studentFrame.setSize(1220, 750);
                studentFrame.setLocationRelativeTo(null);
                studentFrame.setVisible(true);
                Thread.sleep(400);

                List<JButton> sNavs = (List<JButton>) navField.get(studentFrame);

                // Student Tab 0: Report Lost Item
                saveFrame(studentFrame, "scratch/student_lost_preview.png");

                // Student Tab 1: Report Found Item
                if (sNavs.size() > 1) {
                    sNavs.get(1).doClick();
                    Thread.sleep(300);
                    saveFrame(studentFrame, "scratch/student_found_preview.png");
                }

                // Student Tab 2: Notifications & Approvals
                if (sNavs.size() > 2) {
                    sNavs.get(2).doClick();
                    Thread.sleep(300);
                    Field notifTableField = DashboardFrame.class.getDeclaredField("studentNotificationsTable");
                    notifTableField.setAccessible(true);
                    JTable nTable = (JTable) notifTableField.get(studentFrame);
                    if (nTable != null && nTable.getRowCount() > 0) {
                        nTable.setRowSelectionInterval(0, 0);
                        Thread.sleep(200);
                    }
                    saveFrame(studentFrame, "scratch/student_notifications_preview.png");
                }

                // Student Tab 3: My Claimed Items
                if (sNavs.size() > 3) {
                    sNavs.get(3).doClick();
                    Thread.sleep(300);
                    Field claimedTableField = DashboardFrame.class.getDeclaredField("studentClaimedTable");
                    claimedTableField.setAccessible(true);
                    JTable sClaimTable = (JTable) claimedTableField.get(studentFrame);
                    if (sClaimTable != null && sClaimTable.getRowCount() > 0) {
                        sClaimTable.setRowSelectionInterval(0, 0);
                        Thread.sleep(200);
                    }
                    saveFrame(studentFrame, "scratch/student_claimed_preview.png");
                }
                studentFrame.dispose();

                System.out.println("ALL PREVIEWS GENERATED SUCCESSFULLY!");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    private static void saveFrame(JFrame frame, String path) throws Exception {
        frame.validate();
        frame.repaint();
        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        frame.paint(g);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Saved " + path);
    }
}
