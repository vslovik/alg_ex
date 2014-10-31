/**
 * The constructor receives an N-by-N array containing the N2 integers between 0 and N2 − 1,
 * where 0 represents the blank square. The implementation should supports all Board
 * methods in time proportional to N2 (or better) in the worst case.
 */
public class Board {

    private int N; // grid size
    private int[] blocks;
    private int manhattan;

    /**
     * Construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     * @param blocks Blocks
     */
    public Board(int[][] blocks) {
        int rows = blocks.length;
        this.N = rows;
        this.blocks = new int[N * N];
        if (rows < 2)
            throw new IllegalArgumentException();
        int cols = blocks[0].length;
        if (cols != rows)
            throw new IllegalArgumentException();
        int zeros = 0;
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    zeros++;
                this.blocks[count] = blocks[i][j];
                count++;
            }
        }
        if (zeros > 1)
            throw new IllegalArgumentException();

    }

    /**
     * Board dimension N
     *
     * @return N
     */
    public int dimension() {
        return N;
    }

    /**
     * Number of blocks out of place
     *
     * @return int
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            if (i + 1 != blocks[i])
                count++;
        }

        return count;
    }

    /**
     * Sum of Manhattan distances between blocks and goal
     *
     * @return int
     */
    public int manhattan() {
        if (isGoal())
            return 0;

        if (manhattan != 0)
            return manhattan;

        int row, col, k, l;
        for (int i = 0; i < N * N; i++) {
            if (blocks[i] == 0)
                continue;
            if (blocks[i] == i + 1)
                continue;
            col = (blocks[i] - 1) % N;
            row = (blocks[i] - 1) / N;
            k = i % N;
            l = i / N;
            manhattan += Math.abs(k - col) + Math.abs(l - row);
        }

        return manhattan;
    }

    /**
     * Is this board the goal board?
     *
     * @return boolean
     */
    public boolean isGoal() {
        for (int i = 0; i < N * N - 1; i++) {
            if (blocks[i] == 0)
                return false;
            if (blocks[i] != i + 1)
                return false;
        }

        return true;
    }

    /**
     * A board that is obtained by exchanging
     * two adjacent blocks in the same row
     *
     * @return Board
     */
    public Board twin() {

        int[][] b = new int[N][N];
        for (int row = 0; row < N; row++)
            for (int col = 0; col < N; col++)
                b[row][col] = blocks[row * N + col];

        Board twin = new Board(b);

        assert (twin.equals(this));

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                int index = row * N + col;
                if (blocks[index] > 0 && col + 1 < N && blocks[index + 1] > 0) {
                    int tmp = blocks[index];
                    twin.blocks[index] = blocks[index + 1];
                    twin.blocks[index + 1] = tmp;
                    return twin;
                }
            }
        }

        return twin;
    }

    /**
     * Does this board equal y?
     *
     * @param y Board
     * @return boolean
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < N * N; i++) {
            if (this.blocks[i] != that.blocks[i]) return false;
        }

        return true;
    }

    /**
     * All neighboring boards
     *
     * @return Queue<Board>
     */
    public Iterable<Board> neighbors() {
        int index, top, bottom;
        Board nb;
        Queue<Board> q = new Queue<Board>();
        int[][] b = new int[N][N];
        for (int row = 0; row < N; row++)
            for (int col = 0; col < N; col++)
                b[row][col] = blocks[row * N + col];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                index = row * N + col;
                if (blocks[index] == 0) {
                    if (row + 1 < N) {
                        bottom = (row + 1) * N + col;
                        nb = new Board(b);
                        nb.blocks[index] = nb.blocks[bottom];
                        nb.blocks[bottom] = 0;
                        q.enqueue(nb);
                    }
                    if (row - 1 >= 0) {
                        top = (row - 1) * N + col;
                        nb = new Board(b);
                        nb.blocks[index] = nb.blocks[top];
                        nb.blocks[top] = 0;
                        q.enqueue(nb);
                    }
                    if (col + 1 < N) {
                        nb = new Board(b);
                        nb.blocks[index] = nb.blocks[index + 1];
                        nb.blocks[index + 1] = 0;
                        q.enqueue(nb);
                    }
                    if (col - 1 >= 0) {
                        nb = new Board(b);
                        nb.blocks[index] = nb.blocks[index - 1];
                        nb.blocks[index - 1] = 0;
                        q.enqueue(nb);
                    }
                }
            }
        }

        return q;
    }

    /**
     * String representation of this board (in the output format specified below)
     *
     * @return String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N);
        s.append("\n");
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                s.append(String.format("%2s ", blocks[row * N + col]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Unit tests (not graded)
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = (int) in.readInt();
        Board board = new Board(blocks);
        StdOut.println("board:\n");
        StdOut.println(board.toString());
        StdOut.println("dimension:\n");
        StdOut.println(board.dimension());
        StdOut.println("hamming:\n");
        StdOut.println(board.hamming());
        StdOut.println("manhattan:\n");
        StdOut.println(board.manhattan());
        StdOut.println("isGoal:\n");
        StdOut.println(board.isGoal());
        StdOut.println("neighbors:\n");
        for (Board nb : board.neighbors()) {
            StdOut.println(nb.toString());
            StdOut.println(nb.equals(board));
        }
        StdOut.println("twin:\n");
        StdOut.println(board.twin().toString());

    }
}
