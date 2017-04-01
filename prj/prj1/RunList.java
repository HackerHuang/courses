public class RunList {

    // Doubly-linked List to store "run"s.
    // "Sentinel" node is used.
    
    RunNode head;

    int size;

    public RunList() {
	// create a "sentinel" node
	head = new RunNode(new int[] {-1, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE});
	head.prev = head;
	head.next = head;
	size = 0;
    }

    public void insertFront(int[] oneRun) {
	RunNode node = new RunNode(oneRun);
	node.next = head.next;
	head.next.prev = node;
	head.next = node;
	size++;
	// DEBUG MODE
	assert size == countLength() : "ERROR: SIZE NOT MATCH...";
    }

    public void insertEnd(int[] oneRun) {
	RunNode node = new RunNode(oneRun);
	node.next = head;
	node.prev = head.prev;
	head.prev.next = node;
	head.prev = node;
	size++;
	// DEBUG MODE
	assert size == countLength() : "ERROR: SIZE NOT MATCH...";
    }

	void insertAfter(RunNode node, int[] newRun) {
    	RunNode newNode = new RunNode(newRun);
    	newNode.next = node.next;
    	newNode.prev = node;
    	node.next.prev = newNode;
    	node.next = newNode;
    	size++;
	}

    public void removeFront() {
	if(size == 0) return;
	head.next = head.next.next;
	head.next.prev = head;
	size--;
	// DEBUG MODE
	assert size == countLength() : "ERROR: SIZE NOT MATCH...";
    }

    public void removeEnd() {
	if(size == 0) return;
	head.prev = head.prev.prev;
	head.prev.next = head;
	size--;
	// DEBUG MODE
	assert size == countLength() : "ERROR: SIZE NOT MATCH...";
    }

    public int length() {
	return size;
    }

    public String toString() {
	String out = "";
	for(RunNode node = head.next; node != head; node = node.next) {
	    out += node.toString() + " ";
	}
	return out;
    }

    // helper function: used solely for purpose of checking the correctness of "size".
    private int countLength() {
	int count = 0;
	for(RunNode node = head.next; node != head; node = node.next) {
	    count++;
	}
	return count;
    }
}
