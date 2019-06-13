import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class QueueTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		/** QUEUE CONSTRUCTION **/
		
		PriorityQ q = new PriorityQ();
		
		String[] vals = {"A","B","C","D","E","F","G"};
		int[] keys = {1,2,3,4,5,6,7};
		
		for(int i=vals.length-1; i>=0; i--) 
		{
			q.add(vals[i], keys[i]);
		}
		printQ(q);
		
		
		/** QUEUE CONSTRUCTION **/
		
		
		/** decrementPriority Test **/
//		q.decrementPriority(1, 7);
//		System.out.println("----------------");
//		printQ(q);
		/** decrementPriority Test **/
		
		/** returnMax Test **/
//		System.out.println("----------------");
//		System.out.println("returnMax: " + q.returnMax());
		/** returnMax Test **/
		
		/** extractMax Test **/
		System.out.println("----------------");
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		System.out.println("extractMax: " + q.extractMax());
		printQ(q);
		/** extractMax Test **/
		
		/** remove Test **/
//		System.out.println("----------------");
//		q.remove(1);
//		q.remove(2);
//		q.remove(3);
//		q.remove(4);
//		q.remove(5);
//		q.remove(6);
//		q.remove(7);
//		printQ(q);
		/** remove Test **/
		
		/** priorityArray Test **/
//		System.out.println("----------------");
//		for(int i=1; i<=q.getSize(); i++) {
//			System.out.println(q.priorityArray()[i]);
//		}
		/** priorityArray Test **/

		/** isEmpty Test **/
//		System.out.println("----------------");
//		System.out.println(q.isEmpty());
		/** isEmpty Test **/
		
		/** getKey & getVal Test **/
//		System.out.println("----------------");
//		int i=4;
//		System.out.println("key: "+q.getKey(i) + " val: " + q.getValue(i));
		/** getKey & getVal Test **/

	}
	
	
	private static void printQ(PriorityQ queue) 
	{
		for(int i=1; i<=queue.getSize(); i++) 
		{
			System.out.println("KEY: " + queue.getKey(i) + ", VAL: " + queue.getValue(i));
		}
	}
	
	

}
