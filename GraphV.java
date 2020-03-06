/*
 * Kevin Gu
 * March 5, 2020
 * Graph.java: Graph class implemented with vertices for Dijkstra's algorithm
 */
public class GraphV {

    // size and array of vertices
    private int size;
    private Vertex[] vertices;

    // constructor
    GraphV(int size) {
        this.size = size;
        vertices = new Vertex[size];
    }

    // returns number of vertices
    int vertices() {
        return size;
    }

    // adds a vertex at specified index and location
    void addVertex(int index, Location location) {
        vertices[index] = new Vertex(index, location);
    }

    // adds adjacent vertex to adjacent list for both source and destination vertex
    void addAdjacent(int source, int destination) {
        vertices[source].adjacents.add(destination);
        vertices[destination].adjacents.add(source);
    }

    // gets vertex at index
    Vertex getVertex(int index) {
        return vertices[index];
    }

    // returns list of vertices using dijkstra's algorithm for shortest path
    LinkedList<Node> dijkstra(int source, int destination) {
        // construct minimum priority queue
        MinPQ<Node> pq = new MinPQ<>();
        int vertices = vertices();

        // visited array to track if vertex is already visited
        boolean[] visited = new boolean[vertices];

        // track current minimum distances to vertex
        double[] distance = new double[vertices];

        // for backtracking to source node
        int[] parent = new int[vertices];

        // set distance as infinity and parent as -1 for comparison/backtrack
        for (int i = 0; i < vertices; i++) {
            distance[i] = Double.MAX_VALUE;
            parent[i] = -1;
        }

        // lower priority for source so that it comes off first
        distance[source] = 0;
        // for backtracking
        parent[source] = source;

        // inserting all nodes
        for (int i = 0; i < vertices; i++)
            pq.insert(new Node(i, distance[i]));

        // while priority queue isn't empty and destination isn't visited
        while (!pq.isEmpty() && !visited[destination]) {

            // delete minimum on priority queue
            Node n = pq.delMin();

            // if it has no connections/is isolated, break
            if (parent[n.index] == -1) break;

            // set vertex as visited
            visited[n.index] = true;

            // get the vertex
            Vertex v = getVertex(n.index);

            // go through adjacents
            for (int i = 0; i < v.adjacents.size(); i++) {
                int index = v.adjacents.get(i);

                // if unvisited
                if (!visited[index]) {
                    // get adjacent vertex
                    Vertex v2 = this.getVertex(index);

                    // calculate new weight
                    double weight = calculateWeight(v.location, v2.location) + n.weight;

                    // if weight is less than current minimum
                    if (weight < distance[index]) {

                        // update minimum and enqueue into PQ
                        distance[index] = weight;
                        pq.insert(new Node(index, weight));

                        // set current index as parent
                        parent[index] = n.index;
                    }
                }
            }
        }

        // create list for return
        LinkedList<Node> list = new LinkedList<>();

        // if no route, return empty list
        if (parent[destination] == -1)
            return list;
        else {
            // create stack and push
            Stack<Integer> stack = new Stack<>();
            for (int i = destination; i != source; i = parent[i])
                stack.push(i);

            list.add(new Node(source, distance[source]));

            // pop off into list
            while (!stack.isEmpty()) {
                int i = stack.pop();
                list.add(new Node(i, distance[i]));
            }
            // return list
            return list;
        }
    }

    // calculates distance between two locations
    private double calculateWeight(Location l1, Location l2) {
        return Math.sqrt(Math.pow(l2.x - l1.x, 2) + Math.pow(l2.y - l1.y, 2));
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
