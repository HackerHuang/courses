public class RunListTester {

    public static void main(String[] args) {

	RunList lst = new RunList();

	System.out.println(lst);

       	lst.insertFront(new int[] {10, 0, 0, 0});

	System.out.println(lst);

	lst.insertEnd(new int[] {5, 255, 255, 255});

	System.out.println(lst);

	lst.removeEnd();

	System.out.println(lst);

	lst.removeFront();

	System.out.println(lst);
    }
}
	
