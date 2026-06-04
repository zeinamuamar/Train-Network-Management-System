import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TrainPath {
   private Map<Station, List<Path>> Network;

    //======= 1 =======
    public TrainPath() {
        this.Network = new HashMap<>();
    }

    public void addStation(String name, String code) {
        Station newStation = new Station(name, code);
        //Ensure that the station paths are not deleted if called again by mistake
        Network.putIfAbsent(newStation, new ArrayList<>());
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
        try (FileWriter fileWriter = new FileWriter(filePath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

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
            System.out.println("Exported successfully" + filePath);

        } catch (IOException e) {
            System.err.println("Error" + e.getMessage());
        }
    }
    //======= 2.2 =======
    public void importNetwork(String filePath) {
        //Cleaning the current network to receive new data from the file
        this.Network.clear();

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            List<String> linesWithPaths = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                linesWithPaths.add(line);

                String[] parts = line.split("->");
                if (parts.length >= 1) {
                    String sourceName = parts[0].trim();
                    //Automatically generate a code from the first 3 letters
                    String generatedCode = sourceName.substring(0, Math.min(sourceName.length(), 3)).toUpperCase();
                    addStation(sourceName, generatedCode);
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
                                String destCode = destName.substring(0, Math.min(destName.length(), 3)).toUpperCase();
                                addStation(destName, destCode);
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
            System.err.println("Error while reading the file: " + e.getMessage());
        }
    }

    public Map<Station, List<Path>> getNetwork() {
        return Network;

    }

    //======= 3 =======
    public void renderNetworkToFile(String inputPath, String outputPath) {
        importNetwork(inputPath);

        if (Network.isEmpty()) {
            System.out.println("Warning: The grid is empty, there is nothing to draw");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(outputPath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println("=========================================================");
            printWriter.println("                    (Graph Render)                       ");
            printWriter.println("=========================================================");
            printWriter.println();

            for (Map.Entry<Station, List<Path>> entry : Network.entrySet()) {
                Station currentStation = entry.getKey();
                List<Path> paths = entry.getValue();

                printWriter.println(" ┌────────────────────────┐");
                printWriter.println(" │ Station: " + String.format("%-15s", currentStation.getName()) + "│");
                printWriter.println(" └────────────────────────┘");

                if (paths.isEmpty()) {
                        printWriter.println("       └── ⛔ no paths originate from this station");
                    } else {
                   
                    for (int i = 0; i < paths.size(); i++) {
                        Path path = paths.get(i);
                        
                        if (i == paths.size() - 1) {
                           
                            printWriter.println("       └─── 🛤️  ──(" + path.getdistance() + " km)──> [ " + path.getDestination().getName() + " ]");
                        } else {
                            
                            printWriter.println("       ├─── 🛤️  ──(" + path.getdistance() + " km)──> [ " + path.getDestination().getName() + " ]");
                        }
                    }
                }
                //A blank line between the stations
                printWriter.println(); 
            }

            printWriter.println("=========================================================");
            printWriter.println("   The structural diagram was generated successfully      ");
            printWriter.println("=========================================================");
            
            System.out.println("The network was read and rendered visually successfully inside the file: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error while rendering the file: " + e.getMessage());
        }
    }
    //======= 4 =======
    public void findShortestPath(String sourceName, String destName) {
        Station source = findStationByName(sourceName);
        Station destination = findStationByName(destName);

        if (source == null || destination == null) {
            System.out.println("Error: One of the relay stations does not exist in the network ❌");
            return;
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

            if (currentStation.equals(destination)) break;

            if (currentPair.distance > distances.get(currentStation)) continue;

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
            System.out.println("There is no path connecting between ❌" + sourceName + " and " + destName);
        } else {
            System.out.println("====== Result of calculating the shortest path ======");
            System.out.println("The shortest total distance: " + distances.get(destination) + "Km");
            
            List<String> pathList = new ArrayList<>();
            Station curr = destination;
            while (curr != null) {
                pathList.add(0, curr.getName());
                curr = parentMap.get(curr);
            }
            
            System.out.print("The shortest path: " + String.join(" ➔ ", pathList));
            System.out.println("\n==================================");
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


}
