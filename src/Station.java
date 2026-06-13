import java.util.Objects;

public class Station {

    private String name;
    private String code;
//for the map visualization, we can store the x and y coordinates of the station on the map.
    private int x;
    private int y;

    public Station(String name, String code) {
        this.name = name != null ? name.trim() : "";
        this.code = code != null ? code.trim().toUpperCase() : "";

        // default position
        this.x = 0;
        this.y = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.trim().toUpperCase() : "";
    }
//map visualization position getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Station station = (Station) o;

        return name.equalsIgnoreCase(station.name)
                || code.equalsIgnoreCase(station.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name.toLowerCase(),
                code.toLowerCase()
        );
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}