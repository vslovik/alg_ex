import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <tt>Randomized queue<tt> implementation supports each randomized queue operation
 * (besides creating an iterator) in constant amortized time and uses space
 * proportional to the number of items currently in the queue. That is, any
 * sequence of M randomized queue operations (starting from an empty queue)
 * should take at most cM steps in the worst case, for some constant c.
 * <p/>
 * Additionally, iterator implementation supports construction in time linear
 * in the number of items and it must support the operations next() and hasNext()
 * in constant worst-case time; it uses a linear amount of extra memory per
 * iterator.
 * <p/>
 * The order of two or more iterators to the same randomized queue is mutually
 * independent; each iterator maintains its own random order.
 *
 * @author Valeriya Slovikovskaya
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int N;            // number of elements on queue

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
    }

    /**
     * Is the queue empty?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items on the queue
     *
     * @return int
     */
    public int size() {
        return N;
    }

    /**
     * Resize the underlying array holding the elements
     */
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    /**
     * Add the item
     *
     * @param item Item
     */
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Empty item insertion");
        if (N == a.length) resize(2 * a.length);    // double size of array if necessary
        a[N++] = item;                            // add item
    }

    /**
     * Delete and return a random item
     *
     * @return Item
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int n = StdRandom.uniform(N);
        Item item = a[n];
        a[n] = a[N - 1];
        a[N - 1] = null;                              // to avoid loitering
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == a.length / 4) resize(a.length / 2);
        return item;
    }

    /**
     * Return (but do not delete) a random item
     *
     * @return Item
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int n = StdRandom.uniform(N);
        return a[n];
    }

    /**
     * Return an independent iterator over items in random order
     *
     * @return Iterator
     */
    public Iterator<Item> iterator() {
        return new RandomOrderIterator();
    }

    /**
     * An iterator, doesn't implement remove() since it's optional
     */
    private class RandomOrderIterator implements Iterator<Item> {
        private Item[] copy;      // array of items
        private int M;            // number of elements on queue

        public RandomOrderIterator() {
            copy = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                copy[i] = a[i];
            }
            M = N;
        }

        public boolean hasNext() {
            return M > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int n = StdRandom.uniform(M);
            Item item = copy[n];
            copy[n] = copy[M - 1];
            copy[M - 1] = null;  // to avoid loitering
            M--;
            return item;
        }
    }

    /**
     * Unit testing
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        StdOut.println("(" + q.size() + " left on queue)");

        Iterator<String> i = q.iterator();
        StdOut.println("---------------->q");
        while (i.hasNext()) {
            StdOut.println(i.next());
        }
    }
}
