import java.io.*;

public class Nuke2 {
    public static void main(String[] args) throws Exception {
	BufferedReader keyboard;
	InputStreamReader is = new InputStreamReader(System.in);
	keyboard = new BufferedReader(is);

	//System.out.println("please input a string from keyboard:");
	//System.out.flush();
	
	String oneString = keyboard.readLine();
	    
	String newString;
	newString = oneString.charAt(0) + oneString.substring(2);
	System.out.println(newString);
    }
}
	    
