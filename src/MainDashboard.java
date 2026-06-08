import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    // 🧠 ONE shared backend controller (IMPORTANT FIX)
    private TrainController controller;

    // 🗺 map panel must be accessible for updates later
    private MapPanel mapPanel;

    public MainDashboard() {

        setTitle("Train Network Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🧠 create backend ONCE
        controller = new TrainController();

        // 🧱 build UI
        add(createMainPanel());

        setVisible(true);
    }

    private JPanel createMainPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // 🎛 left control panel
        mainPanel.add(createLeftPanel(), BorderLayout.WEST);

        // 🗺 right map panel (REAL ONE)
        mapPanel = new MapPanel(controller);
        mainPanel.add(mapPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createLeftPanel() {

        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(260, 600));
        left.setBackground(new Color(10, 10, 20));
        left.setLayout(null);

        JLabel title = new JLabel("control panel");
        title.setForeground(new Color(200, 200, 255));
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(40, 30, 200, 30);
        left.add(title);

        // 🎯 buttons
        JButton addStation = createButton("Add Station", 100);
        JButton addRoute = createButton("Add Route", 150);
        JButton shortestPath = createButton("Shortest Path", 200);
        JButton checkCycles = createButton("Check Cycles", 250);
        JButton importGraph = createButton("Import Graph", 300);
        JButton exportGraph = createButton("Export Graph", 350);

        // 🧪 TEMP TEST (REMOVE LATER, ONLY FOR DEBUG)
        addStation.addActionListener(e -> {
            controller.addStationFromUI("Damascus", "D1");
            controller.addStationFromUI("Homs", "H1");
            controller.addPathFromUI("Damascus", "Homs", 120);

            mapPanel.repaint(); // 🔥 refresh map
        });

        JLabel status = new JLabel("Status: Ready");
        status.setForeground(Color.LIGHT_GRAY);
        status.setBounds(50, 500, 200, 30);

        left.add(status);
        left.add(addStation);
        left.add(addRoute);
        left.add(shortestPath);
        left.add(checkCycles);
        left.add(importGraph);
        left.add(exportGraph);

        return left;
    }

    private JButton createButton(String text, int y) {

        JButton button = new JButton(text);
        button.setBounds(50, y, 160, 30);
        button.setBackground(new Color(30, 30, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        return button;
    }
}