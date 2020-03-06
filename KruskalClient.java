import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * Kevin Gu
 * March 5, 2020
 * KruskalClient.java: Kruskal's algorithm testing client
 */
public class KruskalClient {

    // text location
    private static final String TEXT_LOCATION = "src/txt/da.txt";

    // graph
    private static GraphE graph;

    // main
    public static void main(String[] args) {
        // instructions
        System.out.println("Kevin's KruskalClient.java");
        System.out.println("- Testing client for Kruskal's's algorithm to obtain a minimum spanning tree for da.txt");

        // load graph
        loadGraph();

        // get edges
        LinkedList<Edge> list = graph.kruskal();

        // print edges
        System.out.println("edges:");
        System.out.printf("%-10s%-25s\n", "source", "destination");
        for (Edge n : list)
            System.out.printf("%-10s%-25s\n", n.source, n.destination);
    }

    // calculates distance between two locations
    private static double calculateWeight(Location l1, Location l2) {
        return Math.sqrt(Math.pow(l2.x - l1.x, 2) + Math.pow(l2.y - l1.y, 2));
    }

    // loads graph
    private static void loadGraph() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(TEXT_LOCATION));
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");

            int vertices = Integer.parseInt(st.nextToken());
            int edges = Integer.parseInt(st.nextToken());
            graph = new GraphE(vertices, edges);

            for (int i = 0; i < vertices; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                graph.addVertex(Integer.parseInt(st.nextToken()), new Location(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
            }

            for (int i = 0; i < edges; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                int s = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                graph.addAdjacent(s, d, calculateWeight(graph.getVertex(s).location, graph.getVertex(d).location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
