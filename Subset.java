/**
 * Client program that takes a command-line integer k;
 * reads in a sequence of N strings from standard input using StdIn.readString();
 * and prints out exactly k of them, uniformly at random.
 * <p/>
 * Each item from the sequence can be printed out at most once.
 * It's assumed that 0 ≤ k ≤ N, where N is the number of string
 * on standard input.
 * <p/>
 * The running time of Subset is linear in the size of the input.
 * It uses only a constant amount of memory plus either one Deque
 * or RandomizedQueue object of maximum size at most N,
 * where N is the number of strings on standard input.
 * <p/>
 * (For an extra challenge, use only one Deque or RandomizedQueue object of maximum size at most k.)
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        useRandomizedQueue(k);
    }

    /**
     * Use randomized queue
     *
     * @param k number
     */
    private static void useRandomizedQueue(int k) {
        String item;
        int m;
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int n = k;
        while (!StdIn.isEmpty()) {
            item = StdIn.readString();
            if (q.size() == k) {
                m = StdRandom.uniform(++n);
                if (m < k) {
                    q.dequeue();
                    q.enqueue(item);
                }
            } else {
                q.enqueue(item);
            }
        }
        while (!q.isEmpty()) {
            StdOut.println(q.dequeue());
        }
    }
}

