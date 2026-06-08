import java.awt.*;

public class Station_UI {

    int x, y;
    String name;
    boolean hovered;

    public Station_UI(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {

        int size = hovered ? 16 : 8;

        if (hovered) {
            g2.setColor(new Color(0, 180, 255, 120));
            g2.fillOval(x - size, y - size, size * 2, size * 2);
        }

        g2.setColor(hovered ? Color.CYAN : Color.WHITE);
        g2.fillOval(x - 4, y - 4, 8, 8);

        if (hovered) {
            g2.setColor(Color.WHITE);
            g2.drawString(name, x + 10, y - 10);
        }
    }

    public boolean contains(int mx, int my) {
        int size = hovered ? 16 : 8;
        int dx = mx - x;
        int dy = my - y;
        return dx * dx + dy * dy <= size * size;
    }
}