import javax.swing.*;
import java.awt.*;

public class NextWindow extends JFrame {

    public NextWindow() {
        setTitle("Main System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("NEXT PAGE LOADED 🚆", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        add(label);

        setVisible(true);
    }
}