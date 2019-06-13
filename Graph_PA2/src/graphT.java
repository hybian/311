import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class graphT {

	static ArrayList<String> lines = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		WGraph wg = new WGraph("example.txt");
		ArrayList<Integer> solution = new ArrayList<Integer>();
		ArrayList<Integer> s1 = new ArrayList<Integer>();
		s1.add(1);
		s1.add(2);
		s1.add(2);
		s1.add(2);
		ArrayList<Integer> s2 = new ArrayList<Integer>();
		s2.add(5);
		s2.add(6);
		s2.add(4);
		s2.add(4);
		solution.add(2);
		solution.add(2);
		solution.add(3);
		solution.add(4);
		solution.add(5);
		solution.add(6);
		
		System.out.println(wg.S2S(s1, s2).toString());
		
		
		

//		WGraph graph = new WGraph("graphinput.txt");
//		WGraph graph = new WGraph("zyb2.txt");
//		WGraph graph = new WGraph("ImageToGraph.txt");
//		ArrayList<Integer> S = new ArrayList<>();
//		ArrayList<Integer> S1 = new ArrayList<>();
//		ArrayList<Integer> S2 = new ArrayList<>();
//		
//		
//		S.add(7);
//		S.add(5);
//		S.add(8);
//		S.add(8);
//		S.add(6);
//		S.add(9);
//		
//		
//		
//
//		S1.add(2);
//		S1.add(3);
//		S1.add(4);
//		S1.add(4);
				
//		S2.add(2);
//		S2.add(3);
//		S2.add(4);
//		S2.add(4);
//		S2.add(4);
//		S2.add(7);
//		S2.add(5);
//		S2.add(5);
//		S2.add(3);
//		S2.add(7);
//		S2.add(2);
//		S2.add(5);
		
//		ArrayList<Integer> test = graph.S2S(S1,S2);

//		ArrayList<Integer> test = graph.V2S(7,5,S2);
//		ArrayList<Integer> test1 = graph.V2S(3,3,S);
//		ArrayList<Integer> test2 = graph.V2S(8,8,S);
//		System.out.println(test.toString());
//		System.out.println(test1.toString());
//		System.out.println(test2.toString());
		
//		myGraph g = new myGraph("graphinput.txt");
//		g.S2S(S1, S2);
//		
		
		
		
//		File file = new File("graphinput.txt"); 
//		BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(file));
//			String st;
//			while ((st=br.readLine()) != null) {
//				lines.add(st);
//			}
//			for(int i=0; i<lines.size(); i++) 
//			{
//				System.out.println(lines.get(i));
//			}
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		//Hashtable<Node, LinkedList<Edge>> hm = new Hashtable<Node, LinkedList<Edge>>(); 
//		//Hashtable<String, LinkedList<Edge>> hm = new Hashtable<String, LinkedList<Edge>>(); 
//		
//		HashMap<Node, LinkedList<Edge>> hm = new HashMap<Node, LinkedList<Edge>>();
//		int numV = 0;
//		int numE = 0;
//		for(int i=0; i<lines.size(); i++) 
//		{
//			if(i==0) 
//				numV = Integer.parseInt(lines.get(0));
//			else if(i==1)
//				numE = Integer.parseInt(lines.get(1));
//			else 
//			{
//				String[] elements = lines.get(i).split(" ");
//				//String source = elements[0] + " " + elements[1]; 
//				
//				Node src = new Node( Integer.parseInt(elements[0]), Integer.parseInt(elements[1]) );
//				Node dest = new Node( Integer.parseInt(elements[2]), Integer.parseInt(elements[3]) );
//				Edge edge = new Edge( src, dest, Integer.parseInt(elements[4]) );
//								
//				if(!hm.containsKey(src)) 
//				{
////					System.out.println("!contain");
//					LinkedList<Edge> list = new LinkedList<>();
//					list.addFirst(edge);
//					hm.put(src, list);
//				}
//				else 
//				{
////					System.out.println("contain");
//					hm.get(src).add(edge);
//				}
//			}
//		}
//		
//		Node node = new Node(1, 2);
//		System.out.println("=============================");
//		System.out.println(hm.get(node).size() + " "+ hm.get(node).get(0).getWeight());


//		for (HashMap.Entry<Node, LinkedList<Edge>> entry : hm.entrySet()) {
//		    String key = entry.getKey();
//		    LinkedList<Edge> value = entry.getValue();
//		    System.out.println("src: "+key + " contains: "+value.size());
//		}
		
	}

}
