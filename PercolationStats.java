/**
 * Monte Carlo simulation. To estimate the percolation threshold, consider the following computational experiment:
 * Initialize all sites to be blocked.
 * Repeat the following until the system percolates:
 * Choose a site (row i, column j) uniformly at random among all blocked sites.
 * Open the site (row i, column j).
 * The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.
 */
public class PercolationStats {

    private int N, T;
    private double[] exp;

    /**
     * Perform T independent computational experiments on an N-by-N grid
     *
     * @param N Size of the grid
     * @param T Number of experiments
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();
        this.N = N;
        this.T = T;
        int k, m, count;
        exp = new double[T];
        for (int i = 0; i < T; i++) {
            count = 0;
            Percolation p = new Percolation(N);
            while (!p.percolates()) {
                k = StdRandom.uniform(N) + 1;
                m = StdRandom.uniform(N) + 1;
                if (!p.isOpen(k, m)) {
                    p.open(k, m);
                    count++;
                }
            }
            exp[i] = (double) count / (double) (N * N);
        }
    }

    /**
     * Sample mean of percolation threshold
     *
     * @return double
     */
    public double mean() {
        return StdStats.mean(exp);
    }

    /**
     * Sample standard deviation of percolation threshold
     *
     * @return double
     */
    public double stddev() {
        return StdStats.stddev(exp);
    }

    /**
     * Returns lower bound of the 95% confidence interval
     *
     * @return double
     */
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * Returns upper bound of the 95% confidence interval
     *
     * @return double
     */
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * Takes two command-line arguments N and T,
     * performs T independent computational experiments on an N-by-N grid,
     * and prints out the mean, standard deviation, and the 95% confidence
     * interval for the percolation threshold.
     * <p/>
     * Uses standard statistics to compute the sample mean and standard deviation.
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println(String.format("%-24s%2s", "mean", "= ") + stats.mean());
        StdOut.println(String.format("%-24s%2s", "stddev", "= ") + stats.stddev());
        StdOut.println(String.format("%-24s%2s", "95% confidence interval", "= ") + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
