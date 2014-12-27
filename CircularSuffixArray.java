import java.util.Arrays;

/**
 * To efficiently implement the key component in the Burrows-Wheeler transform, you will use a fundamental
 * data structure known as the circular suffix array, which describes the abstraction of a sorted array
 * of the N circular suffixes of a string of length N. As an example, consider the string "ABRACADABRA!"
 * of length 12. The table below shows its 12 circular suffixes and the result of sorting them.
 * <p/>
 * i       Original Suffixes           Sorted Suffixes         index[i]
 * --    -----------------------     -----------------------    --------
 * 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 * 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 * 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
 * 3    A C A D A B R A ! A B R     A B R A C A D A B R A !    0
 * 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 * 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 * 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 * 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 * 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 * 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
 * 10   A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
 * 11   ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
 * We define index[i] to be the index of the original suffix that appears ith in the sorted array. For example,
 * index[11] = 2 means that the 2nd original suffix appears 11th in the sorted order (i.e., last alphabetically).
 * Your job is to implement the following circular suffix array API, which provides the client access to the
 * index[] values:
 * <p/>
 * CircularSuffixArray
 * <p/>
 * Corner cases. The constructor should throw a java.lang.NullPointerException if the argument is null;
 * the method index() should throw a java.lang.IndexOutOfBoundsException if i is outside its prescribed
 * range (between 0 and N − 1).
 * <p/>
 * Performance requirements. Your data type should use space proportional to N. The constructor should
 * take time proportional to N log N (or better) on typical English text; the methods length() and index()
 * should take constant time in the worst case. Warning: beginning with Java 7, Update 6, the substring()
 * method takes time and space proportional to the length of the substring—in other words, you cannot form
 * the N circular suffixes explicitly because that would take both quadratic time and space.
 * <p/>
 * Implement the CircularSuffixArray. Be sure not to create explicit copies of the string (e.g., via the substring()
 * method in Java's String data type) when you sort the suffixes. That would take quadratic space. Instead
 * for each suffix, you only need to keep an index that indicates which character is the beginning of the suffix.
 * This way you can build the N suffixes in linear time and space. Then sort this array of indices. It's just
 * like sorting an array of references.
 */
public class CircularSuffixArray {

    private int N;
    private char[] s;
    private Suffix[] suffixes;

    /**
     * Circular suffix array of s
     */
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.NullPointerException("Empty string");

        this.N = s.length();
        this.s = s.toCharArray();
        suffixes = new Suffix[s.length()];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new Suffix(i);
        }

        Arrays.sort(suffixes);
    }

    private class Suffix implements Comparable<Suffix> {

        private int index;

        public Suffix(int index) {
            this.index = index;
        }

        /**
         * @param that That
         * @return int
         */
        public int compareTo(Suffix that) {
            int i, j;
            for (int n = 0; n < N; n++) {
                i = (this.index + n) % N;
                j = (that.index + n) % N;
                if (s[i] < s[j]) return -1;
                else if (s[i] > s[j]) return 1;
            }
            return 0;
        }

        public int getIndex() {
            return this.index;
        }
    }

    /**
     * Length of s
     */
    public int length() {
        return N;
    }

    /**
     * Returns index of ith sorted suffix
     */
    public int index(int i) {
        if (i < 0 || i > N) throw new java.lang.IndexOutOfBoundsException("Out of bounds");
        return suffixes[i].getIndex();
    }

    /**
     * Unit testing of the methods (optional)
     */
    public static void main(String[] args) {
        String s = "ABRACADABRA";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        System.out.printf("%d\n", csa.length());
        for (int i = 0; i < s.length(); i++)
            System.out.printf("%d\n", csa.index(i));

    }
}
