import java.util.ArrayList;


public class imageTest {

	public static void main(String[] args) {
		
		
		ImageProcessor ip = new ImageProcessor("10x10Input.txt");
		ip.writeReduced(9, "10x10Output.txt");
		
//		ImageProcessor ip = new ImageProcessor("imageinput.txt");
//		System.out.println("TEST: " + ip.getImportance().toString());
//		ip.writeReduced(2, "123.txt");
//		System.out.println("TESTwtf: " + ip.getImportance().toString());
		
//		System.out.println("<><><><><><><><><><><><><><><><><><><>");
//		
//		ip.writeReduced(1, "123.txt");
//		System.out.println("TESTwtf: " + ip.getImportance().toString());
		
//		ip.writeReduced(3, "321.txt");
//		ArrayList<ArrayList<Integer>> test = new ArrayList<ArrayList<Integer>>();
//		
//		ArrayList<Integer> inner = new ArrayList<>();
//		inner.add(1);
//		test.add(inner);
//		System.out.println(test.get(0).get(0).intValue());
		
		
	}

}
