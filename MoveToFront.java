/**
 * Move-to-front encoding and decoding. The main idea of move-to-front encoding
 * is to maintain an ordered sequence of the characters in the alphabet,
 * and repeatedly read in a character from the input message, print out the position
 * in which that character appears, and move that character to the front of the sequence.
 * As a simple example, if the initial ordering over a 6-character alphabet is A B C D E F,
 * and we want to encode the input CAAABCCCACCF, then we would update the move-to-front
 * sequences as follows:
 * <p/>
 * move-to-front    in   out
 * -------------    ---  ---
 * A B C D E F      C    2
 * C A B D E F      A    1
 * A C B D E F      A    0
 * A C B D E F      A    0
 * A C B D E F      B    2
 * B A C D E F      C    2
 * C B A D E F      C    0
 * C B A D E F      C    0
 * C B A D E F      A    2
 * A C B D E F      C    1
 * C A B D E F      C    0
 * C A B D E F      F    5
 * F C A B D E
 * <p/>
 * If the same character occurs next to each other many times in the input, then many of the
 * output values will be small integers, such as 0, 1, and 2. The extremely high frequency
 * of certain characters makes an ideal scenario for Huffman coding.
 * <p/>
 * <p/>
 * MoveToFront
 * <p/>
 * Performance requirements. The running time of move-to-front encoding and decoding should be
 * proportional to R N (or better) in the worst case and proportional to N + R (or better) in
 * practice on inputs that arise when compressing typical English text, where N is the number
 * of characters in the input and R is the alphabet size.
 */
public class MoveToFront {

    // alphabet size of extended ASCII
    private static final int R = 256;

    /**
     * Apply move-to-front decoding, reading from standard input and writing to standard output
     * <p/>
     * Move-to-front encoding. Your task is to maintain an ordered sequence of the 256 extended
     * ASCII characters.
     * Initialize the sequence by making the ith character in the sequence equal to the ith extended
     * ASCII character. Now, read in each 8-bit character c from standard input one at a time, output
     * the 8-bit index in the sequence where c appears, and move c to the front.
     * % java MoveToFront - < abra.txt | java HexDump 16
     * 41 42 52 02 44 01 45 01 04 04 02 26
     * 96 bits
     */
    public static void encode() {
        char[] alpha = new char[R];
        for (char i = 0; i < R; i++)
            alpha[i] = i;

        char i, l;

        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        i = input[0];
        System.arraycopy(alpha, 0, alpha, 1, i);
        alpha[0] = i;
        BinaryStdOut.write(i, 8);

        for (l = 1; l < input.length; l++) {
            for (i = 0; i < alpha.length; i++)
                if (alpha[i] == input[l]) {
                    BinaryStdOut.write(i, 8);
                    //System.out.println(i & 0xFF);
                    System.arraycopy(alpha, 0, alpha, 1, i);
                    alpha[0] = alpha[i];
                }

        }

        BinaryStdOut.close();

    }

    /**
     * Apply move-to-front encoding, reading from standard input and writing to standard output
     * <p/>
     * Move-to-front decoding. Initialize an ordered sequence of 256 characters, where extended ASCII
     * character i appears ith in the sequence. Now, read in each 8-bit character i (but treat it as
     * an integer between 0 and 255) from standard input one at a time, write the ith character in the
     * sequence, and move that character to the front.
     * Check that the decoder recovers any encoded message.
     * <p/>
     * % java MoveToFront - < abra.txt | java MoveToFront +
     * ABRACADABRA!
     */
    public static void decode() {
        char[] alpha = new char[R];
        for (char i = 0; i < R; i++)
            alpha[i] = i;

        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        char i;
        for (i = 0; i < input.length; i++) {
            BinaryStdOut.write(alpha[input[i]], 8);
            //StdOut.printf("%02x\n", alpha[input[i]] & 0xff);
            System.arraycopy(alpha, 0, alpha, 1, i);
            alpha[0] = alpha[input[i]];
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
