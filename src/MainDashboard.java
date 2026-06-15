import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
public class MainDashboard extends JFrame {

    // ONE shared backend controller (IMPORTANT FIX)
    private TrainController controller;

    // map panel must be accessible for updates later
    private MapPanel mapPanel;

    public MainDashboard() {
        setTitle("Train Network Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create backend ONCE
        controller = new TrainController();
        //initializeGovernorateStations();

        // build UI
        add(createMainPanel());

        setVisible(true);
        System.out.println("MainDashboard visible");
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                System.out.println("MainDashboard windowOpened");
            }
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("MainDashboard windowClosing");
            }
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                System.out.println("MainDashboard windowClosed");
            }
        });
        setAlwaysOnTop(true);
        setAlwaysOnTop(false);
        toFront();
        requestFocus();
    }

    private JPanel createMainPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        mainPanel.add(createLeftPanel(), BorderLayout.WEST);

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

        // buttons
        JButton addStation = createButton("Add Station", 100);
        JButton addRoute = createButton("Add Route", 150);
        JButton shortestPath = createButton("Shortest Path", 200);
        JButton checkCycles = createButton("Check Cycles", 250);
        JButton importGraph = createButton("Import Graph", 300);
        JButton exportGraph = createButton("Export Graph", 350);
        JButton sortStations = createButton("Sort Stations", 400);
     importGraph.addActionListener(e -> {

    JFileChooser chooser = new JFileChooser();

    int result = chooser.showOpenDialog(MainDashboard.this);

    if (result == JFileChooser.APPROVE_OPTION) {

        String path =
                chooser.getSelectedFile()
                       .getAbsolutePath();

        String message =
                controller.importNetworkFromFile(path);

        JOptionPane.showMessageDialog(
                this,
                message
        );

        mapPanel.repaint();
    }
});
        exportGraph.addActionListener(e -> {

    JFileChooser chooser = new JFileChooser();

    int result = chooser.showSaveDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {

        String path =
                chooser.getSelectedFile()
                       .getAbsolutePath();

        String message =
                controller.exportNetworkToFile(path);

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }
});
        checkCycles.addActionListener(e -> {

    String result = controller.checkNetworkCycles();
    
    JOptionPane.showMessageDialog(
            this,
            result
    );
});
    sortStations.addActionListener(e -> {

    List<String> stations =
            controller.getStationsSortedByConnectionsForUI();

    StringBuilder result =
            new StringBuilder();

    for (String station : stations) {

        result.append(station)
              .append("\n");
    }

    JOptionPane.showMessageDialog(
            this,
            result.toString(),
            "Stations Sorted By Connections",
            JOptionPane.INFORMATION_MESSAGE
    );
});
        addStation.addActionListener(e -> {
          
            
            String[] stations = {
                    "Damascus",
                    "Daraa",
                    "Quneitra",
                    "As-Suwayda",
                    "Homs",
                    "Hama",
                    "Aleppo",
                    "Idlib",
                    "Latakia",
                    "Hasakah"
            };

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Choose a governorate station to add:",
                    "Add Station",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    stations,
                    stations[0]
            );
            

            if (selected == null) {
                return;
            }

            addPredefinedStation(selected);
            
            mapPanel.repaint();
        });
       addRoute.addActionListener(e -> {
//automatically gets all stations currently in the graph.
    String[] stationNames =
            controller.getNetwork()
                    .keySet()
                    .stream()
                    .map(Station::getName)
                    .toArray(String[]::new);

    if (stationNames.length < 2) {
        JOptionPane.showMessageDialog(
                this,
                "Add at least 2 stations first."
        );
        return;
    }

    JComboBox<String> sourceBox =
            new JComboBox<>(stationNames);

    JComboBox<String> destBox =
            new JComboBox<>(stationNames);

    JPanel panel =
            new JPanel(new GridLayout(0, 1));

    panel.add(new JLabel("Source:"));
    panel.add(sourceBox);

    panel.add(new JLabel("Destination:"));
    panel.add(destBox);

    int result =
            JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Add Route",
                    JOptionPane.OK_CANCEL_OPTION
            );

    if (result == JOptionPane.OK_OPTION) {

        String source =
                (String) sourceBox.getSelectedItem();

        String destination =
                (String) destBox.getSelectedItem();

        if (source.equals(destination)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Choose two different stations."
            );
            return;
        }

        boolean success =
                controller.addPathFromUI(
                        source,
                        destination,
                        1
                );

        if (success) {

            System.out.println(
                    "Route added: "
                    + source
                    + " -> "
                    + destination
            );

            mapPanel.repaint();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add route."
            );
        }
    }
});
shortestPath.addActionListener(e -> {

    String[] stations =
            controller.getNetwork()
                    .keySet()
                    .stream()
                    .map(Station::getName)
                    .toArray(String[]::new);

    JComboBox<String> fromBox = new JComboBox<>(stations);
    JComboBox<String> toBox = new JComboBox<>(stations);

    JPanel panel = new JPanel(new GridLayout(2, 2));
    panel.add(new JLabel("From:"));
    panel.add(fromBox);
    panel.add(new JLabel("To:"));
    panel.add(toBox);

    int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Shortest Path",
            JOptionPane.OK_CANCEL_OPTION
    );

    if (result == JOptionPane.OK_OPTION) {

        String from = (String) fromBox.getSelectedItem();
        String to = (String) toBox.getSelectedItem();

        // 1. get path from backend
        List<Station> path = controller.getShortestPath(from, to);

        // 2. update map visually
        mapPanel.setShortestPath(path);

        // 3. also show text result
        String text =
                controller.getShortestPathRoute(from, to);

        JOptionPane.showMessageDialog(this, text);
    }
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
        left.add(sortStations);
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

    private Point getProvinceLocation(String province) {
        switch (province) {
            case "Damascus":
                return new Point(230, 420);
            case "As-Suwayda":
                return new Point(210, 500);
            case "Daraa":
                return new Point(160, 470);
            case "As-Sua":
                return new Point(530, 520);
            case "Hasakah":
                return new Point(470, 100);
            case "Deir ezzor":
                return new Point(420, 245);
            case "Al-Hasakah":
                return new Point(520, 120);
            case "Aleppo":
                return new Point(280, 180);
            case "Idlib":
                return new Point(210,210);
           
         case "Homs":
                return new Point(210, 320);
            default:
                return null;
        }
    }

    private String getStationCode(String stationName) {
        switch (stationName) {
            case "Damascus":
                return "DAM";
            case "Rif Dimashq":
                return "RIF";
            case "Aleppo":
                return "ALE";
            case "Homs":
                return "HOM";
            case "Hama":
                return "HAM";
            case "Latakia":
                return "LAT";
            case "Tartous":
                return "TAR";
            case "Idlib":
                return "IDL";
            case "Deir Ezzor":
                return "DEI";
            case "Raqqa":
                return "RAQ";
            case "Hasakah":
                return "HAS";
            case "Daraa":
                return "DAR";
            case "As-Suwayda":
                return "ASU";
            case "Quneitra":
                return "QUN";
            default:
                return stationName.substring(0, Math.min(3, stationName.length())).toUpperCase();
        }
    }

    private void addPredefinedStation(String stationName) {
        Point location = getProvinceLocation(stationName);
        if (location == null) {
            return;
        }

        controller.addStationFromUI(
                stationName,
                getStationCode(stationName),
                location.x,
                location.y
        );
    }

    private void initializeGovernorateStations() {
        String[] governorates = {
                "Damascus",
                "Daraa",
                "Quneitra",
                "As-Suwayda",
                "Homs",
                "Hama",
                "Aleppo",
                "Idlib",
                "Latakia",
                "Tartous"
        };
        for (String governorate : governorates) {
            addPredefinedStation(governorate);
        }
    }

}
