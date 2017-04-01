package list;

/* Entry.java */

/**
 *  A class for dictionary entries.
 **/

public class Entry implements Comparable <Entry> {

    protected Object key;
    protected Object value;

    public Entry (Object k, Object v) {
        key = k;
        value = v;
    }

    public Object key() {
        return key;
    }

    public Object value() {
        return value;
    }

    //Add toString for testing
    public String toString() {
        return "[" + key + ", " + value + "]";
    }

    public int compareTo(Entry e) {
        Comparable thisComp = (Comparable) key;
        Comparable eComp = (Comparable) e.key;
        if (thisComp.compareTo(eComp) < 0) {return -1;}
        else if (thisComp.compareTo(eComp) == 0) {return 0;}
        else {return 1;}
    }

}