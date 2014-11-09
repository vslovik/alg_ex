/**
 *
 */
public class WordNet {

    /**
     * Constructor takes the name of the two input files
     * Uses space linear in the input size (size of synsets and hypernyms files).
     * Takes time linearithmic (or better) in the input size.
     * For the analysis, assume that the number of nouns per synset is bounded by a constant.
     *
     * Any advice on how to read in and parse the synset and hypernym data files?
     * Use the readLine() method in our In library to read in the data one line at a time.
     * Use the split() method in Java's String library to divide a line into fields.
     * You can find an example using split() in Domain.java.
     * Use Integer.parseInt() to convert string id numbers into integers.
     *
     * What data structure(s) should I use to store the synsets, synset ids, and hypernyms?
     * This part of the assignment is up to you. You must carefully select data structures
     * to achieve the specified performance requirements.
     *
     * Do I need to store the glosses? No, you won't use them on this assignment.
     */
    public WordNet(String synsets, String hypernyms)
    {
        if(synsets == null || hypernyms == null)
            throw java.lang.NullPointerException;
        if(!isRootedDag()) java.lang.IllegalArgumentException
    }

    /**
     * Checks if graph is DAG
     * @return
     */
    private boolean isRootedDag()
    {
        return true;
    }

    /**
     * returns all WordNet nouns
     * @return
     */
    public Iterable<String> nouns()
    {

    }

    /**
     * Is the word a WordNet noun?
     * Runs in time logarithmic (or better) in the number of nouns
     *
     * @param word String
     * @return
     */
    public boolean isNoun(String word)
    {
        if (word == null) throw java.lang.NullPointerException;
        return true;
    }

    /**
     * Distance between nounA and nounB (defined below)
     * Runs in time linear in the size of the WordNet digraph
     *
     * @param nounA String
     * @param nounB String
     * @return int
     */
    public int distance(String nounA, String nounB)
    {
        if(nounA == null || nounB == null) throw java.lang.NullPointerException;
        if(!isNoun(nounA) || !isNoun(nounB)) throw java.lang.IllegalArgumentException;
    }

    /**
     * Shortest ancestral path. An ancestral path between two vertices v and w in a digraph
     * is a directed path from v to a common ancestor x, together with a directed path from
     * w to the same ancestor x. A shortest ancestral path is an ancestral path of minimum
     * total length.
     *
     * For example, in the digraph at left (digraph1.txt), the shortest ancestral path
     * between 3 and 11 has length 4 (with common ancestor 1). In the digraph at right
     * (digraph2.txt), one ancestral path between 1 and 5 has length 4 (with common ancestor
     * 5), but the shortest ancestral path has length 2 (with common ancestor 0).
     *
     * A synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     *
     * Runs in time linear in the size of the WordNet digraph
     *
     * @param nounA String
     * @param nounB String
     * @return String
     */
    //
    public String sap(String nounA, String nounB)
    {
        if(nounA == null || nounB == null) throw java.lang.NullPointerException;
        if(!isNoun(nounA) || !isNoun(nounB)) throw java.lang.IllegalArgumentException;
    }

    /**
     * Do unit testing of this class
     *
     * @param args
     */
    public static void main(String[] args)
    {

    }
}