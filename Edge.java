/*
 * Kevin Gu
 * March 5, 2020
 * Edge.java: Edge class for Graph.java (for use in Kruskal's algorithm)
 */
public class Edge implements Comparable<Edge> {
    // contains a weight, a source index, and a destination index
    double weight;
    int source;
    int destination;

    // constructor
    Edge(double weight, int source, int destination) {
        this.weight = weight;
        this.source = source;
        this.destination = destination;
    }

    // compareTo() method for MinPQ.java
    @Override
    public int compareTo(Edge edge) {
        return (int) (this.weight - edge.weight);
    }

    // equals() method for MinPQ.java
    @Override
    public boolean equals(Object obj) {
        Edge edge = (Edge) obj;
        return (this.source == edge.source && this.destination == edge.destination) || (this.destination == edge.source && this.source == edge.destination);
    }

    // toString() method
    @Override
    public String toString() {
        return "source: " + source + ", destination: " + destination + ";";
    }
}