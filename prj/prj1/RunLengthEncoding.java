/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes
 *  a PixImage object.  Descriptions of the methods you must implement appear
 *  below.  They include constructors of the form
 *
 *      public RunLengthEncoding(int width, int height);
 *      public RunLengthEncoding(int width, int height, int[] red, int[] green,
 *                               int[] blue, int[] runLengths) {
 *      public RunLengthEncoding(PixImage image) {
 *
 *  that create a run-length encoding of a PixImage having the specified width
 *  and height.
 *
 *  The first constructor creates a run-length encoding of a PixImage in which
 *  every pixel is black.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts a PixImage object into a run-length encoding of that image.
 *
 *  See the README file accompanying this project for additional details.
 */

import java.util.Iterator;

public class RunLengthEncoding implements Iterable {

  /**
   * Define any variables associated with a RunLengthEncoding object here.
   * These variables MUST be private.
   */
  private PixImage rle_image;
  private RunList runs;


  /**
   *  The following methods are required for Part II.
   */

  /**
   * RunLengthEncoding() (with two parameters) constructs a run-length
   * encoding of a black PixImage of the specified width and height, in which
   * every pixel has red, green, and blue intensities of zero.
   *
   * @param width  the width of the image.
   * @param height the height of the image.
   */

  public RunLengthEncoding(int width, int height) {
    // Your solution here.
    rle_image = new PixImage(width, height);
    runs = new RunList();
  }

  /**
   * RunLengthEncoding() (with six parameters) constructs a run-length
   * encoding of a PixImage of the specified width and height.  The runs of
   * the run-length encoding are taken from four input arrays of equal length.
   * Run i has length runLengths[i] and RGB intensities red[i], green[i], and
   * blue[i].
   *
   * @param width      the width of the image.
   * @param height     the height of the image.
   * @param red        is an array that specifies the red intensity of each run.
   * @param green      is an array that specifies the green intensity of each run.
   * @param blue       is an array that specifies the blue intensity of each run.
   * @param runLengths is an array that specifies the length of each run.
   *                   <p>
   *                   NOTE:  All four input arrays should have the same length (not zero).
   *                   All pixel intensities in the first three arrays should be in the range
   *                   0...255.  The sum of all the elements of the runLengths array should be
   *                   width * height.  (Feel free to quit with an error message if any of these
   *                   conditions are not met--though we won't be testing that.)
   */

  public RunLengthEncoding(int width, int height, int[] red, int[] green,
                           int[] blue, int[] runLengths) {
    // Your solution here.
    boolean errFlag = false;
    int sum = 0;
    if (!(red.length > 0 && red.length == green.length &&
            red.length == blue.length && red.length == runLengths.length)) errFlag = true;

    if (!errFlag) {
      for (int i = 0; i < red.length; i++) {
        if (!(isValidColor(red[i]) && isValidColor(green[i]) && isValidColor(blue[i]))) {
          errFlag = true;
          break;
        }
        sum += runLengths[i];
      }
    }

    if (errFlag && sum != width * height) {
      System.out.println("Input parameter inaccurate...");
      System.exit(1);
    }

    rle_image = new PixImage(width, height);
    // create a doubly-linked list to store "run"s.
    runs = new RunList();
    for (int i = 0; i < red.length; i++) {
      runs.insertEnd(new int[]{runLengths[i], red[i], green[i], blue[i]});
    }

  }

  /**
   * getWidth() returns the width of the image that this run-length encoding
   * represents.
   *
   * @return the width of the image that this run-length encoding represents.
   */

  public int getWidth() {
    // Replace the following line with your solution.
    return rle_image.getWidth();
  }

  /**
   * getHeight() returns the height of the image that this run-length encoding
   * represents.
   *
   * @return the height of the image that this run-length encoding represents.
   */
  public int getHeight() {
    // Replace the following line with your solution.
    return rle_image.getHeight();
  }

  /**
   * iterator() returns a newly created RunIterator that can iterate through
   * the runs of this RunLengthEncoding.
   *
   * @return a newly created RunIterator object set to the first run of this
   * RunLengthEncoding.
   */
  public RunIterator iterator() {
    // Replace the following line with your solution.
    return new RunIterator(runs);
    // You'll want to construct a new RunIterator, but first you'll need to
    // write a constructor in the RunIterator class.
  }

  /**
   * toPixImage() converts a run-length encoding of an image into a PixImage
   * object.
   *
   * @return the PixImage that this RunLengthEncoding encodes.
   */
  public PixImage toPixImage() {
    // Replace the following line with your solution.
    int width = getWidth();
    int height = getHeight();
    PixImage img = new PixImage(width, height);
    int x = 0; // initialize x coordinate
    int y = 0; // initialize y coordinate
    for (Object oneRun : this) {
      int[] item = (int[]) oneRun;
      int runLength = (int) item[0];
      short red = (short) item[1];
      short green = (short) item[2];
      short blue = (short) item[3];
      for (int i = 0; i < runLength; i++) {
        img.setPixel(x, y, red, green, blue);
        x++;
        if (x >= width) {
          x = x % width;
          y++;
        }
      }
    }
    return img;
  }

  /**
   * toString() returns a String representation of this RunLengthEncoding.
   * <p>
   * This method isn't required, but it should be very useful to you when
   * you're debugging your code.  It's up to you how you represent
   * a RunLengthEncoding as a String.
   *
   * @return a String representation of this RunLengthEncoding.
   */
  public String toString() {
    // Replace the following line with your solution.
    String s = "The rle_image is: \n";
    s += rle_image.toString();
    s += "\n The RunList is: \n";
    s += runs.toString();
    return s;
  }


  /**
   *  The following methods are required for Part III.
   */

  /**
   * RunLengthEncoding() (with one parameter) is a constructor that creates
   * a run-length encoding of a specified PixImage.
   * <p>
   * Note that you must encode the image in row-major format, i.e., the second
   * pixel should be (1, 0) and not (0, 1).
   *
   * @param image is the PixImage to run-length encode.
   */
  public RunLengthEncoding(PixImage image) {
    // Your solution here, but you should probably leave the following line
    // at the end.
    int w = image.getWidth();
    int h = image.getHeight();
    rle_image = new PixImage(w, h);
    runs = new RunList();
    int sx = 0;
    int sy = 0;
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        if (isDifferentColor(image, sx, sy, x, y)) {
          runs.insertEnd(new int[]{(x - sx + w * (y - sy)), image.getRed(sx, sy),
                  image.getGreen(sx, sy), image.getBlue(sx, sy)});
          sx = x;
          sy = y;
        }
      }
    }
    // when for-loop terminates, the last "run" (from (sx, sy) to (w-1, h-1) inclusive)
    // has not been added to the RunList, thus we need to insert it.
    runs.insertEnd(new int[]{(w - sx + w * (h - 1 - sy)), image.getRed(sx, sy),
            image.getGreen(sx, sy), image.getBlue(sx, sy)});
    check();
  }

  /**
   * check() walks through the run-length encoding and prints an error message
   * if two consecutive runs have the same RGB intensities, or if the sum of
   * all run lengths does not equal the number of pixels in the image.
   */
  public void check() {
    // Your solution here.
    boolean errFlag = false;
    int count = 0;
    int[] prevRun = new int[4]; // store the previous "run" info
    int[] currentRun; // store the current "run" info
    boolean isHead = true;
    for (Object run : this) {
      // omit the "sentinel" node, initialize prevRun;
      if (isHead) {
        prevRun = (int[]) run;
        isHead = false;
        continue;
      }
      currentRun = (int[]) run;
      if (prevRun.equals(currentRun) || currentRun[0] < 1) {
        errFlag = true;
        break;
      }
      count += currentRun[0];
      prevRun = currentRun;
    }
    if (errFlag) {
      System.out.println("ERROR: two consecutive runs have the same RGB intensities\n" + "or a run has a length less than 1.\n");
      System.exit(1);
    } else if (getWidth() * getHeight() != count) {
      System.out.println("ERROR: the sum of all run lengths does not equal the number of pixels in the image.\n" +
              "count = " + count + " width * height = " + getWidth() * getHeight());
      System.exit(1);
    }
  }

  /**
   *  The following method is required for Part IV.
   */

  /**
   * setPixel() modifies this run-length encoding so that the specified color
   * is stored at the given (x, y) coordinates.  The old pixel value at that
   * coordinate should be overwritten and all others should remain the same.
   * The updated run-length encoding should be compressed as much as possible;
   * there should not be two consecutive runs with exactly the same RGB color.
   *
   * @param x     the x-coordinate of the pixel to modify.
   * @param y     the y-coordinate of the pixel to modify.
   * @param red   the new red intensity to store at coordinate (x, y).
   * @param green the new green intensity to store at coordinate (x, y).
   * @param blue  the new blue intensity to store at coordinate (x, y).
   */
  public void setPixel(int x, int y, short red, short green, short blue) {

    if(!(x >= 0 && x < getWidth() && y >= 0 && y < getHeight())) {
      System.out.println("ERROR: x and/or y are out of bound...");
      System.exit(1);
    }

    if(!(isValidColor(red) && isValidColor(green) && isValidColor(blue))){
      System.out.println("ERROR: red/green/blue isn't within the range [0, 255]...");
      System.exit(1);
    }

    int count = 1 + x + y * getWidth();
    int len = 0;
    RunNode node;
    // find the "right" run which contains pixel (x, y).
    for (node = runs.head.next; node != runs.head; node = node.next) {
      len = node.len;
      if (count > len) {
        count -= len;
      } else {
        break;
      }
    }

    // color of pixel (x, y) remain the same, no need of modification.
    if (isSameColor(node, red, green, blue)) return;

    // "node" only contains pixel (x, y).
    if (len == 1) {
      if (isSameColor(node.prev, red, green, blue)) {
        node.prev.len += 1;
        // delete "node"
        node.prev.next = node.next;
        node.next.prev = node.prev;
        return;
      } else if (isSameColor(node.next, red, green, blue)) {
        node.next.len += 1;
        // delete "node"
        node.prev.next = node.next;
        node.next.prev = node.prev;
        return;
      } else {
        node.red = red;
        node.green = green;
        node.blue = blue;
        return;
      }
    }

    // "node" contains more than one elements.
    if (count == 1) {
      node.len -= 1;
      // same color as previous "run", thus merge.
      if(isSameColor(node.prev, red, green, blue)) {
        node.prev.len += 1;
        return;
      }
      // color differs from previous "run", new one and insert.
      runs.insertAfter(node.prev, new int[]{1, red, green, blue});
      return;
    } else if (count == len) {
      // pixel (x, y) is located in the end.
      node.len -= 1;
      // same color as next "run", then merge.
      if(isSameColor(node.next, red, green, blue)) {
        node.next.len += 1;
        return;
      }
      // color differs from next "run", new one and insert.
      runs.insertAfter(node, new int[]{1, red, green, blue});
      return;
    } else {
      // pixel (x, y) is located in the middle.
      RunNode left = new RunNode(new int[]{count-1, node.red, node.green, node.blue});
      RunNode mid = new RunNode(new int[]{1, red, green, blue});
      RunNode right = new RunNode(new int[]{len-count, node.red, node.green, node.blue});
      // insert "left"/"mid"/"right" between "node" and "node.next".
      right.next = node.next;
      node.next.prev = right;
      right.prev = mid;
      mid.next = right;
      mid.prev = left;
      left.next = mid;
      node.prev.next = left;
      left.prev = node.prev;
    }

  check();

  }

  /***
   * helper function: to check if pixel at (x1, y1) is the same as (x2, y2) in terms of color.
   * @param image
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return
   */
  private boolean isDifferentColor(PixImage image, int x1, int y1, int x2, int y2) {
    return image.getRed(x1, y1) != image.getRed(x2, y2) ||
            image.getGreen(x1, y1) != image.getGreen(x2, y2) ||
            image.getBlue(x1, y1) != image.getBlue(x2, y2);
  }

  /***
   * helper function: to check if node's RGB is (r, g, b).
   * @param node
   * @param r
   * @param g
   * @param b
   * @return
   */
  private boolean isSameColor(RunNode node, short r, short g, short b) {
    return (node.red == r) && (node.green == g) && (node.blue == b);
  }

  /***
   * helper function: to check if color is within the range [0, 255].
   * @param color
   * @return
   */
  private boolean isValidColor(int color) {
    return color >= 0 && color <= 255;
  }

  /**
   * TEST CODE:  YOU DO NOT NEED TO FILL IN ANY METHODS BELOW THIS POINT.
   * You are welcome to add tests, though.  Methods below this point will not
   * be tested.  This is not the autograder, which will be provided separately.
   */


  /**
   * doTest() checks whether the condition is true and prints the given error
   * message if it is not.
   *
   * @param b the condition to check.
   * @param msg the error message to print if the condition is false.
   */
  private static void doTest(boolean b, String msg) {
    if (b) {
      System.out.println("Good.");
    } else {
      System.err.println(msg);
    }
  }

  /**
   * array2PixImage() converts a 2D array of grayscale intensities to
   * a grayscale PixImage.
   *
   * @param pixels a 2D array of grayscale intensities in the range 0...255.
   * @return a new PixImage whose red, green, and blue values are equal to
   * the input grayscale intensities.
   */
  private static PixImage array2PixImage(int[][] pixels) {
    int width = pixels.length;
    int height = pixels[0].length;
    PixImage image = new PixImage(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setPixel(x, y, (short) pixels[x][y], (short) pixels[x][y],
                       (short) pixels[x][y]);
      }
    }

    return image;
  }

  /**
   * setAndCheckRLE() sets the given coordinate in the given run-length
   * encoding to the given value and then checks whether the resulting
   * run-length encoding is correct.
   *
   * @param rle the run-length encoding to modify.
   * @param x the x-coordinate to set.
   * @param y the y-coordinate to set.
   * @param intensity the grayscale intensity to assign to pixel (x, y).
   */
  private static void setAndCheckRLE(RunLengthEncoding rle,
                                     int x, int y, int intensity) {
    rle.setPixel(x, y,
                 (short) intensity, (short) intensity, (short) intensity);
    rle.check();
  }

  /**
   * main() runs a series of tests of the run-length encoding code.
   */
  public static void main(String[] args) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image1 = array2PixImage(new int[][] { { 0, 3, 6 },
                                                   { 1, 4, 7 },
                                                   { 2, 5, 8 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x3 image.  Input image:");
    System.out.print(image1);
    RunLengthEncoding rle1 = new RunLengthEncoding(image1);
    rle1.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle1.getWidth() == 3 && rle1.getHeight() == 3,
           "RLE1 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(image1.equals(rle1.toPixImage()),
           "image1 -> RLE1 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 42);
    image1.setPixel(0, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           /*
                       array2PixImage(new int[][] { { 42, 3, 6 },
                                                    { 1, 4, 7 },
                                                    { 2, 5, 8 } })),
           */
           "Setting RLE1[0][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 0, 42);
    image1.setPixel(1, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 1, 2);
    image1.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 0);
    image1.setPixel(0, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 7);
    image1.setPixel(2, 2, (short) 7, (short) 7, (short) 7);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 7 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 42);
    image1.setPixel(2, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 2, 42);
    image1.setPixel(1, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][2] = 42 fails.");


    PixImage image2 = array2PixImage(new int[][] { { 2, 3, 5 },
                                                   { 2, 4, 5 },
                                                   { 3, 4, 6 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on another 3x3 image.  Input image:");
    System.out.print(image2);
    RunLengthEncoding rle2 = new RunLengthEncoding(image2);
    rle2.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle2.getWidth() == 3 && rle2.getHeight() == 3,
           "RLE2 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(rle2.toPixImage().equals(image2),
           "image2 -> RLE2 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 0, 1, 2);
    image2.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 2, 0, 2);
    image2.setPixel(2, 0, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[2][0] = 2 fails.");


    PixImage image3 = array2PixImage(new int[][] { { 0, 5 },
                                                   { 1, 6 },
                                                   { 2, 7 },
                                                   { 3, 8 },
                                                   { 4, 9 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 5x2 image.  Input image:");
    System.out.print(image3);
    RunLengthEncoding rle3 = new RunLengthEncoding(image3);
    rle3.check();
    System.out.println("Testing getWidth/getHeight on a 5x2 encoding.");
    doTest(rle3.getWidth() == 5 && rle3.getHeight() == 2,
           "RLE3 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 5x2 encoding.");
    doTest(rle3.toPixImage().equals(image3),
           "image3 -> RLE3 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 4, 0, 6);
    image3.setPixel(4, 0, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[4][0] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 1, 6);
    image3.setPixel(0, 1, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][1] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 0, 1);
    image3.setPixel(0, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][0] = 1 fails.");


    PixImage image4 = array2PixImage(new int[][] { { 0, 3 },
                                                   { 1, 4 },
                                                   { 2, 5 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x2 image.  Input image:");
    System.out.print(image4);
    RunLengthEncoding rle4 = new RunLengthEncoding(image4);
    rle4.check();
    System.out.println("Testing getWidth/getHeight on a 3x2 encoding.");
    doTest(rle4.getWidth() == 3 && rle4.getHeight() == 2,
           "RLE4 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x2 encoding.");
    doTest(rle4.toPixImage().equals(image4),
           "image4 -> RLE4 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 2, 0, 0);
    image4.setPixel(2, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[2][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 0);
    image4.setPixel(1, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 1);
    image4.setPixel(1, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 1 fails.");
  }
}
