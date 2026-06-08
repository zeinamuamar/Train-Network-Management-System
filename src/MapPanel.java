import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

    private Image syriaMap;
    public MapPanel() {
        ImageIcon icon = new ImageIcon("Assect/map.png");
        if(icon.getIconWidth() < 0){
            System.out.println("Map not found!");
        }else{
            syriaMap = icon.getImage();
        }
        setBackground(new Color(20,20,40));
    }
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if(syriaMap != null){

            g.drawImage(
                syriaMap,
                0,
                0,
                getWidth(),
                getHeight(),
                this
            );

        }else{

            g.setColor(Color.WHITE);
            g.drawString("Map could not be loaded",200,200);

        }
    }
}