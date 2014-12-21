/**
 * Measuring the semantic relatedness of two nouns. Semantic relatedness refers to the degree to which two concepts are related.
 * Measuring semantic relatedness is a challenging problem. For example, most of us agree that George Bush and John Kennedy
 * (two U.S. presidents) are more related than are George Bush and chimpanzee (two primates). However, not most of us agree
 * that George Bush and Eric Arthur Blair are related concepts. But if one is aware that George Bush and Eric Arthur Blair
 * (aka George Orwell) are both communicators, then it becomes clear that the two concepts might be related.
 * We define the semantic relatedness of two wordnet nouns A and B as follows:
 * <p/>
 * distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
 * This is the notion of distance that you will use to implement the distance() and sap() methods in the WordNet data type.
 * <p/>
 * All methods (and the constructor) should take time at most proportional to E + V in the worst case,
 * where E and V are the number of edges and vertices in the digraph, respectively.
 * Your data type should use space proportional to E + V.
 * <p/>
 * Should I re-implement breadth-first search in my SAP class? It depends. You are free to call the relevant methods in
 * BreadthFirstDirectedPaths.java. However, you can improve performance by several orders of magnitude by implementing
 * it yourself, optimizing it for repeated calls with the same digraph. (See the optional optimization section below.)
 * If you choose to implement your own version of BFS, give it a different name, e.g., DeluxeBFS.java, and submit it.
 */
public class SAP {

    private final Digraph G;

    /**
     * Constructor takes a digraph (not necessarily a DAG)
     * What assumption can I make about the digraph G passed to the SAP constructor?
     * It can be any digraph, not necessarily a DAG.
     * <p/>
     * How can I make SAP immutable? You can (and should) save the associated digraph in an instance variable.
     * However, because our Digraph data type is mutable, you must first make a defensive copy by calling the copy constructor.
     *
     * @param G Digraph
     */
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    /**
     * Length of shortest ancestral path between v and w; -1 if no such path
     * I understand how to compute the length(int v, int w) method in time proportional to E + V
     * but my length(Iterable<Integer> v, Iterable<Integer> w) method takes time proportional to a × b × (E + V),
     * where a and b are the sizes of the two iterables. How can I improve it to be proportional to E + V?
     * of using a single source.
     *
     * @param v int
     * @param w int
     * @return int
     */
    public int length(int v, int w) {
        int min = -1, l;
        int N = G.V();
        if (v < 0 || v > N - 1 || w < 0 || w > N - 1) throw new java.lang.IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        if (v == w)
            return 0;

        if (bfsV.hasPathTo(w) && bfsV.distTo(w) == 1)
            return 1;

        if (bfsW.hasPathTo(v) && bfsW.distTo(v) == 1)
            return 1;

        for (int vertex = 0; vertex < N; vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                l = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (min == -1 || min > l)
                    min = l;
            }
        }
        return min;
    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     * <p/>
     * Is a vertex considered an ancestor of itself? Yes.
     *
     * @param v int
     * @param w int
     * @return int
     */
    public int ancestor(int v, int w) {
        int min = -1, ancestor = -1, l;
        int N = G.V();
        if (v < 0 || v > N - 1 || w < 0 || w > N - 1) throw new java.lang.IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        if (v == w)
            return v;

        if (bfsV.hasPathTo(w) && bfsV.distTo(w) == 1)
            return w;

        if (bfsW.hasPathTo(v) && bfsW.distTo(v) == 1)
            return v;

        for (int vertex = 0; vertex < N; vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                l = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (min == -1 || min > l) {
                    min = l;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     *
     * @param v int
     * @param w int
     * @return int
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int min = -1, l;
        int N = G.V();

        for (int vertex : v) {
            if (vertex < 0 || vertex > N - 1) throw new java.lang.IndexOutOfBoundsException();
        }

        for (int vertex : w) {
            if (vertex < 0 || vertex > N - 1) throw new java.lang.IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int vertex = 0; vertex < N; vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                l = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (l == 0)
                    return 0;
                if (min == -1 || min > l)
                    min = l;
            }
        }
        return min;
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no such path
     *
     * @param v int
     * @param w int
     * @return int
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int min = -1, ancestor = -1, l;
        int N = G.V();

        for (int vertex : v) {
            if (vertex < 0 || vertex > N - 1) throw new java.lang.IndexOutOfBoundsException();
        }

        for (int vertex : w) {
            if (vertex < 0 || vertex > N - 1) throw new java.lang.IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int vertex = 0; vertex < N; vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                l = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (l == 0)
                    return vertex;
                if (min == -1 || min > l) {
                    min = l;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    }

    /**
     * Do unit testing of this class
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}