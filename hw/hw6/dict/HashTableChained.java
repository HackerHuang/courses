/* HashTableChained.java */

package dict;

import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

    /**
     *  Place any data fields here.
     **/
    protected List[] hashTable;
    private int capacity;// prime number
    private int numEntries;
    private int prime = 109345121;
    private long scale, shift;

    /**
     *  Construct a new empty hash table intended to hold roughly sizeEstimate
     *  entries.  (The precise number of buckets is up to you, but we recommend
     *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
     **/
    private boolean isPrime(int n) {
        boolean flag = true;
        for(int i = 2; i * i <= n; i++){
            if(n % i == 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public HashTableChained(int sizeEstimate) {
        // begin at 4/3 * sizeEstimate, so that loadFactor approximates 0.75.
        for(int i = 4 * sizeEstimate / 3; i <= 2 * sizeEstimate; i++) {
            if(isPrime(i)) {
                capacity = i;
                break;
            }
        }
        hashTable = new SList[capacity];
        for(int i = 0; i < capacity; i++) {
            hashTable[i] = new SList();
        }
        numEntries = 0;
        // initialize a, b in the compression function.
        java.util.Random rand = new java.util.Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
    }

    /**
     *  Construct a new empty hash table with a default size.  Say, a prime in
     *  the neighborhood of 100.
     **/

    public HashTableChained() {
        // Your solution here.
        this(100);
    }

    /**
     *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
     *  to a value in the range 0...(size of hash table) - 1.
     *
     *  This function should have package protection (so we can test it), and
     *  should be used by insert, find, and remove.
     **/

    int compFunction(int code) {
        // Replace the following line with your solution.
        long hashValue;
        hashValue = (Math.abs(scale * code + shift) % prime) % capacity;
        if(hashValue < 0) hashValue += capacity;
        return (int)hashValue;
    }

    /**
     *  Returns the number of entries stored in the dictionary.  Entries with
     *  the same key (or even the same key and value) each still count as
     *  a separate entry.
     *  @return number of entries in the dictionary.
     **/

    public int size() {
        // Replace the following line with your solution.
        numEntries = 0;
        for(int i = 0; i < capacity; i++) {
            numEntries += hashTable[i].length();
        }
        return numEntries;
    }


    /***
     * Return the number of buckets in the dictionary.
     * @return
     */
    public int capacity() {
        return capacity;
    }

    /**
     *  Tests if the dictionary is empty.
     *
     *  @return true if the dictionary has no entries; false otherwise.
     **/

    public boolean isEmpty() {
        // Replace the following line with your solution.
        return numEntries == 0;
    }

    /**
     *  Create a new Entry object referencing the input key and associated value,
     *  and insert the entry into the dictionary.  Return a reference to the new
     *  entry.  Multiple entries with the same key (or even the same key and
     *  value) can coexist in the dictionary.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the key by which the entry can be retrieved.
     *  @param value an arbitrary object.
     *  @return an entry containing the key and value.
     **/

    public Entry insert(Object key, Object value) {
        // Replace the following line with your solution.
        Entry newEntry = new Entry();
        newEntry.key = key;
        newEntry.value = value;
        int index = compFunction(key.hashCode());
        (hashTable[index]).insertFront(newEntry);
        numEntries++;
        // DEBUG
        assert numEntries == size() : "ERROR: NUMBER OF ENTRIES NOT MATCH...";
        return newEntry;
    }

    /**
     *  Search for an entry with the specified key.  If such an entry is found,
     *  return it; otherwise return null.  If several entries have the specified
     *  key, choose one arbitrarily and return it.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the search key.
     *  @return an entry containing the key and an associated value, or null if
     *          no entry contains the specified key.
     **/

    public Entry find(Object key) {
        // Replace the following line with your solution.
        int index = compFunction(key.hashCode());
        Entry oneEntry;
        ListNode node;
        try{
            for(node = hashTable[index].front(); node.isValidNode(); node = node.next()) {
                oneEntry = (Entry)node.item();
                if(key.equals(oneEntry.key)) {
                    return oneEntry;
                }
            }
            // DEBUG
            assert numEntries == size() : "ERROR: NUMBER OF ENTRIES NOT MATCH...";
            return null;
        } catch(InvalidNodeException e) {
            return null;
        }
    }

    /**
     *  Remove an entry with the specified key.  If such an entry is found,
     *  remove it from the table and return it; otherwise return null.
     *  If several entries have the specified key, choose one arbitrarily, then
     *  remove and return it.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the search key.
     *  @return an entry containing the key and an associated value, or null if
     *          no entry contains the specified key.
     */

    public Entry remove(Object key) {
        // Replace the following line with your solution.
        int index = compFunction(key.hashCode());
        ListNode node;
        Entry ent;
        try {
            for (node = hashTable[index].front(); node.isValidNode(); node = node.next()) {
                ent = (Entry)node.item();
                if(key.equals(ent.key)) {
                    node.remove();
                    numEntries--;
                    // DEBUG
                    assert numEntries == size() : "ERROR: NUMBER OF ENTRIES NOT MATCH...";
                    return ent;
                }
            }
            // DEBUG
            assert numEntries == size() : "ERROR: NUMBER OF ENTRIES NOT MATCH...";
            return null;
        } catch (InvalidNodeException e) {
            return null;
        }
    }

    /**
     *  Remove all entries from the dictionary.
     */
    public void makeEmpty() {
        // Your solution here.
        try {
            for (int i = 0; i < capacity; i++) {
                if (!hashTable[i].isEmpty()) {
                    for (ListNode node = hashTable[i].front(); node.isValidNode(); ) {
                        ListNode tmp = node.next();
                        node.remove();
                        node = tmp;
                    }
                }
            }
            numEntries = 0;
            // DEBUG
            assert numEntries == size() : "ERROR: NUMBER OF ENTRIES NOT MATCH...";
        } catch (InvalidNodeException e) { }
    }

    // To test your hash function, add a method to your HashTableChained class
    // that counts the number of collisions--or better yet, also prints
    // a histograph of the number of entries in each bucket.
    public String toString() {
        String s = "";
        for(int i = 0; i < capacity; i++) {
            int tmp = hashTable[i].length();
            if(tmp == 0) {
                s += "[" + " " + "]";
            } else {
                s += "[" + tmp + "]";
            }
            if(i % 20 == 19) s += "\n";
        }
        return s;
    }

    public int numOfCollision() {
        int num = 0;
        for(int i = 0; i < capacity; i++) {
            if (hashTable[i].length() > 1) {
                num++;
            }
        }
        return num;
    }
}
