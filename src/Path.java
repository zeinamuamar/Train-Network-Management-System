package Backend;

public class Path {
    private Station destination;  
    private double distance;  

    public Path(Station destination, double distance) {
        setDestination(destination);
        setWeight(distance);
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination station cannot be empty");
        }
        this.destination = destination;
    }

    public double getWeight() {
        return distance;
    }

    public void setWeight(double weight) {
        
        if (weight <= 0) {
            throw new IllegalArgumentException("The distance (weight) must be a strictly
            positive value and greater than zero");
        }
        this.distance = weight;
    }
}
