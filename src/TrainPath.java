import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.awt.Point;
public class TrainPath {
   private Map<Station, List<Path>> Network;

    //======= 1 =======
    public TrainPath() {
        this.Network = new HashMap<>();
    }
    // this is for the bbackend
    public void addStation(String name, String code) {
        Station newStation = new Station(name, code);
        //Ensure that the station paths are not deleted if called again by mistake
        Network.putIfAbsent(newStation, new ArrayList<>());
    }
    //values are assigned when a station is created...x,y can be set later using the setters of the station object
    public void addStation(String name, String code, int x, int y) {

    Station station = new Station(name, code);

    station.setX(x);
    station.setY(y);

    this.Network.putIfAbsent(station, new ArrayList<>());
}  
    private Point getProvinceLocation(String province) {

    switch (province) {

        case "Damascus":
            return new Point(230, 420);

        case "As-Suwayda":
            return new Point(210, 500);

        case "Daraa":
            return new Point(160, 470);

        case "Hasakah":
            return new Point(470, 100);

        case "Aleppo":
            return new Point(280, 180);

        case "Idlib":
            return new Point(210, 210);

        case "Homs":
            return new Point(210, 320);

        case "Hama":
            return new Point(230, 260);

        case "Latakia":
            return new Point(140, 250);

        case "Tartous":
            return new Point(140, 320);

        case "Quneitra":
            return new Point(170, 420);

        default:
            return new Point(50, 50);
    }
}
    public void addPath(String sourceName, String destName, double distance) {
        Station source = findStationByName(sourceName);
        Station destination = findStationByName(destName);

        if (source == null) {
            throw new IllegalArgumentException("Error: Station " + sourceName + " does not exist in the network");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Error: Station " + destName + " does not exist in the network");
        }

        Path newPath = new Path(destination, distance);
        List<Path> paths = Network.get(source);
        if (!paths.contains(newPath)) {
            paths.add(newPath);
        }
    }

    public Station findStationByName(String name) {
        if (name == null) 
            return null;

        String cleanName = name.trim();
        for (Station station : Network.keySet()) {
            if (station.getName().equalsIgnoreCase(cleanName)) {
                return station;
            }
        }
        return null; 
    }

    //======= 2.1 =======
    public void exportNetwork(String filePath) {
        try (PrintWriter printWriter = new PrintWriter(filePath)) {

            for (Map.Entry<Station, List<Path>> entry : Network.entrySet()) {
                Station currentStation = entry.getKey();
                List<Path> paths = entry.getValue();

                printWriter.print(currentStation.getName() + " -> ");

                for (int i = 0; i < paths.size(); i++) {
                    Path path = paths.get(i);
                    printWriter.print(path.getDestination().getName() + "(" + path.getdistance() + ")");

                    
                    if (i < paths.size() - 1) {
                        printWriter.print(", ");
                    }
                }

                //New line for the new station
                printWriter.println(); 
            }

        } catch (IOException e) {
            System.err.println("Error while exporting the network: " + e.getMessage());
        }
    }

    //======= 2.2 =======
    public void importNetwork(String filePath) {
        //Cleaning the current network to receive new data from the file
        this.Network.clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            String line;
            List<String> linesWithPaths = new ArrayList<>();
            
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                linesWithPaths.add(line);

                String[] parts = line.split("->");
                if (parts.length >= 1) {
                    String sourceName = parts[0].trim();
                    //Automatically generate a code from the first 3 letters
                   String generatedCode =
        sourceName.substring(0,
        Math.min(sourceName.length(), 3))
        .toUpperCase();

Point p = getProvinceLocation(sourceName);

addStation(
        sourceName,
        generatedCode,
        p.x,
        p.y
);
                }
            }

            for (String currentLine : linesWithPaths) {
                String[] parts = currentLine.split("->");
                if (parts.length < 2) continue;

                String sourceName = parts[0].trim();
                String destinationsPart = parts[1].trim();

                String[] destinations = destinationsPart.split(",");
                for (String dest : destinations) {
                    dest = dest.trim();
                    if (dest.isEmpty()) continue;

                    int openBracket = dest.indexOf('(');
                    int closeBracket = dest.indexOf(')');

                    if (openBracket != -1 && closeBracket != -1) {
                        String destName = dest.substring(0, openBracket).trim();
                        String weightStr = dest.substring(openBracket + 1, closeBracket).trim();

                        try {
                            double distance = Double.parseDouble(weightStr);
                            if (findStationByName(destName) == null) {
                               String destCode =
        destName.substring(0,
        Math.min(destName.length(), 3))
        .toUpperCase();

Point p = getProvinceLocation(destName);

addStation(
        destName,
        destCode,
        p.x,
        p.y
);
                            }

                            addPath(sourceName, destName, distance);

                        } catch (NumberFormatException nfe) {
                            System.err.println("Warning: Invalid weight skipped: " + weightStr);
                        }
                    }
                }
            }
            System.out.println("Network imported successfully from: " + filePath);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading the file: " + e.getMessage());
        }
    }

    public Map<Station, List<Path>> getNetwork() {
        return Network;

    }

    //======= 3 =======
   public void renderNetworkToFile(String inputPath, String outputPath) {
    importNetwork(inputPath);

    if (Network.isEmpty()) {
        throw new IllegalArgumentException("Warning: The grid is empty, there is nothing to draw");
    }

    try (PrintWriter printWriter = new PrintWriter(
            new OutputStreamWriter(
                    new FileOutputStream(outputPath),
                    java.nio.charset.StandardCharsets.UTF_8
            ))) {

        printWriter.println("=========================================================");
        printWriter.println("                    (Graph Render)                       ");
        printWriter.println("=========================================================");
        printWriter.println();

        for (Map.Entry<Station, List<Path>> entry : Network.entrySet()) {
            Station currentStation = entry.getKey();
            List<Path> paths = entry.getValue();

            printWriter.println(" Station: " + currentStation.getName());

            if (paths.isEmpty()) {
                printWriter.println("   - no paths originate from this station");
            } else {

                for (int i = 0; i < paths.size(); i++) {
                    Path path = paths.get(i);

                    printWriter.println("   -> " +
                            path.getDestination().getName() +
                            " (" + path.getdistance() + " km)");
                }
            }

            printWriter.println();
        }

        printWriter.println("=========================================================");
        printWriter.println("   The structural diagram was generated successfully      ");
        printWriter.println("=========================================================");

    } catch (IOException e) {
        System.err.println("Error while writing the rendered network: " + e.getMessage());
    }
}
    //======= 4 =======
    public List<Station> findShortestPath(String sourceName, String destName) {
        Station source = findStationByName(sourceName);
        Station destination = findStationByName(destName);

        if (source == null || destination == null) {
            return null;
        }

        Map<Station, Double> distances = new HashMap<>();
        Map<Station, Station> parentMap = new HashMap<>();
        
        PriorityQueue<StationDistancePair> pq = new PriorityQueue<>(
            (a, b) -> Double.compare(a.distance, b.distance)
        );

        for (Station s : Network.keySet()) {
            distances.put(s, Double.MAX_VALUE);
        }

        distances.put(source, 0.0);
        pq.add(new StationDistancePair(source, 0.0));

        while (!pq.isEmpty()) {
            StationDistancePair currentPair = pq.poll();
            Station currentStation = currentPair.station;

            if (currentStation.equals(destination)) 
                break;

            if (currentPair.distance > distances.get(currentStation))
                continue;

            List<Path> paths = Network.get(currentStation);
            if (paths != null) {
                for (Path path : paths) {
                    Station neighbor = path.getDestination();
                    double newDist = distances.get(currentStation) + path.getdistance();

                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        parentMap.put(neighbor, currentStation);
                        pq.add(new StationDistancePair(neighbor, newDist));
                    }
                }
            }
        }

      
        if (distances.get(destination) == Double.MAX_VALUE) {
            return null;
            }
         else {
            List<Station> pathList = new ArrayList<>();
            Station curr = destination;

            while (curr != null) {
                pathList.add(0, curr);
                curr = parentMap.get(curr);
            }
            return pathList;
        }
    }

    private static class StationDistancePair {
        Station station;
        double distance;

        public StationDistancePair(Station station, double distance) {
            this.station = station;
            this.distance = distance;
        }
    }

    //======= 5 =======
    public boolean hasCycle() {
        //We have three cases: (0) not visited, (1) visiting, (2) visited
        Map<Station, Integer> visitingState = new HashMap<>();
        //Made all station 0
        for (Station station : Network.keySet()) { 
            visitingState.put(station, 0);
        }

        for (Station station : Network.keySet()) { 
            if (visitingState.get(station) == 0) {
                if (dfsCheckCycle(station, visitingState)) {
                    return true;
                }
            }
        }

        return false;
    }

    //A helper method to perform DFS and check for cycles
    private boolean dfsCheckCycle(Station currentStation, Map<Station, Integer> visitingState) {
        //Now we put te value of this station as (1) because we are visiting it
        visitingState.put(currentStation, 1);
        //Get the list of paths goes from this station
        List<Path> paths = Network.get(currentStation);
        
        if (paths != null) {
            for (Path path : paths) {
                Station neighbor = path.getDestination();
                //If the neighbor is currently being visited, it means we have a cycle
                if (visitingState.get(neighbor) == 1) {
                    return true;
                }
                //If the neighbor is not visited, we continue the DFS
                if (visitingState.get(neighbor) == 0) {
                    if (dfsCheckCycle(neighbor, visitingState)) {
                        return true;
                    }
                }
                
            }
        }
        //After we finish visiting all neighbors, we mark this station as fully visited (2)
        visitingState.put(currentStation, 2);
        return false;
    }

    //======= 6 =======
    public List<Station> getStationsSortedByConnections() {
        //First, we create a list of stations from the keys of the network map
        List<Station> sortedStations = new ArrayList<>(Network.keySet());

        //Then, we sort this list based on the number of connections (paths) each station has
        Collections.sort(sortedStations, new Comparator<Station>() {
            @Override
            public int compare(Station s1, Station s2) {
                //get the number of connections for station 1 (size of its paths list)
                int connectionsCount1 = (Network.get(s1) != null) ? Network.get(s1).size() : 0;
                
                //get the number of connections for station 2 (size of its paths list)
                int connectionsCount2 = (Network.get(s2) != null) ? Network.get(s2).size() : 0;
                //We want to sort in descending)(تنازلي) order, so we compare station 2 with station 1
                return Integer.compare(connectionsCount2, connectionsCount1);
                
                //if we wanted ascending(تصاعدي) order: Integer.compare(connectionsCount1, connectionsCount2)
            }
        });

        //return the sorted list of stations
        return sortedStations;
    }

}
