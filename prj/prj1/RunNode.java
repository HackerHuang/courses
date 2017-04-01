class RunNode {

    // The returned array encodes a run by storing the length
    // of the run (the number of pixels) at index zero, the red
    // pixel intensity at index one, the green pixel intensity
    // at index two, and the blue pixel intensity at index three.

    int len; // run length

    int red;

    int green;

    int blue;

    RunNode prev;

    RunNode next;

    public RunNode() {
	this(0, 0, 0, 0);
    }

    public RunNode(int runLength, int red, int green, int blue) {
        this.len = runLength;
        this.red = red;
        this.green = green;
        this.blue = blue;
        prev = null;
        next = null;
    }

    public RunNode(int[] run) {
        this(run[0], run[1], run[2], run[3]);
    }

    public String toString() {
 	String s = "[ " +
                len + " " +
                red + " " +
                green + " " +
                blue + "]";
	return s;
    }
}
    
    
