package Backend;

public class Path {
    private Station destination;  
    private double distance;  

    public Path(Station destination, double distance) {
        setDestination(destination);
        setdistance(distance);
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

    public double getdistance() {
        return distance;
    }

    public void setdistance(double distance) {
        
        if (distance <= 0) {
            throw new IllegalArgumentException("The distance must be a strictly positive value and greater than zero");
        }
        this.distance = distance;
    }

    @Override
    public String toString() {
        return destination.getName() + " (" + distance + " km)";
    }
}
