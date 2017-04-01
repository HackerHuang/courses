public class superclass {
    public int method() {
	return 0;
    }

    public static void main(String[] args) {
	// //case (a)
	// subclass b = new subclass();	
	// int i = ((superclass) b).method();
	// System.out.println(i);

	// case (b)
	// superclass a = new superclass();
	// int i = ((subclass) a).method();
	// System.out.println(i);

	// case (c)
	subclass c = new subclass();
	int i = c.method();
	System.out.println(i);
    }
}

class subclass {
    public int method() {
	return 2;
    }
}
