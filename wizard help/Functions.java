import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Functions {

    // Global variables
    static Node[][] mapMatris;
    static int radius;
    static ArrayList<Objective> objectives = new ArrayList<>();
    static ArrayList<String> outputLog = new ArrayList<>();
    static Node startNode;

    /**
     * Parses the input files to initialize the map, travel times, and mission data.
     *
     * @param landFile File containing map data.
     * @param travelTimeFile File containing travel times between nodes.
     * @param missionFile File containing mission objectives.
     * @throws IOException If an error occurs during file reading.
     */
    static void parseInputs(String landFile, String travelTimeFile, String missionFile) throws IOException {
        BufferedReader landReader = new BufferedReader(new FileReader(landFile));
        BufferedReader travelReader = new BufferedReader(new FileReader(travelTimeFile));
        BufferedReader missionReader = new BufferedReader(new FileReader(missionFile));

        // Parse land file to initialize the map matrix
        String[] gridSize = landReader.readLine().split(" ");
        int gridWidth = Integer.parseInt(gridSize[0]);
        int gridHeight = Integer.parseInt(gridSize[1]);
        mapMatris = new Node[gridWidth][gridHeight];

        String line;
        while ((line = landReader.readLine()) != null) {
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int type = Integer.parseInt(parts[2]);
            Node node = new Node(x, y, type);
            mapMatris[x][y] = node;
        }

        // Parse travel times between nodes
        while ((line = travelReader.readLine()) != null) {
            String[] parts = line.split(" ");
            String[] nodes = parts[0].split(",");
            double travelTime = Double.parseDouble(parts[1]);

            // Parse coordinates for the nodes connected by the edge
            String[] fromCoords = nodes[0].split("-");
            int fromX = Integer.parseInt(fromCoords[0]);
            int fromY = Integer.parseInt(fromCoords[1]);
            Node from = mapMatris[fromX][fromY];

            String[] toCoords = nodes[1].split("-");
            int toX = Integer.parseInt(toCoords[0]);
            int toY = Integer.parseInt(toCoords[1]);
            Node to = mapMatris[toX][toY];

            // Add bidirectional edge with travel time
            from.neighbours.put(to, travelTime);
            to.neighbours.put(from, travelTime);
        }

        // Parse mission file for radius, start node, and objectives
        radius = Integer.parseInt(missionReader.readLine());

        String[] startCoords = missionReader.readLine().split(" ");
        int startX = Integer.parseInt(startCoords[0]);
        int startY = Integer.parseInt(startCoords[1]);
        startNode = mapMatris[startX][startY];

        while ((line = missionReader.readLine()) != null) {
            String[] parts = line.split(" ");
            int objX = Integer.parseInt(parts[0]);
            int objY = Integer.parseInt(parts[1]);

            ArrayList<Integer> helpOptions = new ArrayList<>();
            boolean offersHelp = parts.length > 2;

            if (offersHelp) {
                for (int i = 2; i < parts.length; i++) {
                    helpOptions.add(Integer.parseInt(parts[i]));
                }
            }

            objectives.add(new Objective(mapMatris[objX][objY], helpOptions, offersHelp));
        }
    }

    /**
     * Simulates the journey by navigating through objectives and adjusting the map dynamically.
     */
    static void simulate() {
        Node currentNode = startNode;

        for (int i = 0; i < objectives.size(); i++) {
            Objective objective = objectives.get(i);

            // Reveal nodes within the radius of the starting node
            revealNodes(currentNode, radius);

            // Calculate the initial path to the target
            ArrayList<Node> path = dijkstra(currentNode, objective.target);

            while (true) {
                boolean isPathBreak = false;

                // Traverse the path
                for (int j = 1; j < path.size(); j++) {
                    Node tempNode = path.get(j);
                    outputLog.add("Moving to " + tempNode.x + "-" + tempNode.y);

                    // Reveal nodes within the radius of the current node
                    revealNodes(tempNode, radius);

                    // Check if any newly revealed nodes break the current path
                    for (Node node : path) {
                        if (node.type >= 2 && node.revealed == 1) {
                            outputLog.add("Path is impassable!");
                            isPathBreak = true;
                            currentNode = tempNode;
                            break;
                        }
                    }
                    if (isPathBreak) break;
                }

                if (isPathBreak) {
                    // Recalculate the path from the last valid node to the target
                    path = dijkstra(currentNode, objective.target);
                } else {
                    // Path traversal is complete
                    break;
                }
            }

            // Log the completion of the objective
            outputLog.add("Objective " + (i + 1) + " reached!");

            // Handle wizard's help if offered
            if (objective.offersHelp) {
                int bestOption = handleWizardHelp(objective.helpOptions, objective.target,
                        (i + 1 < objectives.size()) ? objectives.get(i + 1).target : null);
                outputLog.add("Number " + bestOption + " is chosen!");
                makeChanges(bestOption);
            }

            // Update the current node for the next objective
            currentNode = objective.target;
        }
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path between two nodes.
     */
    // Dijkstra method using edge matrix and node table
    static ArrayList<Node> dijkstra(Node start, Node target) {
        int gridWidth = mapMatris.length;
        int gridHeight = mapMatris[0].length;

        // Initialize distances and visited status for all nodes
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Node node = mapMatris[i][j];
                if (node != null) {
                    node.distance = Double.MAX_VALUE;
                    node.parent = null;
                    node.visited = false;
                }
            }
        }
        start.distance = 0;
        CustomHeap pq = new CustomHeap();
        pq.insert(start);

        while (!pq.isEmpty()) {
            Node current = pq.extractMin();

            // Skip if already visited
            if (current.visited) continue;
            current.visited = true;

            int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            // Explore neighbors
            for (int[] direct : directions) {
                int neighborX = current.x + direct[0];
                int neighborY = current.y + direct[1];

                if (neighborX >= 0 && neighborY >= 0 && neighborX < gridWidth && neighborY < gridHeight) {
                    Node neighbor = mapMatris[neighborX][neighborY];

                    // Skip if the neighbor is impassable or visited
                    if (neighbor == null || neighbor.revealed == 1 || neighbor.visited) continue;

                    double travelTime = current.neighbours.get(neighbor);

                    if (travelTime > 0) { // Valid edge exists
                        double newDist = current.distance + travelTime;
                        if (newDist < neighbor.distance) {
                            neighbor.distance = newDist;
                            neighbor.parent = current;
                            pq.insert(neighbor); // Update priority queue
                        }
                    }
                }
            }
        }
        return reconstructPath(target); // Path not found
    }

    // Reconstruct path from destination to start

    /**
     * Reconstructs the path from target to start node.
     */
    static ArrayList<Node> reconstructPath(Node destination) {
        ArrayList<Node> path = new ArrayList<>();
        for (Node node = destination; node != null; node = node.parent) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Handles wizard's help by selecting the best option based on future path times.
     */
    static int handleWizardHelp(ArrayList<Integer> options, Node current, Node nextObjective) {
        int bestOption = -1;
        double minTotalTime = Double.MAX_VALUE;

        for (int option : options) {
            makePassable(option);
            double totalTime = calculatePathTime(current, nextObjective);

            if (totalTime < minTotalTime) {
                minTotalTime = totalTime;
                bestOption = option;
            }
            revertPassable(option);
        }
        return bestOption;
    }

    /**
     * Calculates the total path time between two nodes.
     */
    static double calculatePathTime(Node start, Node target) {
        ArrayList<Node> path = dijkstra(start, target);
        if (path.isEmpty()) return Double.MAX_VALUE;

        double totalTime = 0.0;
        for (int i = 1; i < path.size(); i++) {
            Node node1 = path.get(i - 1);
            Node node2 = path.get(i);
            totalTime += node1.neighbours.get(node2);
        }
        return totalTime;
    }

    /**
     * Temporarily makes nodes of a certain type passable.
     */
    static void makePassable(int type) {
        for (Node[] row : mapMatris) {
            for (Node node : row) {
                if (node != null && node.type == type) {
                    node.revealed = 0;
                }
            }
        }
    }

    /**
     * Reverts the temporary passable state of nodes of a certain type.
     */
    static void revertPassable(int type) {
        for (Node[] row : mapMatris) {
            for (Node node : row) {
                if (node != null && node.type == type && node.isRevealed) {
                    node.revealed = 1;
                }
            }
        }
    }

    /**
     * Applies permanent changes to the map for a specific node type.
     */
    static void makeChanges(int bestOption) {
        for (Node[] row : mapMatris) {
            for (Node node : row) {
                if (node != null && node.type == bestOption) {
                    node.type = 0;
                    node.revealed = 0;
                }
            }
        }
    }

    /**
     * Writes the output log to a file.
     */
    static void writeOutput(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String line : outputLog) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Output written successfully to: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reveals nodes within a given radius of the current node.
     */
    static void revealNodes(Node currentNode, int radius) {
        int gridWidth = mapMatris.length;
        int gridHeight = mapMatris[0].length;

        for (int i = currentNode.x - radius; i <= currentNode.x + radius; i++) {
            for (int j = currentNode.y - radius; j <= currentNode.y + radius; j++) {
                if (i >= 0 && j >= 0 && i < gridWidth && j < gridHeight) {
                    Node node = mapMatris[i][j];
                    if (node == null) continue;

                    int distance = (int) (Math.pow(currentNode.x - i, 2) + Math.pow(currentNode.y - j, 2));

                    if (distance <= radius * radius) {
                        if (node.type >= 2) {
                            node.isRevealed = true;
                            node.revealed = 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * Encodes two nodes as a string identifier for map edges.
     */
    static String encodeMap(Node node1, Node node2) {
        return node1.x + "-" + node1.y + "," + node2.x + "-" + node2.y;
    }
}
