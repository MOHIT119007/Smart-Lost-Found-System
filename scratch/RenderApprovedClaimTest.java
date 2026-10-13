import ui.DashboardFrame;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class RenderApprovedClaimTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                User student = new User(2, "Mohit Kumar Sharma", "mohit.sharma@campus.edu", "student123", "STUDENT", "9829012345", "24EARAD103");
                DashboardFrame studentFrame = new DashboardFrame(student);
                studentFrame.setSize(1220, 750);
                studentFrame.setLocationRelativeTo(null);
                studentFrame.setVisible(true);
                Thread.sleep(300);

                java.lang.reflect.Field navField = DashboardFrame.class.getDeclaredField("navButtons");
                navField.setAccessible(true);
                java.util.List<JButton> sNavs = (java.util.List<JButton>) navField.get(studentFrame);
                sNavs.get(3).doClick(); // My Claims & Status
                Thread.sleep(300);

                java.lang.reflect.Field sTableField = DashboardFrame.class.getDeclaredField("studentClaimsTable");
                sTableField.setAccessible(true);
                JTable sTable = (JTable) sTableField.get(studentFrame);

                // Find a row with APPROVED status or row index 2
                int targetRow = -1;
                for (int r = 0; r < sTable.getRowCount(); r++) {
                    String st = String.valueOf(sTable.getValueAt(r, 3));
                    if (st.contains("FOUND") || st.contains("APPROVED") || st.contains("Action")) {
                        targetRow = r;
                        break;
                    }
                }
                if (targetRow >= 0) {
                    sTable.setRowSelectionInterval(targetRow, targetRow);
                } else if (sTable.getRowCount() > 0) {
                    sTable.setRowSelectionInterval(0, 0);
                }
                Thread.sleep(300);

                BufferedImage img = new BufferedImage(1220, 750, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                studentFrame.paint(g);
                g.dispose();
                ImageIO.write(img, "png", new File("scratch/student_approved_claim.png"));
                System.out.println("Saved student_approved_claim.png for row " + targetRow);
                studentFrame.dispose();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
