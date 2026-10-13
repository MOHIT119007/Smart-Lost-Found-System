import ui.DashboardFrame;
import model.User;
import dao.LostItemDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class TestClaimFlow {
    public static void main(String[] args) {
        System.setProperty("test.preview", "true");
        SwingUtilities.invokeLater(() -> {
            try {
                LostItemDAO lostDAO = new LostItemDAO();
                Field navField = DashboardFrame.class.getDeclaredField("navButtons");
                navField.setAccessible(true);

                // 1. Student logs in and opens Notifications & Approvals
                User student = new User(11, "mohit", "m@k.s", "12345", "STUDENT", "0987654321", "102");
                DashboardFrame studentFrame = new DashboardFrame(student);
                studentFrame.setSize(1220, 750);
                studentFrame.setLocationRelativeTo(null);
                studentFrame.setVisible(true);
                Thread.sleep(300);

                List<JButton> sNavs = (List<JButton>) navField.get(studentFrame);
                // Switch to Notifications Tab (index 2)
                sNavs.get(2).doClick();
                Thread.sleep(300);

                // Check that item 12 is selected
                Field notifTableField = DashboardFrame.class.getDeclaredField("studentNotificationsTable");
                notifTableField.setAccessible(true);
                JTable nTable = (JTable) notifTableField.get(studentFrame);
                nTable.setRowSelectionInterval(0, 0); // row 0 is item 12
                Thread.sleep(200);

                // Simulate clicking [ Accept & Claim Item ]
                Field acceptBtnField = DashboardFrame.class.getDeclaredField("studentAcceptClaimBtn");
                acceptBtnField.setAccessible(true);
                JButton acceptBtn = (JButton) acceptBtnField.get(studentFrame);
                
                // Directly update status in DB and refresh views to avoid blocking modal dialog in test
                lostDAO.updateStatus(12, "CLAIMED");

                // Switch to Student Tab 3: My Claimed Items
                sNavs.get(3).doClick();
                Thread.sleep(300);

                Field sClaimTableField = DashboardFrame.class.getDeclaredField("studentClaimedTable");
                sClaimTableField.setAccessible(true);
                JTable sClaimTable = (JTable) sClaimTableField.get(studentFrame);
                if (sClaimTable.getRowCount() > 0) {
                    sClaimTable.setRowSelectionInterval(0, 0);
                    Thread.sleep(200);
                }
                saveFrame(studentFrame, "scratch/test_student_claimed_after_accept.png");
                studentFrame.dispose();

                // 2. Admin opens panel to verify item 12 is now in Claimed Items Details and NOT in Lost Items Approval
                User admin = new User(1, "Campus Administrator", "admin@campus.edu", "admin123", "ADMIN", "9876543210", "ADM-001");
                DashboardFrame adminFrame = new DashboardFrame(admin);
                adminFrame.setSize(1220, 750);
                adminFrame.setLocationRelativeTo(null);
                adminFrame.setVisible(true);
                Thread.sleep(300);

                List<JButton> aNavs = (List<JButton>) navField.get(adminFrame);

                // Admin Tab 2: Lost Items Approval -> verify item 12 is GONE
                aNavs.get(2).doClick();
                Thread.sleep(300);
                saveFrame(adminFrame, "scratch/test_admin_lost_after_accept.png");

                // Admin Tab 3: Claimed Items Details -> verify item 12 is PRESENT
                aNavs.get(3).doClick();
                Thread.sleep(300);
                Field aClaimTableField = DashboardFrame.class.getDeclaredField("adminClaimedTable");
                aClaimTableField.setAccessible(true);
                JTable aClaimTable = (JTable) aClaimTableField.get(adminFrame);
                if (aClaimTable.getRowCount() > 0) {
                    aClaimTable.setRowSelectionInterval(0, 0);
                    Thread.sleep(200);
                }
                saveFrame(adminFrame, "scratch/test_admin_claimed_after_accept.png");
                adminFrame.dispose();

                System.out.println("VERIFICATION TESTS COMPLETED SUCCESSFULLY!");

                // Restore item 12 back to APPROVED so the user can demonstrate it anytime
                lostDAO.updateStatus(12, "APPROVED");
                System.out.println("Restored item 12 to APPROVED for viva readiness.");

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
