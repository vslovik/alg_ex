import java.awt.Color;

/**
 * The data type may not mutate the Picture argument to the constructor.
 * <p/>
 * Performance requirements. The width(), height(), and energy() methods
 * should take constant time in the worst case. All other methods should
 * run in time at most proportional to W H in the worst case.
 * <p/>
 * For faster performance, do not construct explicit DirectedEdge and
 * EdgeWeightedDigraph objects.
 * <p/>
 * Challenge for the bored:
 * 1. Your energy() method implemented the dual gradient energy function. Try out other energy functions.
 * 2. Implement an interactive object-removal feature: The user highlights an area of the image, and that
 * portion of the image is forced to zero energy. Rows and columns are then successively removed until
 * every pixel in that zero-energy region has been removed.
 */
public class SeamCarver {

    /**
     * Image to resize
     */
//    private Picture picture;
    private byte colors[][][];
    private int[][] energy;
    private boolean transposed = false;
    private int w;
    private int h;

    /**
     * Create a seam carver object based on the given picture
     *
     * @param picture Picture
     */
    public SeamCarver(Picture picture) {
        long sm = usedMem();
        colors = new byte[picture.height()][picture.width()][3];
        energy = new int[picture.height()][picture.width()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                colors[y][x][0] = (byte) picture.get(x, y).getRed();
                colors[y][x][1] = (byte) picture.get(x, y).getGreen();
                colors[y][x][2] = (byte) picture.get(x, y).getBlue();
                energy[y][x] = Integer.MAX_VALUE;
            }
        }
        w = colors[0].length;
        h = colors.length;
        System.out.printf("constructor - mem: (%s)\n", usedMem() - sm);
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * Current picture
     *
     * @return Picture
     */
    public Picture picture() {

        compress();

        Picture picture = new Picture(width(), height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                picture.set(x, y, new Color(colors[y][x][0] & 0xFF, colors[y][x][1] & 0xFF, colors[y][x][2] & 0xFF));
            }
        }

        return picture;
    }

    /**
     * Width of current picture
     *
     * @return int
     */
    public int width() {
        compress();
        return w;
    }

    /**
     * Height of current picture
     *
     * @return int
     */
    public int height() {
        compress();
        return h;
    }


    /**
     * Energy of pixel at column x and row y
     * <p/>
     * Computing the energy of a pixel.
     * <p/>
     * We will use the dual gradient energy function:
     * The energy of pixel (x, y) is Δx2(x, y) + Δy2(x, y),
     * <p/>
     * where the square of the x-gradient
     * Δx2(x, y) = Rx(x, y)2 + Gx(x, y)2 + Bx(x, y)2,
     * <p/>
     * and where the central differences Rx(x, y), Gx(x, y), and Bx(x, y)
     * are the absolute value in differences of red, green, and blue components
     * between pixel (x + 1, y) and pixel (x − 1, y).
     * <p/>
     * The square of the y-gradient Δy2(x, y) is defined in an analogous manner.
     * <p/>
     * We define the energy of pixels at the border of the image to be 2552 + 2552 + 2552 = 195075.
     * <p/>
     * As an example,
     * consider the 3-by-4 image with RGB values (each component is an integer between 0 and 255)
     * as shown in the table below.
     * <p/>
     * (255, 101, 51) (255, 101, 153) (255, 101, 255)
     * (255,153,51)   (255,153,153)  (255,153,255)
     * (255,203,51)   (255,204,153)  (255,205,255)
     * (255,255,51)   (255,255,153)  (255,255,255)
     * <p/>
     * The ten border pixels have energy 195075. Only the pixels (1, 1) and (1, 2) are nontrivial.
     * We calculate the energy of pixel (1, 2) in detail:
     * <p/>
     * Rx(1, 2) = 255 − 255 = 0,
     * Gx(1, 2) = 205 − 203 = 2,
     * Bx(1, 2) = 255 − 51 = 204,
     * yielding Δx2(1, 2) = 22 + 2042 = 41620.
     * <p/>
     * Ry(1, 2) = 255 − 255 = 0,
     * Gy(1, 2) = 255 − 153 = 102,
     * By(1, 2) = 153 − 153 = 0,
     * yielding Δy2(1, 2) = 1022 = 10404.
     * <p/>
     * Thus, the energy of pixel (1, 2) is 41620 + 10404 = 52024. Similarly, the energy of pixel (1, 1) is 2042 + 1032 = 52225.
     * <p/>
     * 195075.0 	 195075.0 	 195075.0
     * 195075.0 	 52225.0 	 195075.0
     * 195075.0 	 52024.0 	 195075.0
     * 195075.0 	 195075.0 	 195075.0
     * <p/>
     * Calculating Δx2 and Δy2 are very similar.
     * Using two private methods will keep your code simple.
     * To test that your code works, use the client PrintEnergy described in the testing section above.
     * <p/>
     * Optimization: Avoid recomputing the parts of the energy matrix that don't change.
     * Optimization: Is it faster to traverse the energy matrix in row-major order or column-major order?
     * Hint: Recall that in Java a "2D array" is really an array of arrays.
     * Optimization. Consider using System.arraycopy() to shift elements within an array.
     *
     * @param x double
     * @param y int
     * @return int
     */
    public double energy(int x, int y) {
        compress();

        if (transposed) {
            int tmp = x;
            x = y;
            y = tmp;
        }

        if (x < 0 || x >= colors[0].length || y < 0 || y >= colors.length)
            throw new java.lang.IndexOutOfBoundsException("Pixel coordinates out of range");

        if (x == 0 || x == colors[0].length - 1 || y == 0 || y == colors.length - 1)
            return 195075;


        if (energy[y][x] < Integer.MAX_VALUE)
            return energy[y][x];

        energy[y][x] = getDeltaX2(x, y) + getDeltaY2(x, y);

        return energy[y][x];
    }

    /**
     * @param x int
     * @param y int
     * @return int
     */
    private int getDeltaX2(int x, int y) {
        int deltaX2 = 0;
        deltaX2 += Math.pow(Math.abs((colors[y][x + 1][0] & 0xFF) - (colors[y][x - 1][0] & 0xFF)), 2.0);
        deltaX2 += Math.pow(Math.abs((colors[y][x + 1][1] & 0xFF) - (colors[y][x - 1][1] & 0xFF)), 2.0);
        deltaX2 += Math.pow(Math.abs((colors[y][x + 1][2] & 0xFF) - (colors[y][x - 1][2] & 0xFF)), 2.0);
        return deltaX2;
    }

    /**
     * @param x int
     * @param y int
     * @return int
     */
    private int getDeltaY2(int x, int y) {
        int deltaY2 = 0;
        deltaY2 += Math.pow(Math.abs((colors[y + 1][x][0] & 0xFF) - (colors[y - 1][x][0] & 0xFF)), 2.0);
        deltaY2 += Math.pow(Math.abs((colors[y + 1][x][1] & 0xFF) - (colors[y - 1][x][1] & 0xFF)), 2.0);
        deltaY2 += Math.pow(Math.abs((colors[y + 1][x][2] & 0xFF) - (colors[y - 1][x][2] & 0xFF)), 2.0);

        return deltaY2;
    }

    /**
     * Sequence of indices for horizontal seam
     * <p/>
     * Returns an array of length H such that entry x is the column number of the pixel to be removed from row x of the image.
     * For example, consider the 6-by-5 image below
     * ( 97, 82,107) 	 (220,172,141) 	 (243, 71,205) 	 (129,173,222) 	 (225, 40,209) 	 ( 66,109,219)
     * (181, 78, 68) 	 ( 15, 28,216) 	 (245,150,150) 	 (177,100,167) 	 (205,205,177) 	 (147, 58, 99)
     * (196,224, 21) 	 (166,217,190) 	 (128,120,162) 	 (104, 59,110) 	 ( 49,148,137) 	 (192,101, 89)
     * ( 83,143,103) 	 (110, 79,247) 	 (106, 71,174) 	 ( 92,240,205) 	 (129, 56,146) 	 (121,111,147)
     * ( 82,157,137) 	 ( 92,110,129) 	 (183,107, 80) 	 ( 89, 24,217) 	 (207, 69, 32) 	 (156,112, 31)
     * <p/>
     * The corresponding pixel energies are shown below, with a minimum energy vertical seam highlighted in red.
     * <p/>
     * <p/>
     * 195075.0	 195075.0  *195075.0 195075.0  195075.0  195075.0
     * 195075.0	 23346.0   51304.0	 *31519.0   55112.0	 195075.0
     * 195075.0	 47908.0   61346.0	 *35919.0   38887.0	 195075.0
     * 195075.0	 31400.0   37927.0	 *14437.0   63076.0	 195075.0
     * 195075.0	 195075.0  *195075.0 195075.0  195075.0  195075.0
     * <p/>
     * <p/>
     * In this case, the method findVerticalSeam() should return the array { 2, 3, 3, 3, 2 }.
     * <p/>
     * When there are multiple vertical seams with minimal total energy (as in the example above),
     * your method can return any such seam.
     * <p/>
     * To write findVerticalSeam(), you will want to first make sure you understand the topologial sort algorithm
     * for computing a shortest path in a DAG.
     * <p/>
     * Do not create an EdgeWeightedDigraph.
     * Instead construct a 2d energy array using the energy() method that you have already written.
     * <p/>
     * Your algorithm can traverse this matrix treating some select entries as reachable from (x, y)
     * to calculate where the seam is located.
     * <p/>
     * To test that your code works, use the client PrintSeams described in the testing section above.
     *
     * @return int[]
     */
    public int[] findVerticalSeam() {
        compress();

        int x, y, end;

        int W = transposed ? colors.length : colors[0].length;
        int H = transposed ? colors[0].length : colors.length;

        int[] minSeam = new int[H];
        minSeam[H - 1] = 0;

        int[][] e = new int[H][W];
        byte[][] to = new byte[H][W];

        for (y = 0; y < H; y++)
            for (x = 0; x < W; x++) {
                if (y == 0)
                    e[y][x] = (int) energy(x, y);
                else
                    e[y][x] = Integer.MAX_VALUE;
            }


        Topological topological = new Topological(W, H);
        for (Integer N : topological.order()) {
            x = N % W;
            y = N/W;

            if (e[y][x] != Integer.MAX_VALUE && y + 1 < H) {
                for (int k = x - 1; k <= x + 1; k++)
                    if (k >= 0 && k < W && e[y][x] + energy(k, y + 1) < e[y + 1][k]) {
                        e[y + 1][k] = e[y][x] + (int) energy(k, y + 1);
                        to[y][k] = (byte) (x - k);
                    }
            }

            if (y == H - 1) {
                if (e[H - 1][x] < e[H - 1][minSeam[H - 1]]) {
                    minSeam[H - 1] = x;
                }
            }

        }

        for (y = H - 2; y >= 0; y--) {
            end = minSeam[y + 1];
            minSeam[y] = to[y][end] + end;
        }

        return minSeam;
    }

    /**
     * Sequence of indices for vertical seam
     * <p/>
     * To write findHorizontalSeam(), transpose the image,
     * call findVerticalSeam(), and transpose it back.
     * <p/>
     * Optimization. Don't explicitly transpose the Picture until you need to do so.
     * For example, if you perform a sequence of 50 consecutive horizontal seam removals,
     * you should need only two transposes instead of 100.
     *
     * @return int[]
     */
    public int[] findHorizontalSeam() {
        transposed = true;

        int[] seam = findVerticalSeam();

        transposed = false;

        return seam;
    }

    /**
     * Remove horizontal seam from current picture
     * <p/>
     * To write removeHorizontalSeam(), transpose the image, call removeVerticalSeam(), and transpose it back.
     * <p/>
     * Optimization: There is no need to create a new Picture object after removing a seam —instead,
     * you can maintain the color information in a 2D array of short integers (or a 3D array of bytes).
     * That is, you can defer creating a Picture object until required to do so because of a call to picture().
     * Since Picture objects are relatively expensive, this will speed things up.
     *
     * @param seam int[]
     */
    public void removeHorizontalSeam(int[] seam) {

        transposed = true;

        removeVerticalSeam(seam);

        transposed = false;
    }

    /**
     * Remove vertical seam from current picture
     * <p/>
     * Typically, this method will be called with the output of findVerticalSeam(),
     * but be sure that they work for any seam.
     * To test that your code words, use the client ResizeDemo described in the testing section above.
     *
     * @param seam int[]
     */
    public void removeVerticalSeam(int[] seam) {

        if (transposed) {
            compressV();
            h -= 1;
        } else {
            compressH();
            w -= 1;
        }

        int W = transposed ? colors.length : colors[0].length;
        int H = transposed ? colors[0].length : colors.length;

        if (seam == null)
            throw new java.lang.NullPointerException("Seam is null");
        if (W <= 1 || H <= 1)
            throw new java.lang.IllegalArgumentException("Degenerate image dimensions");

        if (!isValid(seam))
            throw new java.lang.IllegalArgumentException("Invalid seam");

        for (int y = 0; y < H; y++) {
            int x = seam[y];
            if (transposed) {
                energy[x][y] = Integer.MIN_VALUE;
            } else {
                energy[y][x] = Integer.MIN_VALUE;
            }
        }
    }

    /**
     * Finalize seam removal processing
     */
    private void compress() {
        compressV();
        compressH();
    }

    private void compressV() {
        if (w < colors[0].length) {
            shift();
            w = colors[0].length;
        }
    }

    private void compressH() {
        if (h < colors.length) {
            transpose();
            shift();
            transpose();
            h = colors.length;
        }
    }

    /**
     * Finds segment to shift
     */
    private void shift() {
        long sm = usedMem();
        int W = colors[0].length;
        int H = colors.length;

        int a = -1, step, cells = 0;
        for (int y = 0; y < H; y++) {
            step = 0;
            a = -1;
            for (int x = 0; x < W; x++) {
                if (a == -1 && energy[y][x - step] == Integer.MIN_VALUE)
                    a = x;
                else if (a >= 0 && energy[y][x - step] != Integer.MIN_VALUE) {
                    shift(y, a - step, x - step);
                    step += x - a;
                    a = -1;
                }
            }

            if (y == 0)
                cells = W - step;
            cut(y, cells);
        }
        System.out.printf("shift - mem: (%s)\n", usedMem() - sm);
    }

    /**
     * Cuts shifted array
     *
     * @param y int
     */
    private void cut(int y, int length) {
        byte[][] c = new byte[length][3];
        System.arraycopy(colors[y], 0, c, 0, length);
        colors[y] = c;

        int[] e = new int[length];
        System.arraycopy(energy[y], 0, e, 0, length);
        energy[y] = e;
    }

    /**
     * Makes sequential shifts
     *
     * @param y int
     * @param a int
     * @param b int
     */
    private void shift(int y, int a, int b) {
        if (a - 1 > 0)
            energy[y][a - 1] = Integer.MAX_VALUE;
        if (b + 1 < colors[0].length)
            energy[y][b + 1] = Integer.MAX_VALUE;
        System.arraycopy(colors[y], b, colors[y], a, colors[y].length - b);
        System.arraycopy(energy[y], b, energy[y], a, energy[y].length - b);
    }

    /**
     * Transposes matrices
     */
    private void transpose() {
        long sm = usedMem();
        int W = colors[0].length;
        int H = colors.length;

        byte[][][] tmpC = new byte[W][H][3];
        int[][] tmpE = new int[W][H];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                tmpC[x][y] = colors[y][x];
                tmpE[x][y] = energy[y][x];
            }
        }

        colors = tmpC;
        energy = tmpE;
        System.out.printf("transpose - mem: (%s)\n", usedMem() - sm);
    }

    /**
     * Is seam valid
     *
     * @param seam int[]
     * @return boolean
     */
    private boolean isValid(int[] seam) {

        int H = transposed ? colors[0].length : colors.length;

        if (seam.length != H)
            return false;

        for (int i = 0; i < seam.length; i++) {
            if (i - 1 > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                return false;
        }
        return true;
    }


    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage:\njava ResizeDemo [image filename] ");
            return;
        }

        Stopwatch sw = new Stopwatch();

        Picture inputImg = new Picture(args[0]);

        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);

        int[] verticalSeam = sc.findVerticalSeam();
        sc.removeVerticalSeam(verticalSeam);

        int[] horizontalSeam = sc.findHorizontalSeam();
        sc.removeHorizontalSeam(horizontalSeam);


        int W = sc.picture().width();
        int H = sc.picture().height();

        int num = 2;

        int[][] seamsh = new int[num][H];
        boolean[][] marked = new boolean[W][H];
        if (H > num) {
            for (int i = 0; i < num; i++)
                seamsh[i] = sc.generateSeam(H, W, marked);

            for (int i = 0; i < num; i++)
                sc.removeHorizontalSeam(seamsh[i]);
        }

        W = sc.picture().width();
        H = sc.picture().height();

        int[][] seamsv = new int[num][W];
        marked = new boolean[H][W];
        if (W > num) {
            for (int i = 0; i < num; i++)
                seamsv[i] = sc.generateSeam(W, H, marked);

            for (int i = 0; i < num; i++)
                sc.removeVerticalSeam(seamsv[i]);
        }


        Picture outputImg = sc.picture();

        System.out.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

        System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        inputImg.show();
        outputImg.show();
    }

    private int[] generateSeam(int w, int h, boolean[][] marked) {
        compress();
        int val, shift;
        int[] seam = new int[h];
        do {
            val = (int) (w * Math.random());
            seam[0] = val;
        } while (marked[0][val]);
        marked[0][val] = true;
        for (int i = 1; i < h; i++) {
            do {
                shift = (int) (3 * Math.random()) - 1;
                val = Math.min(w - 1, Math.max(0, seam[i - 1] + shift));
            } while (marked[i][val]);
            seam[i] = val;
            marked[i][val] = true;
        }

        return seam;
    }
}
