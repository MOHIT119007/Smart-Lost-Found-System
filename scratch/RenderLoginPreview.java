import ui.LoginFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class RenderLoginPreview {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.pack();
                // LoginFrame has setSize(480, 585) in initComponents, but let's ensure it has its set size
                frame.setSize(480, 550);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Wait a moment for layout to settle
                Thread.sleep(300);

                Dimension size = frame.getSize();
                BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                frame.paint(g2);
                g2.dispose();

                File output = new File("scratch/login_preview.png");
                ImageIO.write(image, "png", output);
                System.out.println("Login frame preview rendered: " + output.getAbsolutePath());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
