/**
 * Outcast detection. Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others?
 * To identify an outcast, compute the sum of the distances between each noun and every other one:
 *
 * di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
 * and return a noun At for which dt is maximum.
 *
 * Assume that argument to outcast() contains only valid wordnet nouns (and that it contains at least two such nouns).
 */
public class Outcast {

    /**
     * Constructor takes a WordNet object
     * @param wordnet
     */
    public Outcast(WordNet wordnet)
    {

    }

    /**
     * Given an array of WordNet nouns, return an outcast
     *
     * @param nouns String[]
     * @return String
     */
    public String outcast(String[] nouns)
    {

    }

    /**
     * The following test client takes from the command line the name of a synset file, the name of a hypernym file,
     * followed by the names of outcast files, and prints out an outcast in each file
     *
     * Here is a sample execution:
     * % more outcast5.txt
     * horse zebra cat bear table
     *
     * % more outcast8.txt
     * water soda bed orange_juice milk apple_juice tea coffee
     *
     * % more outcast11.txt
     * apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
     *
     *
     * % java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
     * outcast5.txt: table
     * outcast8.txt: bed
     * outcast11.txt: potato
     *
     * @param args
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