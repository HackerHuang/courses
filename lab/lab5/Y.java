public class Y extends X implements fooable {
    
    public Y() {
	super();
    }

    public void foo(int i) {
	System.out.println("this is subclass's foo()");
	// return 0;
    }
    
    public static void main(String[] args) {

	X x;
	Y y = new Y();
	x = y;
	System.out.println(x.dummy);
	
	// Y y = new Y();
	// System.out.println(y.dummy);
	
	
	// Y y = new Y();
	// X x = new X();
	// // y.foo();
	// y.foo(2);
	
	// Y y = new Y();
	// X x = new X();
	// x = y;
	// y = x; //compile-time error
	// y = (Y) x;

	// Y[] ya = new Y[10];
	// X[] xa = new X[10];
	// X[] xa = new Y[5];
	
	// X[] xa = ya;
	// ya = xa;
	// ya =(Y[]) xa;
	// xa = ya;
    }
}

interface fooable {

    public static final int dummy = 1;
    
    public void foo(int w);

}
