/**
 *
 */
public class WordNet {

    private ST<Integer, Queue<String>> st;
    private ST<String, Queue<Integer>> ts;
    private SAP sap;

    /**
     * Constructor takes the name of the two input files
     * Uses space linear in the input size (size of synsets and hypernyms files).
     * Takes time linearithmic (or better) in the input size.
     * For the analysis, assume that the number of nouns per synset is bounded by a constant.
     * <p/>
     * Any advice on how to read in and parse the synset and hypernym data files?
     * Use the readLine() method in our In library to read in the data one line at a time.
     * Use the split() method in Java's String library to divide a line into fields.
     * You can find an example using split() in Domain.java.
     * Use Integer.parseInt() to convert string id numbers into integers.
     * <p/>
     * What data structure(s) should I use to store the synsets, synset ids, and hypernyms?
     * This part of the assignment is up to you. You must carefully select data structures
     * to achieve the specified performance requirements.
     * <p/>
     * Do I need to store the glosses? No, you won't use them on this assignment.
     */
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null)
            throw new java.lang.NullPointerException();

        In in = new In(synsets);

        st = new ST<Integer, Queue<String>>();
        ts = new ST<String, Queue<Integer>>();

        int V = 0, key;
        String line;
        String[] fields, syns;
        while (in.hasNextLine()) {
            line = in.readLine();
            fields = line.split(",");
            key = Integer.parseInt(fields[0]);
            syns = fields[1].split(" ");
            for (int i = 0; i < syns.length; i++) {
                String val = syns[i];
                if (!st.contains(key)) st.put(key, new Queue<String>());
                if (!ts.contains(val)) ts.put(val, new Queue<Integer>());
                st.get(key).enqueue(val);
                ts.get(val).enqueue(key);
            }
            V += 1;
        }

        if (V == 0) throw new java.lang.IllegalArgumentException();

        Digraph G = new Digraph(V);

        in = new In(hypernyms);

        while (in.hasNextLine()) {
            line = in.readLine();
            fields = line.split(",");
            int key1 = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int key2 = Integer.parseInt(fields[i]);
                if (st.contains(key1) && st.contains(key2))
                    G.addEdge(key1, key2);
            }
        }

        validate(G);

        sap = new SAP(G);
    }

    /**
     * @param G Digraph
     */
    private void validate(Digraph G) {
        int count = 0, roots = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                roots++;
            for (int w : G.adj(v))
                if (G.outdegree(w) == 0)
                    count++;
        }

        if (count == 0) throw new java.lang.IllegalArgumentException("Cycle");
        if (roots > 1) throw new java.lang.IllegalArgumentException("More than one root");
    }

    /**
     * returns all WordNet nouns
     *
     * @return Iterable
     */
    public Iterable<String> nouns() {
        return ts.keys();
    }

    /**
     * Is the word a WordNet noun?
     * Runs in time logarithmic (or better) in the number of nouns
     *
     * @param word String
     * @return boolean
     */
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
        return ts.contains(word);
    }

    /**
     * Distance between nounA and nounB (defined below)
     * Runs in time linear in the size of the WordNet digraph
     *
     * @param nounA String
     * @param nounB String
     * @return int
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        return sap.length(ts.get(nounA), ts.get(nounB));
    }

    /**
     * Shortest ancestral path. An ancestral path between two vertices v and w in a digraph
     * is a directed path from v to a common ancestor x, together with a directed path from
     * w to the same ancestor x. A shortest ancestral path is an ancestral path of minimum
     * total length.
     * <p/>
     * For example, in the digraph at left (digraph1.txt), the shortest ancestral path
     * between 3 and 11 has length 4 (with common ancestor 1). In the digraph at right
     * (digraph2.txt), one ancestral path between 1 and 5 has length 4 (with common ancestor
     * 5), but the shortest ancestral path has length 2 (with common ancestor 0).
     * <p/>
     * A synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     * <p/>
     * Runs in time linear in the size of the WordNet digraph
     *
     * @param nounA String
     * @param nounB String
     * @return String
     */
    //
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        int ancestor = sap.ancestor(ts.get(nounA), ts.get(nounB));
        return st.get(ancestor).toString();
    }

    /**
     * Do unit testing of this class
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        /**
         (distance = 23) white_marlin, mileage
         (distance = 33) Black_Plague, black_marlin
         (distance = 27) American_water_spaniel, histology
         (distance = 29) Brown_Swiss, barrel_roll
         */

        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.sap("individual", "edible_fruit"));
        StdOut.println(wordnet.distance("individual", "edible_fruit"));

        StdOut.println(wordnet.sap("white_marlin", "mileage"));
        StdOut.println(wordnet.distance("white_marlin", "mileage"));

        StdOut.println(wordnet.sap("Black_Plague", "black_marlin"));
        StdOut.println(wordnet.distance("Black_Plague", "black_marlin"));

        StdOut.println(wordnet.sap("American_water_spaniel", "histology"));
        StdOut.println(wordnet.distance("American_water_spaniel", "histology"));

        StdOut.println(wordnet.sap("Brown_Swiss", "barrel_roll"));
        StdOut.println(wordnet.distance("Brown_Swiss", "barrel_roll"));

    }
}