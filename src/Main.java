import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

public class Main {
    public static void main(String[] args) {
        System.out.println("Headless: " + GraphicsEnvironment.isHeadless());
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}
