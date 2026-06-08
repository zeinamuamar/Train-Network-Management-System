public class Main {
    public static void main(String[] args) {
        new SplashScreen();
       TrainController controller = new TrainController();
        controller.addStationFromUI("Damascus", "D1");
        controller.addStationFromUI("Homs", "H1");
        controller.addStationFromUI("Aleppo", "A1");
        controller.addPathFromUI("Damascus", "Homs", 120);
        controller.addPathFromUI("Homs", "Aleppo", 180);
        MapPanel mapPanel = new MapPanel(null);
        mapPanel.repaint();
    }
}
