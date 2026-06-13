import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class TrainController {
    //This class acts as a mediator between the UI and the backend logic,
    //allowing for better separation of concerns and easier maintenance.
    TrainPath backendGraph;

    //===============================================
    // 1. Constructor to initialize the backend graph
    public TrainController() {
        this.backendGraph = new TrainPath();
    }

    //to add a station from the UI, we validate the input and then call the backend method
    public boolean addStationFromUI(String name, String code) {
        //to ensure data integrity, we check that the station name and code are not null or empty before adding
        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            return false; 
        }
        //we also check if a station with the same name already 
        //exists in the backend graph to prevent duplicates
        if (backendGraph.findStationByName(name) != null) {
            // station with the same name already exists, we do not add it again
            return false;
        }
        backendGraph.addStation(name, code);
        return true;
    }
public boolean addStationFromUI(String name, String code, int x, int y) {

    if (name == null || name.trim().isEmpty()
            || code == null || code.trim().isEmpty()) {
        return false;
    }

    if (backendGraph.findStationByName(name) != null) {
        return false;
    }

    backendGraph.addStation(name, code, x, y);

    return true;
}
    //===============================================
    // 2. To add a path from the UI, we validate the input and then call the backend method
    public boolean addPathFromUI(String sourceName, String destName, double distance) {
        //to ensure data integrity, we check that the station names are not null or empty
        //and that the distance is a positive value before adding
        if (sourceName == null || sourceName.trim().isEmpty() || destName == null || destName.trim().isEmpty() || distance <= 0) {
            return false;
        }

        Station source = backendGraph.findStationByName(sourceName);
        Station dest = backendGraph.findStationByName(destName);
        
        if (source != null && dest != null) {
            backendGraph.addPath(sourceName, destName, distance);
          //  backendGraph.addPath(destName, sourceName, distance);
            return true;
        }
        return false; 
    }

    //===============================================
    // 3. A method to get the shortest path between two stations, which will be
    // called by the UI when the user requests it
    public String getShortestPathRoute(String from, String to) {
    if (from == null || to == null || from.trim().isEmpty() || to.trim().isEmpty()) {
        return "الرجاء تحديد محطة البداية والنهاية بشكل صحيح!";
    }
    
    //get the shortest path from the backend graph
    List<Station> path = backendGraph.findShortestPath(from, to);
    
    if (path == null || path.isEmpty()) {
        return "There is no path connecting between ❌" + from + " and " + to;
    }
    
    //format the path into a user-friendly string to display in the UI
    StringBuilder result = new StringBuilder("Shortest path:\n\n");
    for (int i = 0; i < path.size(); i++) {
        result.append(path.get(i).getName());
        if (i < path.size() - 1) {
            result.append(" ➔ ");
        }
    }
    return result.toString();
}
public List<Station> getShortestPath(String from, String to) {
    return backendGraph.findShortestPath(from, to);
}
    
//===============================================
    // 4. A method to check for cycles in the network, which will be called
    // by the UI when the user clicks the "Check Network" button
    public String checkNetworkCycles() {
        //we call the backend method to check for cycles and then return 
        // a user-friendly message based on the result
        boolean hasCycle = backendGraph.hasCycle(); 
        
        if (hasCycle) {
            return "⚠️ Network contains at least one cycle!";
        } else {
            return "✅ Network is acyclic, no cycles detected";
        }
    }
     public Map<Station, List<Path>> getNetwork() {
    return backendGraph.getNetwork();
     }
    //===============================================
    // 5. A method to get the station names for populating the combo boxes in the UI,
    // which will be called when the UI initializes
    public String[] getStationNamesForComboBox() {
        // we retrieve the list of stations from the backend graph and
        // extract their names into an array to return to the UI
        List<Station> stations = new ArrayList<>(backendGraph.getNetwork().keySet());
        String[] names = new String[stations.size()];
        
        for (int i = 0; i < stations.size(); i++) {
            names[i] = stations.get(i).getName(); 
        }
        return names;
    }

    //===============================================
    // 6. A method to import the train network data from a file,
    // which will be called by the UI when the user clicks the "Import Network" button
    public String importNetworkFromFile(String filePath) {
        try {
            backendGraph.importNetwork(filePath);
            return "✅ Network imported successfully from " + filePath;
        } catch (Exception e) {
            return "❌ Failed to import network: Error reading or parsing file data. " + e.getMessage();
        }
    }

    // ===============================================
    // 7. A method to get the stations sorted by connections for the UI
    public List<String> getStationsSortedByConnectionsForUI() {
        // we call the backend method to get the stations sorted by their number of connections,
        // and then format the result into a list of strings to display in the UI
        List<Station> sortedStations = backendGraph.getStationsSortedByConnections();
        List<String> formattedList = new ArrayList<>();
        
        for (Station s : sortedStations) {
            // we also include the number of connections for each station in the formatted string
            int connectionsCount = (backendGraph.getNetwork().get(s) != null) ? backendGraph.getNetwork().get(s).size() : 0;
            formattedList.add(s.getName() + " (" + s.getCode() + ") - Paths count: " + connectionsCount);
        }
        return formattedList;
    }
    
    // ===============================================
    // 8. A method to export the train network data to a file
    public String exportNetworkToFile(String filePath) {
        try {
            backendGraph.exportNetwork(filePath); 
            return "Network exported successfully to " + filePath;
        } catch (Exception e) {
            return "Failed to export network: " + e.getMessage();
        }
    }
}
