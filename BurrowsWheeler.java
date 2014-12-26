
/**
 * Burrows-Wheeler encoding
 *
 * Burrows-Wheeler transform. The goal of the Burrows-Wheeler transform is not to compress a message,
 * but rather to transform it into a form that is more amenable to compression.
 * The transform rearranges the characters in the input so that there are lots of clusters with
 * repeated characters, but in such a way that it is still possible to recover the original input.
 *
 * It relies on the following intuition: if you see the letters hen in English text, then most of the
 * time the letter preceding it is t or w. If you could somehow group all such preceding letters together
 * (mostly t's and some w's), then you would have an easy opportunity for data compression.

 Burrows-Wheeler encoding.
 The Burrows-Wheeler transform of a string s of length N is defined as follows: Consider the result of
 sorting the N circular suffixes of s.
 The Burrows-Wheeler transform is the last column in the sorted suffixes array t[], preceded by the row
 number first in which the original string ends up.
 Continuing with the "ABRACADABRA!" example above, we highlight the two components of the Burrows-Wheeler
 transform in the table below.

 i     Original Suffixes          Sorted Suffixes       t    index[i]
 --    -----------------------     -----------------------    --------
 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
 *3   A C A D A B R A ! A B R     A B R A C A D A B R A !   *0
 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
 10   A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
 11   ! A B R A C A D A B R A     R A C A D A B R A ! A B    2

 Since the original string ABRACADABRA! ends up in row 3, we have first = 3. Thus, the Burrows-Wheeler transform is
 3
 ARD!RCAAAABB
 Notice how there are 4 consecutive As and 2 consecutive Bs—these clusters make the message easier to compress.

 % java BurrowsWheeler - < abra.txt | java HexDump 16
 00 00 00 03 41 52 44 21 52 43 41 41 41 41 42 42
 128 bits

 Also, note that the integer 3 is represented using 4 bytes (00 00 00 03). The character 'A' is represented by hex 41, the character 'R' by 52, and so forth.


 Burrows-Wheeler decoder.
 Now, we describe how to invert the Burrows-Wheeler transform and recover the original
 input string. If the jth original suffix (original string, shifted j characters to the left) is the ith row in
 the sorted order, we define next[i] to be the row in the sorted order where the (j + 1)st original suffix appears.

 For example, if first is the row in which the original input string appears, then next[first] is the row in the
 sorted order where the 1st original suffix (the original string left-shifted by 1) appears; next[next[first]]
 is the row in the sorted order where the 2nd original suffix appears; next[next[next[first]]] is the row where
 the 3rd original suffix appears; and so forth.

 Decoding the message given t[], first, and the next[] array.
 The input to the Burrows-Wheeler decoder is the last column t[] of the sorted suffixes along with first.
 From t[], we can deduce the first column of the sorted suffixes because it consists of precisely the same characters,
 but in sorted order.

 i      Sorted Suffixes     t      next
 --    -----------------------      ----
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R        0
 2    A ? ? ? ? ? ? ? ? ? ? D        6
 *3   A ? ? ? ? ? ? ? ? ? ? !        7
 4    A ? ? ? ? ? ? ? ? ? ? R        8
 5    A ? ? ? ? ? ? ? ? ? ? C        9
 6    B ? ? ? ? ? ? ? ? ? ? A       10
 7    B ? ? ? ? ? ? ? ? ? ? A       11
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
 10   R ? ? ? ? ? ? ? ? ? ? B        1
 11   R ? ? ? ? ? ? ? ? ? ? B        4

 Now, given the next[] array and first, we can reconstruct the original input string because the first character
 of the ith original suffix is the ith character in the input string.

 In the example above, since first = 3, we
 know that the original input string appears in row 3; thus, the original input string starts with 'A' (and ends with '!').

 Since next[first] = 7, the next original suffix appears in row 7; thus, the next character in the original
 input string is 'B'. Since next[next[first]] = 11, the next original suffix appears in row 11; thus, the next character
 in the original input string is 'R'.

 Constructing the next[] array from t[] and first.
 Amazingly, the information contained in the Burrows-Wheeler transform suffices to reconstruct the next[] array,
 and, hence, the original message!
 Here's how. It is easy to deduce a next[] value for a character that appears exactly once in the input string.
 For example, consider the suffix that starts with 'C'.
 By inspecting the first column, it appears 8th in the sorted order. The next original suffix after this one will
 have the character 'C' as its last character. By inspecting the last column, the next original appears 5th in the sorted order.
 Thus, next[8] = 5.

 Similarly, 'D' and '!' each occur only once, so we can deduce that next[9] = 2 and next[0] = 3.
 i      Sorted Suffixes     t      next
 --    -----------------------      ----
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R
 2    A ? ? ? ? ? ? ? ? ? ? D
 *3   A ? ? ? ? ? ? ? ? ? ? !
 4    A ? ? ? ? ? ? ? ? ? ? R
 5    A ? ? ? ? ? ? ? ? ? ? C
 6    B ? ? ? ? ? ? ? ? ? ? A
 7    B ? ? ? ? ? ? ? ? ? ? A
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
 10   R ? ? ? ? ? ? ? ? ? ? B
 11   R ? ? ? ? ? ? ? ? ? ? B
 However, since 'R' appears twice, it may seem ambiguous whether next[10] = 1 and next[11] = 4, or whether next[10] = 4 and next[11] = 1. Here's the key rule that resolves the apparent ambiguity:
 If sorted row i and j both start with the same character and i < j, then next[i] < next[j].
 This rule implies next[10] = 1 and next[11] = 4. Why is this rule valid? The rows are sorted so row 10 is lexicographically less than row 11. Thus the ten unknown characters in row 10 must be less than the ten unknown characters in row 11 (since both start with 'R'). We also know that between the two rows that end with 'R', row 1 is less than row 4. But, the ten unknown characters in row 10 and 11 are precisely the first ten characters in rows 1 and 4. Thus, next[10] = 1 and next[11] = 4 or this would contradict the fact that the suffixes are sorted.
 Check that the decoder recovers any encoded message.

 % java BurrowsWheeler - < abra.txt | java BurrowsWheeler +
 ABRACADABRA!
 Name your program BurrowsWheeler.java and organize it using the following API:
 public class BurrowsWheeler {
 // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
 public static void encode()

 // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
 public static void decode()

 // if args[0] is '-', apply Burrows-Wheeler encoding
 // if args[0] is '+', apply Burrows-Wheeler decoding
 public static void main(String[] args)
 }
 Performance requirements. The running time of your Burrows-Wheeler encoder should be proportional to N + R (or better) in the worst case, excluding the time to construct the circular suffix array. The running time of your Burrows-Wheeler decoder should be proportional to N + R (or better) in the worst case.
 *
 * Performance requirements.
 * The running time of your Burrows-Wheeler encoder should be proportional to N + R (or better) in the worst case,
 * excluding the time to construct the circular suffix array. The running time of your Burrows-Wheeler decoder
 * should be proportional to N + R (or better) in the worst case.
 */
public class BurrowsWheeler {

    // alphabet size of extended ASCII
    private static final int R = 256;

    /**
     * Apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
     */
    public static void encode()
    {
        // read the input
        String s = BinaryStdIn.readString();
        int N = s.length();
        CircularSuffixArray suffix = new CircularSuffixArray(s);

        for(int i = 0; i < suffix.length(); i++)
            if(suffix.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }

        for(int i = 0; i < suffix.length(); i++) {
            BinaryStdOut.write(s.charAt((suffix.index(i) + N - 1) % N));
        }

        BinaryStdOut.close();
    }

    /**
     * Apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
     */
    public static void decode()
    {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int N = s.length();
        char[] input = s.toCharArray();
        // If the jth original suffix (original string, shifted j characters to the left) is the ith row in
        // the sorted order, we define next[i] to be the row in the sorted order where the (j + 1)st original suffix appears.
        int[] next = new int[N];

        char[] firstColumn = new char[N];

        int[] count = new int[R + 1];

        for (int i = 0;  i < N; i++)
            count[input[i] + 1]++; // check if it works

        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        for(int i = 0; i < N; i++) {
            int index = count[input[i]];
            firstColumn[index] = input[i];
            next[index] = i;
            count[input[i]]++;
        }

        int step = first;
        for(int i = 0; i < N; i++) {
            BinaryStdOut.write(firstColumn[step]);
            step = next[step];
        }

        BinaryStdOut.close();
    }

    /**
     * If args[0] is '-', apply Burrows-Wheeler encoding
     * If args[0] is '+', apply Burrows-Wheeler decoding
     */
    public static void main(String[] args)
    {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}