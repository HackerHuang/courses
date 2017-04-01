/* ListSorts.java */

import list.*;
import sun.awt.image.ImageWatched;

public class ListSorts {

  private final static int SORTSIZE = 1000000;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    LinkedQueue lq = new LinkedQueue();
    try {
      while (!q.isEmpty()) {
        LinkedQueue tmpQ = new LinkedQueue();
        tmpQ.enqueue(q.dequeue());
        lq.enqueue(tmpQ);
      }
    } catch(QueueEmptyException qe){
      System.err.println("ERROR: dequeue an empty queue.");
    }
    return lq;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    LinkedQueue q = new LinkedQueue();
    Comparable item1, item2;
    try {
      while ((!q1.isEmpty()) && (!q2.isEmpty())) {
        item1 = (Comparable) q1.front();
        item2 = (Comparable) q2.front();
        int comp = item1.compareTo(item2);
        if(comp < 0){
          q.enqueue(item1);
          q1.dequeue();
        } else if(comp == 0){
          q.enqueue(item1);
          q.enqueue(item2);
          q1.dequeue();
          q2.dequeue();
        } else {
          q.enqueue(item2);
          q2.dequeue();
        }
      }
      if(!q1.isEmpty()) q.append(q1);
      if(!q2.isEmpty()) q.append(q2);
    } catch(QueueEmptyException qe){
      System.err.println("ERROR: dequeue an empty queue.");
    }
    return q;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  public static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
    Comparable item;
    try{
      while(!qIn.isEmpty()) {
        item = (Comparable) qIn.dequeue();
        int comp = item.compareTo(pivot);
        if(comp == 0) qEquals.enqueue(item);
        else if(comp > 0) qLarge.enqueue(item);
        else qSmall.enqueue(item);
      }
    } catch(QueueEmptyException qe){
      qe.printStackTrace();
    }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
    // q is empty.
    if(q.isEmpty()) return;
    // q is NOT empty.
    LinkedQueue queueOfQueue = makeQueueOfQueues(q);
    LinkedQueue q1, q2;
    try {
      while (queueOfQueue.size() > 1) {
        q1 = (LinkedQueue) queueOfQueue.dequeue();
        q2 = (LinkedQueue) queueOfQueue.dequeue();
        queueOfQueue.enqueue(mergeSortedQueues(q1, q2));
      }
      q.append((LinkedQueue) queueOfQueue.dequeue());
    } catch(QueueEmptyException qe){
      qe.printStackTrace();
    }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
    if(q.isEmpty() || q.size() == 1) return;

    LinkedQueue qSmall = new LinkedQueue();
    LinkedQueue qEquals = new LinkedQueue();
    LinkedQueue qLarge = new LinkedQueue();
    Comparable pivot = (Comparable) q.nth((int)(1 + (q.size()-1)* Math.random()));
    partition(q, pivot, qSmall, qEquals, qLarge);
    quickSort(qSmall);
    quickSort(qLarge);
    q.append(qSmall);
    q.append(qEquals);
    q.append(qLarge);
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

  /**
   *  main() performs some tests on mergesort and quicksort.  Feel free to add
   *  more tests of your own to make sure your algorithms works on boundary
   *  cases.  Your test code will not be graded.
   **/
  public static void main(String [] args) {

    LinkedQueue q = makeRandom(10);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());

    // corner case 1: size == 1
    q = makeRandom(1);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());
    // corner case 2: size == 0
    q = makeRandom(0);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());


    q = makeRandom(10);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());

    q = makeRandom(1);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());

    q = makeRandom(0);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());

    // Part III.
    Timer stopWatch = new Timer();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    System.out.println("/* Test Part IV */");
    LinkedQueue q7 = new LinkedQueue();
    Entry e1 = new Entry(new Integer(3), "spa");
    Entry e2 = new Entry(new Integer(7), "hex");
    Entry e3 = new Entry(new Integer(3), "boo");
    Entry e4 = new Entry(new Integer(7), "for");
    // add one more item, so that total number is odd.
    Entry e5 = new Entry(new Integer(3), "mee");
    q7.enqueue(e1);
    q7.enqueue(e2);
    q7.enqueue(e3);
    q7.enqueue(e4);
    q7.enqueue(e5);

    System.out.println("\t\t q7: " + q7);
    mergeSort(q7);
    System.out.println("After mergeSort, q7: " + q7);
    System.out.println("\n");

    LinkedQueue q8 = new LinkedQueue();
    q8.enqueue(e1);
    q8.enqueue(e2);
    q8.enqueue(e3);
    q8.enqueue(e4);
    q8.enqueue(e5);

    System.out.println("\t\t q8: " + q8);
    quickSort(q8);
    System.out.println("After quickSort, q8: " + q8);
  }
}
