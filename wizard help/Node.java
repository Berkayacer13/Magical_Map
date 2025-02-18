import java.util.ArrayList;

public class Node implements Comparable<Node> {
    // Node coordinates
    int x, y;

    // Type of the node
    int type;

    // Indicates if the node has been visited in a traversal
    boolean visited = false;

    // Distance used for Dijkstra
    double distance = Double.MAX_VALUE;

    // Parent node in the shortest path (used to reconstruct the path)
    Node parent = null;

    // Revealed status: 1 for revealed, 0 for hidden
    int revealed;

    // Indicates if the node has been explicitly revealed during traversal
    boolean isRevealed = false;

    // Adjacency map for neighbors and their corresponding travel times
    CustomHashMap neighbours;

    // Constructor to initialize a Node object
    Node(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type; // Node type

        // Set the initial `revealed` value based on the type
        if (type == 1) {
            revealed = 1; // Nodes of type 1 are immediately revealed
        } else {
            revealed = 0; // Other nodes start as hidden
        }

        neighbours = new CustomHashMap();
    }

    // Compare nodes by their distance (used in priority queues for pathfinding)
    @Override
    public int compareTo(Node node1) {
        return Double.compare(this.distance, node1.distance);
    }
}
