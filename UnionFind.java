/*
 * Kevin Gu
 * October 8, 2019
 * QuickUnionPC.java: A UF algorithm with path compression
 */
public class UnionFind {

    // a grid
    private int[] grid;

    // constructor
    public UnionFind(int N) {
        grid = new int[N];
        for (int i = 0; i < N; i++)
            grid[i] = i;
    }

    // links every site checked to root
    public int find(int i) {
        validate(i);
        while (i != grid[i]) {
            grid[i] = grid[grid[i]];
            i = grid[i];
        }
        return i;
    }

    // checks whether two indices are connected
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // unions two indices
    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        grid[i] = j;
    }

    // validates whether index p is within bounds
    public void validate(int p) {
        int n = grid.length;
        if (p < 0 || p >= n)
            throw new IllegalArgumentException("Index " + p + " is not between 0 and " + (n - 1) + ".");
    }
}
