/*
 * Kevin Gu, Alex Yuk, Elven Shum
 * Jan 27, 2020
 * MinPQ.java: Miniumum priority queue
 */
public class MinPQ<T> {

    // containing keys and size
    private T[] pq;
    private int size;

    // Constructor
    public MinPQ() {
        pq = (T[]) new Object[1];
        size = 0;
    }

    // Returns size
    public int size() {
        return size;
    }

    // Whether the PQ is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns smallest priority element
    public T min() {
        if (isEmpty())
            return null;
        return pq[1];
    }

    // Resizes pq to newSize
    private void resize(int newSize) {
        T[] temp = (T[]) new Object[newSize];
        for (int i = 1; i <= size; i++)
            temp[i] = pq[i];
        pq = temp;
    }

    // Inserts element
    public void insert(T element) {
        // Change pq size if too small
        if (size == pq.length - 1)
            resize(2 * pq.length);
        // Adds new element
        pq[++size] = element;
        // Maintain min heap
        insertUtility(size);
    }

    // Traverses upwards and sorting in correct order
    private void insertUtility(int index) {
        // i / 2 used to maintain min heap
        while (index > 1 && compare(index / 2, index)) {
            swap(index, index / 2);
            index = index / 2;
        }
    }

    // Swaps elements in i and j
    private void swap(int i, int j) {
        T temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    // Deletes and returns smallest priority element
    public T delMin() {
        if (isEmpty())
            return null;
        // Temp to return later
        T min = pq[1];
        swap(1, size--);
        // Keep min heep order
        delMinUtility(1);
        // No memory leak
        pq[size + 1] = null;

        // Resize to save memory
        if ((size > 0) && (size == (pq.length - 1) / 4))
            resize(pq.length / 2);

        return min;
    }

    // Traverses downwards and sorting in correct order
    private void delMinUtility(int index) {
        // 2 * i used to maintain min heap
        while (size >= 2 * index) {
            int temp = 2 * index;
            // Checks if out of bounds before comparing to avoid null pointer
            if (temp < size && compare(temp, temp + 1))
                temp++;
            // If index is smaller priority than temp, break
            if (!compare(index, temp))
                break;
            swap(index, temp);
            index = temp;
        }
    }

    private boolean compare(int i, int j) {
        return ((Comparable<T>) pq[i]).compareTo(pq[j]) > 0;
    }
}