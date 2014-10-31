/**
 * Implements the A* algorithm
 * Be sure to ask Java for additional memory,
 * e.g., java -Xmx1600m Solver puzzle36.txt
 * <p/>
 * Deliverable Solver.java Board.java
 */
public class Solver {

    private Stack<Board> solution;

    // helper linked list class
    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int moves;

        public int compareTo(Node that) {
            int p = this.board.manhattan() + this.moves;
            int tp = that.board.manhattan() + that.moves;
            if (p < tp) return -1;
            if (p > tp) return +1;
            return 0;
        }
    }

    /**
     * Find a solution to the initial board (using the A* algorithm)
     *
     * @param initial Board
     */
    public Solver(Board initial) {
        Node nb, tnb;

        Node n = new Node();
        Node tn = new Node();

        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> tpq = new MinPQ<Node>();

        n.board = initial;
        tn.board = initial.twin();

        pq.insert(n);
        tpq.insert(tn);

        while (true) {
            if (pq.isEmpty())
                break;
            n = pq.delMin();
            tn = tpq.delMin();
            if (n.board.isGoal()) {
                solution = new Stack<Board>();
                solution.push(n.board);
                while (n.prev != null) {
                    n = n.prev;
                    solution.push(n.board);
                }
                break;
            }
            if (tn.board.isGoal()) {
                break;
            }
            for (Board b : n.board.neighbors()) {
                nb = new Node();
                nb.prev = n;
                nb.moves = n.moves + 1;
                nb.board = b;
                if (n.prev == null || (n.prev != null && !nb.board.equals(n.prev.board)))
                    pq.insert(nb);
            }
            for (Board b : tn.board.neighbors()) {
                tnb = new Node();
                tnb.prev = tn;
                tnb.moves = tn.moves + 1;
                tnb.board = b;
                if (tn.prev == null || (tn.prev != null && !tnb.board.equals(tn.prev.board)))
                    tpq.insert(tnb);
            }
        }
    }

    /**
     * Is the initial board solvable?
     *
     * @return boolean
     */
    public boolean isSolvable() {
        return solution != null;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable
     *
     * @return int
     */
    public int moves() {
        if (solution == null)
            return -1;
        return solution.size() - 1;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     *
     * @return Queue<Board>
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * Solve a slider puzzle (given below)
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
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}