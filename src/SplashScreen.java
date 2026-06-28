import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        setTitle("Train Network Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        add(new SplashPanel());
        setVisible(true);
        setAlwaysOnTop(true);
        setAlwaysOnTop(false);
        toFront();
        requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }

    class SplashPanel extends JPanel {
       private boolean typingFinished = false;
       private int progress = 0;
       private JProgressBar progressBar;
        private Image background;
        private String fullTitle = "TRAIN NETWORK";
        private String currentTitle = "";
        private String fullSubtitle = "MANAGEMENT SYSTEM";
        private String currentSubtitle = "";
        private int currentIndex = 0;
        
        public SplashPanel() {
            background = loadImage("Assect/train_background.jpeg");
            if (background == null) {
                System.out.println("Splash background not found.");
            }

            javax.swing.Timer typingTimer = new javax.swing.Timer(120, e -> {
                if (currentIndex< fullTitle.length()) {
                    currentTitle += fullTitle.charAt(currentIndex);
                } else if (currentIndex < fullTitle.length() + fullSubtitle.length()) {
                    currentSubtitle += fullSubtitle.charAt(currentIndex - fullTitle.length());
                }else {
                ((javax.swing.Timer) e.getSource()).stop();
                    startLoading();}

                currentIndex++;
                repaint();
            });
            typingTimer.start();
            setLayout(null);
            progressBar=new JProgressBar(0,100);
            progressBar.setBounds(300,400,400,30);
                progressBar.setStringPainted(true);
                progressBar.setVisible(false);
                setLayout(null);
                add(progressBar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            g.setColor(new Color(0, 0, 0, 210));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString(currentTitle, 180, 220);
            g.setColor(new Color(0, 150, 255));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(currentSubtitle, 180, 280);
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Manage stations, routes and railway connections.", 180, 340);
       
        }       
        private void startLoading(){
                progressBar.setVisible(true);
                javax.swing.Timer LoadingTimer =new javax.swing.Timer(50,e->{
                        progress+=2;
                        progressBar.setValue(progress);
                        if(progress>=100){
                        ((javax.swing.Timer)e.getSource()).stop();
                        System.out.println("Splash finished, opening MainDashboard...");
                        //open next window
                        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
                        if (window != null) {
                            window.dispose();
                        }
                        new MainDashboard();
                        System.out.println("MainDashboard created.");
                        }
        });
                LoadingTimer.start();
        }

        private Image loadImage(String relativePath) {
            String[] candidates = {
                relativePath,
                "src/" + relativePath,
                "../" + relativePath,
                "../Train-Network-Management-System/" + relativePath,
                "Train-Network-Management-System/" + relativePath
            };
            for (String candidate : candidates) {
                File file = new File(candidate);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    if (icon.getIconWidth() >= 0) {
                        System.out.println("Loaded splash image from: " + file.getAbsolutePath());
                        return icon.getImage();
                    }
                }
            }
            return null;
        }
}
}
