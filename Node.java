/*
 * Kevin Gu
 * March 5, 2020
 * Node.java: A comparable node class containing indices and weights for use in MinPQ.java
 */
public class Node implements Comparable<Node> {
    // each node contains an index of a vertex and a weight
    int index;
    double weight;

    // constructor
    Node(int index, double weight) {
        this.index = index;
        this.weight = weight;
    }

    // compareTo() method for use in MinPQ.java
    @Override
    public int compareTo(Node node) {
        if (weight < node.weight)
            return -1;
        else if (weight > node.weight)
            return 1;
        return 0;
    }

    // equals() method for use in MinPQ.java
    @Override
    public boolean equals(Object obj) {
        Node n = (Node) obj;
        return n.index == index;
    }

    // toString() method
    @Override
    public String toString() {
        return "node " + index;
    }

//    // toString() method
//    @Override
//    public String toString() {
//        return "node " + index + " " + weight;
//    }
}