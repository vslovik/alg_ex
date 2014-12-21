/**
 * Your task. Your challenge is to write a Boggle solver
 * that finds all valid words in a given Boggle board,
 * using a given dictionary. Implement an immutable data
 * type BoggleSolver with the following API:
 * <p/>
 * Boggle boards. A boggle board consists of two integers M and N,
 * followed by the M × N characters in the board, with the integers
 * and characters separated by whitespace. You can assume the
 * integers are nonnegative and that the characters are uppercase
 * letters A through Z (with the two-letter sequence Qu represented
 * as either Q or Qu). For example, here are the files board4x4.txt
 * and board-q.txt:
 * <p/>
 * % more board4x4.txt        % more board-q.txt
 * 4 4                        4 4
 * A  T  E  E                 S  N  R  T
 * A  P  Y  O                 O  I  E  L
 * T  I  N  U                 E  Qu T  T
 * E  D  S  E                 R  S  A  T
 * <p/>
 * Performance. If you choose your data structures and algorithms
 * judiciously, your program can preprocess the dictionary and find all
 * valid words in a random Hasbro board (or even a random 10-by-10 board)
 * in a fraction of a second. To stress test the performance of your
 * implementation, create one BoggleSolver object (from a given dictionary);
 * then, repeatedly generate and solve random Hasbro boards.
 * How many random Hasbro boards can you solve per second? For full
 * credit, your program must be able to solve thousands of random Hasbro
 * boards per second. The goal on this assignment is raw speed—for example,
 * it's fine to use 10× more memory if the program is 10× faster.
 * <p/>
 * Challenge for the bored. Here are some challenges:
 * Find a maximum scoring 4-by-4 Hasbro board using the Zinga list of 584,983 Italian words.
 * <p/>
 * These are purely suggestions for how you might make progress. You do not have to follow these steps.
 * <p/>
 * Familiarize yourself with the BoggleBoard.java data type.
 * <p/>
 * Use a standard set data type to represent the dictionary, e.g., a SET<String>, a TreeSet<String>,
 * or a HashSet<String>.
 * <p/>
 * Create the data type BoggleSolver.
 * Write a method based on depth-first search to enumerate all
 * strings that can be composed by following sequences of adjacent dice. That is, enumerate all
 * simple paths in the Boggle graph (but there is no need to explicitly form the graph).
 * For now, ignore the special two-letter sequence Qu.
 * <p/>
 * Now, implement the following critical backtracking optimization: when the current path corresponds
 * to a string that is not a prefix of any word in the dictionary, there is no need to expand the path
 * further. To do this, you will need to create a data structure for the dictionary that supports
 * the prefix query operation: given a prefix, is there any word in the dictionary that starts with that prefix?
 * <p/>
 * Deal with the special two-letter sequence Qu.
 * <p/>
 * You will likely need to optimize some aspects of your program to pass all of the performance points (which are, intentionally, more challenging on this assignment).
 * Here are a few ideas:
 * <p/>
 * Make sure that you have implemented the critical backtracking optimization described above. This is, by far, the most important step—several orders of magnitude!
 * Think about whether you can implement the dictionary in a more efficient manner. Recall that the alphabet consists of only the 26 letters A through Z.
 * Exploit that fact that when you perform a prefix query operation, it is usually alomst identical to the previous prefix query, except that it is one letter longer.
 * Precompute the Boggle graph, i.e., the set of cubes adjancent to each cube. But don't necessarily use a heavyweight Graph object.
 * Consider a nonrecursive implemention of the prefix query operation.
 * Consider a nonrecursive implemention of depth-first search.
 * <p/>
 * Scores
 * word length  	  points
 * 0–2	0
 * 3–4	1
 * 5	2
 * 6	3
 * 7	5
 * 8+	11
 */
public class BoggleSolver {

    private TST<Boolean> dict;
    private BoggleBoard board;
    private SET<String> words;
    private Stack<Node> bt;
    private Stack<Stack<Node>> bn;

    private static class Node {
        private int row;
        private int col;
    }

    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     *
     * @param dictionary Dictionary
     */
    public BoggleSolver(String[] dictionary) {
        dict = new TST<Boolean>();
        for (String word : dictionary)
            dict.put(word, true);
    }

    /**
     * @param l length
     * @return score
     */
    private int score(int l) {
        if (l < 3) return 0;
        else if (l == 3 || l == 4) return 1;
        else if (l == 5) return 2;
        else if (l == 6) return 3;
        else if (l == 7) return 5;
        else return 11;
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     *
     * @param board Board
     * @return Words
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        words = new SET<String>();
        bt = new Stack<Node>();
        bn = new Stack<Stack<Node>>();

        for (int row = 0; row < board.rows(); row++)
            for (int col = 0; col < board.cols(); col++)
                dfs(row, col);

        return words;
    }

    /**
     * @param row Row
     * @param col Col
     * @return Stack<Node>
     */
    private Stack<Node> getNbrs(int row, int col) {

        Stack<Node> nbrs = new Stack<Node>();
        for (int i = Math.max(0, row - 1); i <= Math.min(board.rows() - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(board.cols() - 1, col + 1); j++) {
                if (i == row && j == col) continue;
                Node node = new Node();
                node.row = i;
                node.col = j;
                nbrs.push(node);
            }
        }

        return nbrs;
    }

    /**
     * @param row Row
     * @param col Col
     */
    private void dfs(int row, int col) {
        Node nb;
        String prefix, sprefix, prev;

        nb = new Node();
        nb.row = row;
        nb.col = col;

        Stack<Node> nbrs = getNbrs(nb.row, nb.col);
        bn.push(nbrs);
        bt.push(nb);

        while (bn.size() > 0) {

            while (nbrs.size() == 0 && bn.size() > 0) {
                nbrs = bn.peek();
                if (nbrs.size() == 0) {
                    nbrs = bn.pop();
                    bt.pop();
                }
            }

            if (bn.size() == 0)
                break;

            nb = nbrs.pop();

            prev = "";
            boolean c = false;
            if (bt.size() > 0)
                for (Node n : bt) {
                    prev = Character.toString(board.getLetter(n.row, n.col)) + prev;
                    if (n.col == nb.col && n.row == nb.row) {
                        c = true;
                        break;
                    }
                }
            if (c) continue;

            prefix = prev + Character.toString(board.getLetter(nb.row, nb.col));
            sprefix = prefix.replace("Q", "QU");


            Iterable<String> keys = dict.keysWithPrefix(sprefix);

            if (!keys.iterator().hasNext())
                continue;

            if (sprefix.length() > 2 && dict.contains(sprefix))
                words.add(sprefix);

            nbrs = getNbrs(nb.row, nb.col);

            bn.push(nbrs);
            bt.push(nb);

        }
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
     *
     * @param word Word
     * @return int
     */
    public int scoreOf(String word) {
        if (dict.contains(word)) return score(word.length());
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int counter = 0;
        for (String word : solver.getAllValidWords(board)) {
            //StdOut.println(word);
            score += solver.scoreOf(word);
            counter++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Counter = " + counter);
    }

}
