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
    //temporary variables for station creation
    private List<Station> shortestPath = new ArrayList<>();
    private List<Station> cyclePath = new ArrayList<>();
    public MapPanel(TrainController controller) {
        this.controller = controller;

        syriaMap = loadImage("Assect/map.png");
        if (syriaMap == null) {
            System.out.println("Map image not found.");
        }

        setBackground(new Color(20, 20, 40));
    }
    public void setShortestPath(List<Station> path) {
        this.shortestPath = path;
        repaint();
    }
    public void setCyclePath(List<Station> path) {
        this.cyclePath = path;
        repaint();
    }
    private boolean isInShortestPath(Station a, Station b) {

    if (shortestPath == null || shortestPath.size() < 2) {
        return false;
    }

    for (int i = 0; i < shortestPath.size() - 1; i++) {

        Station s1 = shortestPath.get(i);
        Station s2 = shortestPath.get(i + 1);

        if ((s1.equals(a) && s2.equals(b)) ||
            (s1.equals(b) && s2.equals(a))) {
            return true;
        }
    }

    return false;
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

        if (syriaMap != null) {
            g2.drawImage(syriaMap, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // GET STATIONS FROM BACKEND (correct way)
        Map<Station, List<Path>> network = controller.getNetwork();
        System.out.println("Stations in network: " + network.size());
        List<Station> stations = new ArrayList<>(network.keySet());

        // draw stations
       drawRoutes(g2, network);
       drawStations(g2, stations);
    }
    // ---------------- DRAW STATIONS ----------------
    private void drawStations(Graphics2D g2, List<Station> stations) {
        for (Station s : stations) {

            //temporary layout (Stage 2 only)
           int x = s.getX();
           int y = s.getY();

            // blue glow effect
            g2.setColor(new Color(0, 180, 255, 120));
            g2.fillOval(x - 12, y - 12, 24, 24);

            // white core dot
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 5, y - 5, 10, 10);

            // a label for the station name
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(s.getName(), x + 10, y - 10);

        }}
        //draw routes (Stage 3)
        private void drawRoutes(Graphics2D g2, Map<Station, List<Path>> network) {

            g2.setColor(new Color(0, 180, 255, 120));
            g2.setStroke(new BasicStroke(2));

            for (Station source : network.keySet()) {

                List<Path> paths = network.get(source);

                for (Path p : paths) {

                    Station dest = p.getDestination();
                      if (isInShortestPath(source, dest)) {
                        g2.setColor(new Color(255, 215, 0)); // GOLD
                    } else {
                        g2.setColor(new Color(0, 180, 255, 120)); // NORMAL
                    }
                    int x1 = source.getX();
                    int y1 = source.getY();

                    int x2 = dest.getX();
                    int y2 = dest.getY();

            // draw line
                    g2.drawLine(x1, y1, x2, y2);

            // draw distance in middle
                     int midX = (x1 + x2) / 2;
                    int midY = (y1 + y2) / 2;

                    g2.drawString(
                        String.valueOf(p.getdistance()),
                        midX,
                        midY
                    );
                }
            
        }
        
    }
}