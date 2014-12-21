/**
 * Topological order for Grid DAG
 */
public class Topological {

    private int W, H;
    private boolean[] marked;
    private Stack<Integer> reversePost;

    public Topological(int W, int H) {
        this.W = W;
        this.H = H;
        reversePost = new Stack<Integer>();
        marked = new boolean[H * W];
        for (int x = 0; x < W; x++)
            if (!marked[x]) dfs(x, 0);
    }

    private void dfs(int x, int y) {
        int n = y * W + x;
        marked[n] = true;

        if (y + 1 < H) {
            for (int k = x - 1; k <= x + 1; k++) {
                if (k >= 0 && k < W && !marked[(y + 1) * W + k]) {
                    dfs(k, y + 1);
                }
            }
        }

        reversePost.push(n);
    }

    public Iterable<Integer> order() {
        return reversePost;
    }

    /**
     * Tests this <tt>Picture</tt> data type. Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     */
    public static void main(String[] args) {
        int W = 3;
        int H = 7;
        Topological t = new Topological(W, H);
        for (int N : t.order()) {
            System.out.printf("%d---%d\n", N % W, N / W);
        }
    }
}
