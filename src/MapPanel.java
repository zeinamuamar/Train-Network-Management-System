import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class MapPanel extends JPanel {

    private Image syriaMap;
    private TrainController controller;

    public MapPanel(TrainController controller) {
        this.controller = controller;

        syriaMap = loadImage("Assect/map.png");
        if (syriaMap == null) {
            System.out.println("Map image not found.");
        }

        setBackground(new Color(20, 20, 40));
    }

    // ---------------- IMAGE LOADER ----------------
    private Image loadImage(String... relativePaths) {
        String[] prefixes = {
                "",
                "src/",
                "../",
                "../Train-Network-Management-System/",
                "Train-Network-Management-System/"
        };

        for (String path : relativePaths) {
            for (String prefix : prefixes) {
                File file = new File(prefix + path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    if (icon.getIconWidth() >= 0) {
                        return icon.getImage();
                    }
                }
            }
        }
        return null;
    }

    // ---------------- PAINT ----------------
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
    // 🌍 draw map
    if (syriaMap != null) {
        g2.drawImage(syriaMap, 0, 0, getWidth(), getHeight(), this);
    }

    // 🚉 GET STATIONS FROM BACKEND (correct way)
    Map<Station, List<Path>> network = controller.getNetwork();
    System.out.println("Stations in network: " + network.size());
    List<Station> stations = new ArrayList<>(network.keySet());

    // 🎯 draw stations
    drawStations(g2, stations);
}
    // ---------------- DRAW STATIONS ----------------
    private void drawStations(Graphics2D g2, List<Station> stations) {

        int index = 0;

        for (Station s : stations) {

            // 📍 temporary layout (Stage 2 only)
            int x = 120 + (index * 140);
            int y = 120 + (index % 4) * 120;

            // 🔵 glow effect
            g2.setColor(new Color(0, 180, 255, 120));
            g2.fillOval(x - 12, y - 12, 24, 24);

            // ⚪ core dot
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 5, y - 5, 10, 10);

            // 🏷 label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(s.getName(), x + 10, y - 10);

            index++;
        }
    }
}