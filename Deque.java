/*************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque < input.txt
 *  Data files:
 *
 *  Stack implementation with a resizing array.
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>Deque</tt> class represents a double-ended queue or deque, it is a
 * generalization of a stack and a queue that supports inserting and removing
 * items from either the front or the back of the data structure.
 * <p/>
 * Deque implementation supports each deque operation in constant worst-case time
 * and uses space proportional to the number of items currently in the deque.
 * <p/>
 * Iterator implementation supports the operations next() and hasNext()
 * (plus construction) in constant worst-case time and use a constant amount of
 * extra space per iterator.
 *
 * @author Valeriya Slovikovskaya
 */
public class Deque<Item> implements Iterable<Item> {
    private int N;          // size of the deque
    private Node first;     // top of stack
    private Node last;    // bottom of stack

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    /**
     * Initializes an empty deque.
     */
    public Deque() {
        first = null;
        last = null;
        N = 0;
        assert check();
    }

    /**
     * Checks internal invariants
     *
     * @return int
     */
    private boolean check() {
        if (N == 0) {
            if (first != null || last != null) return false;
        } else if (N == 1) {
            if (first == null) return false;
            if (first.next != null) return false;
            if (last != null) return false;
        } else {
            if (first.next == null) return false;
            if (last.prev == null) return false;
        }

        // check internal consistency of instance variable N
        int numberOfNodes = 0;
        for (Node x = first; x != null; x = x.next) {
            numberOfNodes++;
        }

        return (numberOfNodes != N);
    }

    /**
     * Is this deque empty?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in the deque.
     *
     * @return int
     */
    public int size() {
        return N;
    }

    /**
     * Insert the item at the front
     *
     * @param item the item to insert
     * @throws NullPointerException
     */
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("Empty item insertion");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;

        if (size() == 0) {
            last = first;
        } else {
            oldFirst.prev = first;
        }
        N++;

        assert check();
    }

    /**
     * Insert the item at the end
     *
     * @param item the item to insert
     * @throws NullPointerException
     */
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("Empty item insertion");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        if (size() == 0) {
            first = last;
        } else {
            oldLast.next = last;
        }
        N++;
        assert check();
    }

    /**
     * Delete and return the item at the front
     *
     * @return item the item to delete and return
     * @throws NoSuchElementException
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node

        if (N == 1) {
            last = first;
        } else {
            first.prev = null;
        }
        N--;
        assert check();
        return item;                   // return the saved item
    }

    /**
     * Delete and return the item at the end
     *
     * @return item the item to delete and return
     * @throws NoSuchElementException
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;        // save item to return
        last = last.prev;            // delete first node
        if (N == 1) {
            first = last;
        } else {
            last.next = null;
        }
        N--;
        assert check();
        return item;                 // return the saved item
    }

    /**
     * Return an iterator over items in order from front to end
     *
     * @return Iterator<Item>
     */
    public Iterator<Item> iterator() {
        return new FrontToEndIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class FrontToEndIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * Unit tests the <tt>Deque</tt> data type.
     */
    public static void main(String[] args) {
        Deque<String> dq1 = new Deque<String>();
        Deque<String> dq2 = new Deque<String>();
        Deque<String> dq3 = new Deque<String>();
        Deque<String> dq4 = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            dq1.addFirst(item);
            dq2.addLast(item);
            dq3.addFirst(item);
            dq4.addLast(item);
        }
        Iterator<String> i1 = dq1.iterator();
        Iterator<String> i2 = dq2.iterator();
        Iterator<String> i3 = dq3.iterator();
        Iterator<String> i4 = dq4.iterator();
        StdOut.println("---------------->dq1");
        while (i1.hasNext()) {
            StdOut.println(i1.next());
        }
        StdOut.println("---------------->dq2");
        while (i2.hasNext()) {
            StdOut.println(i2.next());
        }
        StdOut.println("---------------->dq3");
        while (i3.hasNext()) {
            StdOut.println(i3.next());
        }
        StdOut.println("---------------->dq4");
        while (i4.hasNext()) {
            StdOut.println(i4.next());
        }
        StdOut.println("---------------->FF");
        StdOut.println(dq1.size());
        while (!dq1.isEmpty()) {
            StdOut.println(dq1.removeFirst());
        }
        StdOut.println("---------------->LF");
        StdOut.println(dq2.size());
        while (!dq2.isEmpty()) {
            StdOut.println(dq2.removeFirst());
        }
        StdOut.println("---------------->FL");
        StdOut.println(dq3.size());
        while (!dq3.isEmpty()) {
            StdOut.println(dq3.removeLast());
        }
        StdOut.println("---------------->LL");
        StdOut.println(dq4.size());
        while (!dq4.isEmpty()) {
            StdOut.println(dq4.removeLast());
        }
    }
}
