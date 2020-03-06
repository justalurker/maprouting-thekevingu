/*
 * Kevin Gu
 * March 5, 2020
 * Vertex.java: Vertex class for Graph.java
 */
public class Vertex {
    // each vertex contains an index, a location, and a list of indices of adjacent vertices
    int index;
    Location location;
    LinkedList<Integer> adjacents;

    // constructor
    Vertex(int index, Location location) {
        this.index = index;
        this.location = location;
        adjacents = new LinkedList<>();
    }

    // toString() method
    @Override
    public String toString() {
        return index + ": " + location.toString() + " " + adjacents.toString();
    }
}