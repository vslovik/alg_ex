/**
 * Outcast detection. Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others?
 * To identify an outcast, compute the sum of the distances between each noun and every other one:
 * <p/>
 * di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
 * and return a noun At for which dt is maximum.
 * <p/>
 * Assume that argument to outcast() contains only valid wordnet nouns (and that it contains at least two such nouns).
 */

public class Outcast {

    private WordNet wordnet;

    /**
     * Constructor takes a WordNet object
     *
     * @param wordnet WordNet
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return an outcast
     * di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
     *
     * @param nouns String[]
     * @return String
     */
    public String outcast(String[] nouns) {
        int max = 0, d;
        String outcast = nouns[0];
        for (String n1 : nouns) {
            d = 0;
            for (String n2 : nouns)
                d += wordnet.distance(n1, n2);
            if (d > max) {
                max = d;
                outcast = n1;
            }
        }
        return outcast;
    }

    /**
     * The following test client takes from the command line the name of a synset file, the name of a hypernym file,
     * followed by the names of outcast files, and prints out an outcast in each file
     * <p/>
     * Here is a sample execution:
     * % more outcast5.txt
     * horse zebra cat bear table
     * <p/>
     * % more outcast8.txt
     * water soda bed orange_juice milk apple_juice tea coffee
     * <p/>
     * % more outcast11.txt
     * apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
     * <p/>
     * <p/>
     * % java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
     * outcast5.txt: table
     * outcast8.txt: bed
     * outcast11.txt: potato
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
