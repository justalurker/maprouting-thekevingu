/*
 * Kevin Gu
 * March 5, 2020
 * Graph2.java: Graph class implemented with edges for Kruskal's algorithm
 */
public class GraphE {

    // size and array of vertices and edges
    private int size;
    private Vertex[] vertices;
    private Edge[] edges;

    // constructor
    GraphE(int size, int numEdges) {
        this.size = size;
        vertices = new Vertex[size];
        edges = new Edge[numEdges];
    }

    // returns number of vertices
    int vertices() {
        return size;
    }

    // adds a vertex at specified index and location
    void addVertex(int index, Location location) {
        vertices[index] = new Vertex(index, location);
    }

    // gets vertex at index
    Vertex getVertex(int index) {
        return vertices[index];
    }

    // adds adjacent vertex to adjacent list for both source and destination vertex
    // adds to edge array
    private int edgeUtility = 0;

    void addAdjacent(int source, int destination, double weight) {
        vertices[source].adjacents.add(destination);
        vertices[destination].adjacents.add(source);
        edges[edgeUtility++] = new Edge(weight, source, destination);
    }

    // returns number of edges
    Edge[] edges() {
        return edges;
    }

    // returns list of edges to form minimum spanning tree using kruskal's algorithm
    LinkedList<Edge> kruskal() {
        // construct minimum priority queue
        MinPQ<Edge> pq = new MinPQ<>();

        // insert all edges
        for (Edge edge : this.edges())
            pq.insert(edge);

        // create return list
        LinkedList<Edge> list = new LinkedList<>();

        // use unionfind
        UnionFind uf = new UnionFind(this.vertices());

        // while priorit queue is not empty and list size is less than vertices - 1
        while (!pq.isEmpty() && list.size() < this.vertices() - 1) {

            // delete lowest weight edge
            Edge edge = pq.delMin();
            int s = edge.source;
            int d = edge.destination;

            // if it is not connected (not a cycle)
            if (!uf.connected(s, d)) {
                // union/connect edges
                uf.union(s, d);
                // add edge to list
                list.add(edge);
            }
        }
        // return list
        return list;
    }

    // toString() method
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += i + vertices[i].toString() + "\n";
        }
        return s;
    }
}
