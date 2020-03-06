import java.util.Iterator;

/*
 * Kevin Gu
 * Stack.java
 * October 21, 2019
 */
public class Stack<Item> implements Iterable<Item> {

    private Node top;
    private int size;

    /* constructor */
    public Stack() {
        top = null;
        size = 0;
    }

    /* pushes item */
    public void push(Item item) {
        Node temp = top;
        top = new Node();
        top.item = item;
        top.next = temp;
        size++;
    }

    /* pops top */
    public Item pop() {
        Item element = top.item;
        top = top.next;
        size--;
        return element;
    }

    /* peeks top */
    public Item peek() {
        return top.item;
    }

    /* returns size */
    public int size() {
        return size;
    }

    /* checks if stack is empty */
    public Boolean isEmpty() {
        return top == null;
    }

    @Override
    public Iterator<Item> iterator() {
        return new StackIterator();
    }

    private class StackIterator implements Iterator<Item> {

        private Node temp = top;

        public boolean hasNext() {
            return temp != null;
        }

        public void remove() {
        }

        public Item next() {
            Item item = temp.item;
            temp = temp.next;
            return item;
        }
    }

    private class Node {
        Item item;
        Node next;
    }

    public String toString() {
        String returned = "";
        Node temp = top;
        while (temp != null) {
            returned += temp.item + " ";
            temp = temp.next;
        }
        return returned;
    }
}