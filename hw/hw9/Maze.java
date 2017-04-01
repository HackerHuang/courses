/* Maze.java */

import java.util.*;
import set.*;

/**
 *  The Maze class represents a maze in a rectangular grid.  There is exactly
 *  one path between any two points.
 **/

public class Maze {

  // Horizontal and vertical dimensions of the maze.
  protected int horiz;
  protected int vert;
  // Horizontal and vertical interior walls; each is true if the wall exists.
  protected boolean[][] hWalls;
  protected boolean[][] vWalls;

  // Object for generting random numbers.
  private static Random random;

  // Constants used in depth-first search (which checks for cycles in the
  // maze).
  private static final int STARTHERE = 0;
  private static final int FROMLEFT = 1;
  private static final int FROMRIGHT = 2;
  private static final int FROMABOVE = 3;
  private static final int FROMBELOW = 4;

  /**
   *  Maze() creates a rectangular maze having "horizontalSize" cells in the
   *  horizontal direction, and "verticalSize" cells in the vertical direction.
   *  There is a path between any two cells of the maze.  A disjoint set data
   *  structure is used to ensure that there is only one path between any two
   *  cells.
   **/
  public Maze(int horizontalSize, int verticalSize) {
    int i, j;

    horiz = horizontalSize;
    vert = verticalSize;
    if ((horiz < 1) || (vert < 1) || ((horiz == 1) && (vert == 1))) {
      return;                                    // There are no interior walls
    }

    // Create all of the horizontal interior walls.  Initially, every
    // horizontal wall exists; they will be removed later by the maze
    // generation algorithm.
    if (vert > 1) {
      hWalls = new boolean[horiz][vert - 1];
      for (j = 0; j < vert - 1; j++) {
        for (i = 0; i < horiz; i++) {
          hWalls[i][j] = true;
        }
      }
    }
    // Create all of the vertical interior walls.
    if (horiz > 1) {
      vWalls = new boolean[horiz - 1][vert];
      for (i = 0; i < horiz - 1; i++) {
        for (j = 0; j < vert; j++) {
          vWalls[i][j] = true;
        }
      }
    }


    /**
     * Fill in the rest of this method.  You should go through all the walls of
     * the maze in random order, and remove any wall whose removal will not
     * create a cycle.  Use the implementation of disjoint sets provided in the
     * set package to avoid creating any cycles.
     *
     * Note the method randInt() further below, which generates a random
     * integer.  randInt() generates different numbers every time the program
     * is run, so that you can make lots of different mazes.
     **/
//    // version 1: use Disjoint Sets.
//    genRandMaze(hWalls, vWalls);
//    // version 2: use Depth First Search.
//    genRandMaze(hWalls, vWalls, horiz, vert);
    // version 3: use revised Depth First Search.
    genRandMaze();
  }

  /** IMPLEMENTATION 1: use DISJOINT SETS.
   *  generate random maze using disjoint sets data structure.
   *  specifically, merge many sets gradually to only ONE set.
   */
  private void genRandMaze(boolean[][] hWalls, boolean[][] vWalls) {
    // STEP 1: create a disjoint sets data structure in which each cell
    // of the maze is represented as a separate item.
    int numOfSets = horiz * vert;
    DisjointSets cellSet = new DisjointSets(numOfSets);

    // STEP 2: order the interior walls of the maze in a random order.
    // STEP 2.1: create an array in which every wall is represented.
    Wall[] walls = createWalls();
    // STEP 2.2: create a random permutation of the array
    randPerm(walls);

    // STEP 3: visit the walls in the (random) order, and scramble the wall
    // when two adjacent cells are NOT in the same set.
    int n = 0;// loop index in "walls".

    // WARNING: MUST use while loop to check if all cells belongs to ONE set; can NOT use for loop.
    // one BUG: for(int n = 0; n < horiz * vert-1; n++) is WRONG.
    // Although we know that (horiz*vert-1) walls(either horizontal OR vertical) needed to be eliminated,
    // we can NOT ensure the preceding (horiz*vert-1) walls will work.
    while (numOfSets != 1 && n < walls.length) {
      Wall oneWall = walls[n];
      int x1, y1;// cell 1
      int x2, y2;// cell 2

      x1 = oneWall.x;
      y1 = oneWall.y;
      // horizontal wall
      if (oneWall.isHorizon) {
        x2 = x1;
        y2 = y1 + 1;
      } else {
        // vertical wall
        x2 = x1 + 1;
        y2 = y1;
      }
      int s1 = cellSet.find(x1 + y1 * horiz);
      int s2 = cellSet.find(x2 + y2 * horiz);
      // if NOT in the SAME set
      if (s1 != s2) {
        // eliminate the horizontal OR vertical wall.
        if (oneWall.isHorizon) hWalls[oneWall.x][oneWall.y] = false;
        else vWalls[oneWall.x][oneWall.y] = false;
        // merge two cells.
        cellSet.union(s1, s2);
        numOfSets--;
      }
      n++;
    }
  }

  /** IMPLEMENTATION 2: use given depthFirstSearch() method to determined
   * whether a wall can be removed. Although it works, but it is low efficient.
   */
  private void genRandMaze(boolean[][] hWalls, boolean[][] vWalls, int horiz, int vert) {
    Wall[] walls = createWalls();
    randPerm(walls);
    int numOfCells = horiz * vert;
    int numOfEdges = 0;
    boolean[][] cellVisited;
    int n = 0;
    while (numOfEdges != (numOfCells - 1)) {
      Wall oneWall = walls[n];
      // WARNING: the initialization of cellVisited should be inside the while-loop.
      // OTHERWISE, depthFirstSearch will NOT work correctly.
      cellVisited = new boolean[horiz][vert];
      if (oneWall.isHorizon) {
        hWalls[oneWall.x][oneWall.y] = false;
        if (depthFirstSearch(oneWall.x, oneWall.y, STARTHERE, cellVisited))
          hWalls[oneWall.x][oneWall.y] = true;
        else numOfEdges++;
      } else {
        vWalls[oneWall.x][oneWall.y] = false;
        if (depthFirstSearch(oneWall.x, oneWall.y, STARTHERE, cellVisited))
          vWalls[oneWall.x][oneWall.y] = true;
        else numOfEdges++;
      }
      n++;
    }
  }

  private void genRandMaze(){
    int x = randInt(horiz);
    int y = randInt(vert);
    boolean[][] cellVisited = new boolean[horiz][vert];
    depthFirstSearch2(x, y, STARTHERE, cellVisited);
  }

  private void depthFirstSearch2(int x, int y, int fromWhere, boolean[][] cellVisited){
    cellVisited[x][y] = true;

    int[] dir = new int[]{1, 2, 3, 4};
    for(int i = dir.length; i > 1; --i){
      int index = randInt(i);
      // swap the picked item with the last item.
      int tmp = dir[i-1];
      dir[i-1] = dir[index];
      dir[index] = tmp;
    }

    for(int d : dir){
      if (fromWhere != d) {//    int x = 5;
//    int y = 4;
        // Visit the cell to the right?
        if (d == FROMRIGHT && (x+1 < horiz) && (!cellVisited[x + 1][y])){
          vWalls[x][y] = false;
          depthFirstSearch2(x + 1, y, FROMLEFT, cellVisited);
        }
        // Visit the cell below?
        if (d == FROMBELOW && (y+1 < vert) && (!cellVisited[x][y + 1])) {
          hWalls[x][y] = false;
          depthFirstSearch2(x, y + 1, FROMABOVE, cellVisited);
        }
        // Visit the cell to the left?
        if (d == FROMLEFT && (x-1 >= 0) && (!cellVisited[x - 1][y])) {
          vWalls[x - 1][y] = false;
          depthFirstSearch2(x - 1, y, FROMRIGHT, cellVisited);
        }
        // Visit the cell above?
        if (d == FROMABOVE && (y-1 >= 0) && (!cellVisited[x][y - 1])) {
          hWalls[x][y - 1] = false;
          depthFirstSearch2(x, y - 1, FROMBELOW, cellVisited);
        }
      }
    }
  }

  private void randPerm(Wall[] walls) {
    for (int n = walls.length; n > 1; n--) {
      // choose a wall and swap it to the end of the array.
      int index = randInt(n);// index belongs to [0, n-1].
      Wall tmp = walls[index];
      walls[index] = walls[n - 1];
      walls[n - 1] = tmp;
    }
  }

  private Wall[] createWalls() {
    Wall[] walls = new Wall[horiz * (vert - 1) + (horiz - 1) * vert];
    // horizontal walls => walls
    for (int i = 0; i < horiz; i++) {
      for (int j = 0; j < vert - 1; j++)
        walls[horiz * j + i] = new Wall(i, j, true);
    }
    // vertical walls => walls.
    for (int i = 0; i < horiz - 1; i++) {
      for (int j = 0; j < vert; j++)
        walls[horiz * (vert - 1) + (horiz - 1) * j + i] = new Wall(i, j, false);
    }
    return walls;
  }

  /**
   *  toString() returns a string representation of the maze.
   **/
  public String toString() {
    int i, j;
    String s = "";

    // Print the top exterior wall.
    for (i = 0; i < horiz; i++) {
      s = s + "--";
    }
    s = s + "-\n|";

    // Print the maze interior.
    for (j = 0; j < vert; j++) {
      // Print a row of cells and vertical walls.
      for (i = 0; i < horiz - 1; i++) {
        if (vWalls[i][j]) {
          s = s + " |";
        } else {
          s = s + "  ";
        }
      }
      s = s + " |\n+";
      if (j < vert - 1) {
        // Print a row of horizontal walls and wall corners.
        for (i = 0; i < horiz; i++) {
          if (hWalls[i][j]) {
            s = s + "-+";
          } else {
            s = s + " +";
          }
        }
        s = s + "\n|";
      }
    }

    // Print the bottom exterior wall.  (Note that the first corner has
    // already been printed.)
    for (i = 0; i < horiz; i++) {
      s = s + "--";
    }
    return s + "\n";
  }

  /**
   * horizontalWall() determines whether the horizontal wall on the bottom
   * edge of cell (x, y) exists.  If the coordinates (x, y) do not correspond
   * to an interior wall, true is returned.
   **/
  public boolean horizontalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 1) || (y > vert - 2)) {
      return true;
    }
    return hWalls[x][y];
  }

  /**
   * verticalWall() determines whether the vertical wall on the right edge of
   * cell (x, y) exists. If the coordinates (x, y) do not correspond to an
   * interior wall, true is returned.
   **/
  public boolean verticalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 2) || (y > vert - 1)) {
      return true;
    }
    return vWalls[x][y];
  }

  /**
   * randInt() returns a random integer from 0 to choices - 1.
   **/
  private static int randInt(int choices) {
    if (random == null) {       // Only executed first time randInt() is called
      random = new Random();       // Create a "Random" object with random seed
    }
    int r = random.nextInt() % choices;      // From 1 - choices to choices - 1
    if (r < 0) {
      r = -r;                                          // From 0 to choices - 1
    }
    return r;
  }

  /**
   * diagnose() checks the maze and prints a warning if not every cell can be
   * reached from the upper left corner cell, or if there is a cycle reachable
   * from the upper left cell.
   *
   * DO NOT CHANGE THIS METHOD.  Your code is expected to work with our copy
   * of this method.
   **/
  protected void diagnose() {
    if ((horiz < 1) || (vert < 1) || ((horiz == 1) && (vert == 1))) {
      return;                                    // There are no interior walls
    }

    boolean mazeFine = true;

    // Create an array that indicates whether each cell has been visited during
    // a depth-first traversal.
    boolean[][] cellVisited = new boolean[horiz][vert];
    // Do a depth-first traversal.
    if (depthFirstSearch(0, 0, STARTHERE, cellVisited)) {
      System.out.println("Your maze has a cycle.");
      mazeFine = false;
    }

    // Check to be sure that every cell of the maze was visited.
    outerLoop:
    for (int j = 0; j < vert; j++) {
      for (int i = 0; i < horiz; i++) {
        if (!cellVisited[i][j]) {
          System.out.println("Not every cell in your maze is reachable from " +
                  "every other cell.");
          mazeFine = false;
          break outerLoop;
        }
      }
    }

    if (mazeFine) {
      System.out.println("What a fine maze you've created!");
    }
  }

  /**
   * depthFirstSearch() does a depth-first traversal of the maze, marking each
   * visited cell.  Returns true if a cycle is found.
   *
   * DO NOT CHANGE THIS METHOD.  Your code is expected to work with our copy
   * of this method.
   */
  protected boolean depthFirstSearch(int x, int y, int fromWhere,
                                     boolean[][] cellVisited) {
    boolean cycleDetected = false;
    cellVisited[x][y] = true;

    // Visit the cell to the right?
    if ((fromWhere != FROMRIGHT) && !verticalWall(x, y)) {
      if (cellVisited[x + 1][y]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x + 1, y, FROMLEFT, cellVisited) ||
                cycleDetected;
      }
    }

    // Visit the cell below?
    if ((fromWhere != FROMBELOW) && !horizontalWall(x, y)) {
      if (cellVisited[x][y + 1]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x, y + 1, FROMABOVE, cellVisited) ||
                cycleDetected;
      }
    }

    // Visit the cell to the left?
    if ((fromWhere != FROMLEFT) && !verticalWall(x - 1, y)) {
      if (cellVisited[x - 1][y]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x - 1, y, FROMRIGHT, cellVisited) ||
                cycleDetected;
      }
    }

    // Visit the cell above?
    if ((fromWhere != FROMABOVE) && !horizontalWall(x, y - 1)) {
      if (cellVisited[x][y - 1]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x, y - 1, FROMBELOW, cellVisited) ||
                cycleDetected;
      }
    }

    return cycleDetected;
  }

  /**
   * main() creates a maze of dimensions specified on the command line, prints
   * the maze, and runs the diagnostic method to see if the maze is good.
   */
  public static void main(String[] args) {
    int x = 39;
    int y = 15;
//    int x = 3;
//    int y = 2;

    /**
     *  Read the input parameters.
     */

    if (args.length > 0) {
      try {
        x = Integer.parseInt(args[0]);
      }
      catch (NumberFormatException e) {
        System.out.println("First argument to Simulation is not an number.");
      }
    }

    if (args.length > 1) {
      try {
        y = Integer.parseInt(args[1]);
      }
      catch (NumberFormatException e) {
        System.out.println("Second argument to Simulation is not an number.");
      }
    }

    Maze maze = new Maze(x, y);
    System.out.print(maze);
    maze.diagnose();
  }
}
