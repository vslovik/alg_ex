/**
 * By convention, the row and column indices i and j are integers between 1 and N,
 * where (1, 1) is the upper-left site.
 * <p/>
 * The constructor takes time proportional to N2;
 * All methods take constant time plus a constant
 * number of calls to the union-find methods union(),
 * find(), connected(), and count().
 */
public class Percolation {

    private int N; // grid size
    private int top;
    private int bottom;
    private boolean[][] cells; // grid cells
    private WeightedQuickUnionUF uf; // union find object
    private WeightedQuickUnionUF pUf; // bottom union find object
    private boolean percolates = false;

    /**
     * Create N-by-N grid, with all sites blocked
     *
     * @param N Number
     */
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException();

        this.N = N;
        this.top = N * N;
        this.bottom = N * N + 1;


        uf = new WeightedQuickUnionUF(N * N + 1);
        pUf = new WeightedQuickUnionUF(N * N + 2);

        this.cells = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cells[i][j] = false;
            }
        }
    }

    /**
     * Open site (row i, column j) if it is not already
     *
     * @param i Row
     * @param j Column
     */
    public void open(int i, int j) {
        if (i <= 0 || j <= 0 || i > N || j > N)
            throw new IndexOutOfBoundsException();
        int k = i - 1;
        int m = j - 1;
        cells[k][m] = true;
        // order number of cells[k][m]: k*N + m
        // top: (k-1)*N + m, bottom: (k+1)*N + m,
        // left: k*N + m - 1, right: k*N + m + 1
        int num = k * N + m;
        if (k == 0) {
            uf.union(num, top);
            if (pUf != null)
                pUf.union(num, top);
        }
        if (k == N - 1) {
            if (pUf != null) pUf.union(num, bottom);
        }
        if ((k - 1 >= 0) && cells[k - 1][m]) {
            uf.union(num, (k - 1) * N + m);
            if (pUf != null) pUf.union(num, (k - 1) * N + m);
        }
        if ((k + 1 < N) && cells[k + 1][m]) {
            uf.union(num, (k + 1) * N + m);
            if (pUf != null) pUf.union(num, (k + 1) * N + m);
        }
        if ((m - 1 >= 0) && cells[k][m - 1]) {
            uf.union(num, num - 1);
            if (pUf != null) pUf.union(num, num - 1);
        }
        if ((m + 1 < N) && cells[k][m + 1]) {
            uf.union(num, num + 1);
            if (pUf != null) pUf.union(num, num + 1);
        }
        if (!percolates && pUf != null && pUf.connected(top, bottom)) {
            percolates = true;
            pUf = null;
        }
    }

    /**
     * Is site (row i, column j) open?
     * Mark new site as open; connect it to
     * all of its adjacent open sites
     * up to 4 calls to union()
     *
     * @param i Row
     * @param j Column
     * @return boolean
     */
    public boolean isOpen(int i, int j) {
        if (i <= 0 || j <= 0 || i > N || j > N)
            throw new IndexOutOfBoundsException();
        int k = i - 1;
        int m = j - 1;
        return cells[k][m];
    }

    /**
     * Is site (row i, column j) full?
     * A full site is an open site that can be connected to an open site
     * in the top row via a chain of neighboring (left, right, up, down)
     * open sites
     *
     * @param i Row
     * @param j Column
     * @return boolean
     */
    public boolean isFull(int i, int j) {
        if (i <= 0 || j <= 0 || i > N || j > N)
            throw new IndexOutOfBoundsException();
        int k = i - 1;
        int m = j - 1;
        int num = k * N + m;
        return isOpen(i, j) && uf.connected(num, top);
    }

    /**
     * Does the system percolate?
     *
     * @return boolean
     */
    public boolean percolates() {
        return percolates;
    }

    /**
     * Test client, optional
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        int k, m, N = 10;
        Percolation p = new Percolation(N);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                p.open(i + 1, j + 1);
                StdOut.println(p.percolates());
            }
        }

        while (!p.percolates()) {
            k = StdRandom.uniform(N) + 1;
            m = StdRandom.uniform(N) + 1;
            if (!p.isOpen(k, m))
                p.open(k, m);
        }
    }
}
