import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Train Network Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);//Places the window in the center of the screen.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); 
        add(createMainPanel());
    // JFRAME->MAINJPANEL->LEFTJPANEL->BUTTONS. ->RIGHTJPANEL->TABLE
        setVisible(true);
    }   
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(createLeftPanel(), BorderLayout.WEST);//control panel
        mainPanel.add(new MapPanel(), BorderLayout.CENTER);//display panel
        return mainPanel;
    }
    
    private JPanel createLeftPanel(){
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(260,600));
        left.setBackground(new Color(10,10,20));//dark blue.
        left.setLayout(null);
        JLabel title=new JLabel("control panel");
        title.setForeground(new Color(200,200,255));
        title.setFont(new Font("Arial", Font.BOLD, 18));
        left.add(title);
        title.setBounds(40,30,200,30);

        JButton addStation = createButton("Add Station",100);
        JButton addRoute = createButton("Add Route",150);
        JButton shortestPath = createButton("Shortest Path",200);
        JButton checkCycles = createButton("Check Cycles",250);
        JButton importGraph = createButton("Import Graph",300);
        JButton exportGraph = createButton("Export Graph",350);
        JLabel status =new JLabel("Status:Ready");
        status.setForeground(Color.LIGHT_GRAY);
        status.setBounds(50,500,200,30);
        left.add(status);
        left.add(addStation);
        left.add(addRoute);
        left.add(shortestPath);
        left.add(checkCycles);
        left.add(importGraph);
        left.add(exportGraph);
        return left;
    }
    private JButton createButton(String text,int y) {
        JButton button = new JButton(text);
        button.setBounds(50,y,160,30);
        button.setBackground(new Color(30,30,60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
   private JPanel createRightPanel() {
    JPanel right = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Network Map",180,250);
        }
    };
    right.setBackground(new Color(20,20,40));


    return right;
}
}