import edu.princeton.cs.algs4.StdIn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * Kevin Gu
 * March 5, 2020
 * DijkstraClient.java: Dijkstra's algorithm testing client
 */
public class DijkstraClient {

    // text location
    private static final String TEXT_LOCATION = "src/txt/usa.txt";

    // graph
    private static GraphV graph;

    // main
    public static void main(String[] args) {
        // instructions
        System.out.println("Kevin's DijkstraClient.java");
        System.out.println("- Testing client for Dijkstra's algorithm to find the shortest path between two vertices for usa.txt");

        // load graph
        loadGraph();

        // inputs
        System.out.println("\nEnter source vertex between 0 and 87574:");
        int source = StdIn.readInt();
        System.out.println("Enter source vertex between 0 and 87574:");
        int destination = StdIn.readInt();

        // if both vertices are valid
        if (isVertexValid(source) && isVertexValid(destination)) {

            // get solution path
            LinkedList<Node> solution = graph.dijkstra(source, destination);

            // print path
            System.out.println("\npath:");
            System.out.printf("%-10s%-25s\n", "vertex", "distance");
            for (Node n : solution)
                System.out.printf("%-10s%-25s\n", n.index, n.weight);

            // if vertices not valid
        } else {
            System.out.println("\nPlease enter a valid index.");
        }
    }

    // returns whether index is a valid vertex
    private static boolean isVertexValid(int i) {
        return i >= 0 && i < graph.vertices();
    }

    // loads graph
    private static void loadGraph() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(TEXT_LOCATION));
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");

            int vertices = Integer.parseInt(st.nextToken());
            int edges = Integer.parseInt(st.nextToken());
            graph = new GraphV(vertices);

            for (int i = 0; i < vertices; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                graph.addVertex(Integer.parseInt(st.nextToken()), new Location(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
            }

            for (int i = 0; i < edges; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                graph.addAdjacent(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
