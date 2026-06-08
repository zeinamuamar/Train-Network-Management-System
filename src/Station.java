import java.util.Objects;

public class Station {

    private String name;
    private String code;

    public Station(String name, String code) {
        this.name = name != null ? name.trim() : "";
        this.code = code != null ? code.trim().toUpperCase() : "";
        //setName(name);
        //setCode(code);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) 
        return true; 
        if (o == null || getClass() != o.getClass())
         return false; 
        
        Station station = (Station) o;
        return name.equalsIgnoreCase(station.name) || code.equalsIgnoreCase(station.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), code.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
