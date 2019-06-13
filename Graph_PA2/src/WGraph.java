import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


/**
* This is the implementation of the WGraph
*
*@author Hongyi Bian
*@author Yuanbo Zheng
*/

public class WGraph {

	private boolean TEST = false;
	private boolean TEST_V2V = false; // to enable test prints
	private boolean TEST_V2S = false;
	private boolean TEST_S2S = false;

	
	private int numVertices;
	private int numEdges;
	private HashMap<Vertex, LinkedList<QueueVertex>> adjList;
	
	
	/**
	 * Constructor WGraph
	 * 
	 * @param FName input file name representing the graph
	 */
	public WGraph(String FName) 
	{
		long start_time = System.nanoTime();	// calculate construction time
		ArrayList<String> lines = new ArrayList<>();	// each line of the file
		File file = new File(FName); 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			while ((str = br.readLine()) != null) {
					 lines.add(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		adjList = new HashMap<Vertex, LinkedList<QueueVertex>>();	// instantiate adjList
		for(int i=0; i<lines.size(); i++) 
		{
			if(i==0) 
				numVertices = Integer.parseInt(lines.get(0));	// first line = # of vertices
			else if(i==1)
				numEdges = Integer.parseInt(lines.get(1));		// second line = # of vertices
			else 
			{
				String[] elements = lines.get(i).split(" ");				
				Vertex src = new Vertex( Integer.parseInt(elements[0]), Integer.parseInt(elements[1]) );
				Vertex dest = new Vertex( Integer.parseInt(elements[2]), Integer.parseInt(elements[3]) );
				QueueVertex toDest = new QueueVertex( dest, Integer.parseInt(elements[4]) );
								
				if( !adjList.containsKey(dest) ) 	// new vertex, add to the list, value = new linkedlist
				{
					LinkedList<QueueVertex> list = new LinkedList<>();
					adjList.put(dest, list);
				}
				if( !adjList.containsKey(src) ) 	// new vertex, add to the list, value = new linkedlist, add dest to list
				{
					LinkedList<QueueVertex> list = new LinkedList<>();
					list.addFirst(toDest);
					adjList.put(src, list);
				}
				else // add dest to existing src's list
				{
					adjList.get(src).add(toDest);
				}
			}
		}
		long end_time = System.nanoTime();
		double difference = (end_time - start_time) / 1e6;
		if(adjList.size() == numVertices && TEST) 
		{
			System.out.println("Successfully created adjList");
			System.out.println("Number of V: " + this.numVertices);
			System.out.println("Number of E: " + this.numEdges);
			System.out.println(FName + ": " + difference + "ms");
		} 
		
	} // end of constructor
	
	
	/**
	 * public method for V2V
	 *
	 * @param ux, uy, vx, vy - valid coordinates of vertices u and v in the graph
	 * @return ArrayList<Integer> path - ArrayList contains even number of integers, for any even i,
	 * 									 i-th and i+1-th integers in the array represent
	 *									 the x-coordinate and y-coordinate of the i-th vertex
	 *									 in the returned path (path is an ordered sequence of vertices)
	 */
	public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy)
	{
		Vertex src = new Vertex(ux, uy);												// src vertex
		Vertex dest = new Vertex(vx, vy);												// dest vertex
		
		/* src and dest same */
		if(src.equals(dest)) 
		{
			ArrayList<Integer> result = new ArrayList<Integer>();
			result.add(ux);
			result.add(uy);
			return result;
		} 
		
		DijkstraReturn result = Dijkstra(src);
		HashMap<Vertex, Integer> dis = result.getDist();	// dist[] from Dij's
		HashMap<Vertex, Vertex> prev = result.getPrev();	// prev[] from Dji's

		if(TEST_V2V)
			System.out.println(result.toString());
		if ( Integer.MAX_VALUE / dis.get(dest) <=1 ) return new ArrayList<>();
		
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Vertex> vPath = new ArrayList<>();
		Vertex current = dest;
		vPath.add(current);
		while( !prev.get(current).equals(src) ) 
		{
			current = prev.get(current);
			vPath.add(current);
		}
		vPath.add(src);
		for(int i=vPath.size()-1; i>=0; i--) 
		{
			path.add(vPath.get(i).getX());
			path.add(vPath.get(i).getY());
		}		
				
		if(TEST_V2V) 
		{
			System.out.println("\n\n-------------result--------------");
			System.out.println("V2V Path: "+path.toString());
		}
		
		return path;
	}
	
	
	/**
	 * public method for V2S
	 *
	 * @param ux, uy, vx, vy - valid coordinates of vertices u and v in the graph
	 * @param S              - represents a set of vertices. 
	 *
	 * @return ArrayList<Integer> path - ArrayList contains even number of integers, for any even i,
	 * 									 i-th and i+1-th integers in the array represent
	 *									 the x-coordinate and y-coordinate of the i-th vertex
	 *									 in the returned path (path is an ordered sequence of vertices)
	 */
	public ArrayList<Integer> V2S(int ux, int uy, ArrayList<Integer> S)
	{        
        /* Put S into a format of ArrayList<Vertex> */
        /* Initialize variables */
		Vertex src = new Vertex(ux, uy);		// src vertex
		Vertex dest = null;
		ArrayList<Vertex> setVertices = new ArrayList<>();
		for(int i=0; i<S.size(); i=i+2) {
			setVertices.add(new Vertex(S.get(i), S.get(i+1)));
		}
		if( setVertices.contains(src) ) 
		{
			ArrayList<Integer> include = new ArrayList<>();
			include.add(src.getX());
			include.add(src.getY());
			return include;						// if V is part of S, return V
		}
		if( setVertices.isEmpty() ) 
			return new ArrayList<Integer>();	// if S is empty, return empty arraylist
		
		DijkstraReturn result = Dijkstra(src);
		HashMap<Vertex, Integer> dis = result.getDist();
		HashMap<Vertex, Vertex> prev = result.getPrev();
				
		/* Finding the closet vertex among all dis hashamp based on shortest distance */
		PriorityQueue<QueueVertex> postQ = new PriorityQueue<>(vertexCompartor);
		for(HashMap.Entry<Vertex, Integer> entry: dis.entrySet())
		{
			QueueVertex postQv = new QueueVertex(entry.getKey(), entry.getValue()); 
			postQ.add(postQv);
	    }
		while( !postQ.isEmpty() ) 
		{
			QueueVertex temp = postQ.poll();
			if (TEST_V2S) System.out.println(temp.toString());
			
			if(setVertices.contains(temp.getVertex()) && !temp.getVertex().equals(src) ) 
			{
				dest = temp.getVertex();
				break;
			}
		}
		if( dest.equals(null) ) 
		{
			if(TEST_V2S) 
			{
				System.out.println("\n\n-------------result--------------");
				System.out.println("V2S Path: empty arraylist, destination not found!");
			}
			return new ArrayList<Integer>(); // if no desired destination
		}
			
		if ( dis.get(dest)!=0 && Integer.MAX_VALUE / dis.get(dest) <=1 ) 
		{
			if(TEST_V2S) 
			{
				System.out.println("\n\n-------------result--------------");
				System.out.println("V2S Path: empty arraylist, no way to reach");
			}
			return new ArrayList<Integer>();
		}
			
		
		/* Computing the path vector based on the previous result */
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Vertex> vPath = new ArrayList<>();
		Vertex current = dest;
		vPath.add(current);
		while( !prev.get(current).equals(src) ) 
		{
			current = prev.get(current);
			vPath.add(current);
		}
		vPath.add(src);
		for(int i=vPath.size()-1; i>=0; i--) 
		{
			path.add(vPath.get(i).getX());
			path.add(vPath.get(i).getY());
		}
		
		if(TEST_V2S) 
		{
			System.out.println("\n\n-------------result--------------");
			System.out.println("\n\nSRC: "+ src.toString());
			System.out.println("S: "+setVertices.toString());
			System.out.println("Closest vertex in S: " + dest.toString());
			System.out.println("V2S Path: "+path.toString());
		}

		return path;
	}
	
	
	/**
	 * public method for S2S
	 *
	 * @param S1 and S2 represent sets of vertices (see above for the representation of a set of vertices as arrayList)
	 *
	 * @return ArrayList<Integer> path - ArrayList contains even number of integers, for any even i,
	 * 									 i-th and i+1-th integers in the array represent
	 *									 the x-coordinate and y-coordinate of the i-th vertex
	 *									 in the returned path (path is an ordered sequence of vertices)
	 */
	public ArrayList<Integer> S2S(ArrayList<Integer> S1, ArrayList<Integer> S2)
	{
        /* Put S1 & S2 into a format of ArrayList<Vertex> AND adj list */
        ArrayList<Vertex> setVertices1 = new ArrayList<>();
        ArrayList<Vertex> setVertices2 = new ArrayList<>();
		
		Vertex s = new Vertex(-1, -1);
		LinkedList<QueueVertex> sEdge = new LinkedList<>();
		for(int i=0; i<S1.size(); i=i+2) 
		{
			Vertex v = new Vertex(S1.get(i), S1.get(i+1));
			setVertices1.add(v);
			QueueVertex qv = new QueueVertex(v, 0);
			sEdge.add(qv);
		}
		for(int i=0; i<S2.size(); i=i+2) 
		{
			Vertex v = new Vertex(S2.get(i), S2.get(i+1));
			if(setVertices1.contains(v)) 
			{
				ArrayList<Integer> dupVertex = new ArrayList<>();
				dupVertex.add(v.getX());
				dupVertex.add(v.getY());
				if(TEST_S2S) 
				{
					System.out.println("\n\n-------------result--------------");
					System.out.println("S2S Path: "+dupVertex.toString());
				}
				return dupVertex;
			}
			setVertices2.add(v);
		}
		adjList.put(s, sEdge);
		
		if( setVertices1.isEmpty() || setVertices2.isEmpty() ) 
		{
			if(TEST_S2S) 
			{
				System.out.println("\n\n-------------result--------------");
				System.out.println("S2S Path: empty arraylist");
			}
			adjList.remove(s);
			return new ArrayList<Integer>();	// if S is empty, return empty arraylist
		} 
		
		ArrayList<Integer> result1 = this.V2S(s.getX(), s.getY(), S2);
				
		if(result1.size()>1) 
		{
			result1.remove(0);	// remove x of s
			result1.remove(0);	// remove y of s
		}

		if(TEST_S2S) 
		{
			System.out.println("result1: " + result1.toString());
		}
		
		int costFromS1ToS2 = -1;
		int numEdgesFromS1ToS2 = result1.size()/2 - 1;	// example: 8coordinates/2 vertices - 1 edges		
		
		for(int i=0, j=0; i<numEdgesFromS1ToS2; i++, j+=2) 
		{
			Vertex src = new Vertex(result1.get(j), result1.get(j+1));
			Vertex dest = new Vertex(result1.get(j+2), result1.get(j+3));
			LinkedList<QueueVertex> edges = adjList.get(src);
			for(int k=0; k<edges.size(); k++)
			{
				if(edges.get(k).getVertex().equals(dest)) 
				{
					costFromS1ToS2 += edges.get(k).getDistance();
				}
			}
		}
		
		ArrayList<Integer> path = new ArrayList<>();
		
		if( costFromS1ToS2 != -1 ) 	// S1 to S2 is better || S2 cannot reach S1
		{
			path = result1;
		}
		else {	// S1 cannot reach S2 || S2 cannot reach S1
			if(TEST_S2S) System.out.println("S2S Path: "+path.toString());
		}
		
		
		if(TEST_S2S) 
		{
			System.out.println("S1 to S2: " + result1.toString());
			System.out.println("cost= " + costFromS1ToS2);
			System.out.println("-------------result--------------");
			System.out.println("S2S Path: "+path.toString());
		}
		
		adjList.remove(s);
		
		return path;
	}
	
	
	/**
	 * private comparator for QueueVertex
	 * 
	 * compare the distance between two vertices to the same src
	 * this is for default priority queue to act as a min heap queue
	 */
	private Comparator<QueueVertex> vertexCompartor = new Comparator<QueueVertex>() {
		@Override
		public int compare(QueueVertex qv1, QueueVertex qv2) {
			return qv1.getDistance() - qv2.getDistance();
		}
    };
	
    
	/**
	 * private method for Dijkstra's algorithm
	 * 
	 * @param src - Source vertex
	 * @return DijkstraReturn result - dist[] and prev[]
	 */
	private DijkstraReturn Dijkstra(Vertex src) 
	{
		HashMap<Vertex, Integer> dis = new HashMap<>();									// store the dis table
		HashMap<Vertex, Vertex> prev = new HashMap<>();									// store the previous info
		PriorityQueue<QueueVertex> queue = new PriorityQueue<>(vertexCompartor);		// min Queue

		for (Vertex key : adjList.keySet())												// initialize dis & prev & queue
		{
			Vertex v = new Vertex(key);
			QueueVertex qv = new QueueVertex(v, -1);
			if( !v.equals(src) ) {														// all other vertices = infinity
				qv.setDistance(Integer.MAX_VALUE);
				dis.put(v, Integer.MAX_VALUE);
			}
			else {																		// source vertex = 0
				qv.setDistance(0);
				dis.put(v, 0);
			}
			prev.put(v, null);
			queue.add(qv);
		}
				
		while( !queue.isEmpty() ) 
		{
			QueueVertex temp = queue.poll();											// extract the vertex with min dis
			LinkedList<QueueVertex> neighbors = adjList.get(temp.getVertex());			// get all neighbor edges of the v
			
//			System.out.println(temp.toString() + " has " + neighbors.size() + "neighbors");
			
			for (int i = 0; i < neighbors.size(); i++) 
			{
				QueueVertex neighbor = neighbors.get(i);
//				System.out.println("\tneighbor" + i + ": " + neighbor.getVertex().toString());
				
				int distU_weightUV = Math.abs(dis.get(temp.getVertex()) + neighbor.getDistance());
//				System.out.println("\t\t->dis: " + distU_weightUV);
				
				int alt = Math.min(distU_weightUV, Integer.MAX_VALUE);
//				System.out.println("\t\t->alt: " + alt);
				
//				System.out.println("\t\t->dis.get(neighbor.getVertex()): " + dis.get(neighbor.getVertex()));
//				boolean test = alt < dis.get(neighbor.getVertex());
//				System.out.println("\t\t->if branch: " + test);

				if ( alt < dis.get(neighbor.getVertex()) ) 
				{
					dis.put(neighbor.getVertex(), alt);
					prev.put(neighbor.getVertex(), temp.getVertex());
					
					QueueVertex decreseNeighbor = new QueueVertex(neighbor.getVertex(), neighbor.getDistance() - alt);
					queue.remove(neighbor);
					queue.add(decreseNeighbor);
//					System.out.println("\t\t->UPDATE dis.get(neighbor.getVertex()): " + dis.get(neighbor.getVertex()));
				}
			}	
		}
		DijkstraReturn result = new DijkstraReturn(dis, prev);
//		System.out.println("\n\n\n"+result.getDist().toString());

		return result;
	}
	

	/**
	 * Inner class represent a vertex
	 */
	protected class Vertex
	{
	    private int x, y;
	 
	    protected Vertex(int x, int y){
	        this.x = x;
	        this.y = y; 
	    }
	    protected Vertex(Vertex n){
	        this.x = n.getX();
	        this.y = n.getY(); 
	    }
	    protected int getX() {
	    	return x;
	    }
	    protected int getY() {
	    	return y;
	    }
	    @Override
		public boolean equals(Object obj){
	        if (obj!=null && obj instanceof Vertex) {
	        	Vertex vertex = (Vertex) obj;
	            return (vertex.getX()==this.x && vertex.getY() == this.y);
	        } else {
	            return false;
	        }
	    }
		@Override
		public int hashCode() {
			int result = 66;
			result = result * 33 + x;
			result = result * 33 + y;
			return result;
		}
		@Override
		public String toString() {
			return "Vertex: ("+x+", "+y+")";
		}
		
	}

	/**
	 * Inner class represent a QueueVertex
	 */
	protected class QueueVertex
	{
		private Vertex vertex;
	    private int distance;
	 
	    protected QueueVertex(Vertex vertex, int distance){
	    	this.vertex = new Vertex(vertex);
	        this.distance = distance;
	    }
	    protected Vertex getVertex() {
	    	return vertex;
	    }
	    protected int getDistance() {
	    	return distance;
	    }
	    protected void setDistance(int distance) {
	    	this.distance = distance;
	    }
	    @Override
		public String toString() {
			return "QueueVertex: ["+vertex.toString()+"; "+distance+"]";
		}
	}
	
	/**
	 * Inner class represent a Dijkstra return
	 */
	protected class DijkstraReturn
	{
		private HashMap<Vertex, Integer> dist;
		private HashMap<Vertex, Vertex> prev;
	 
	    protected DijkstraReturn(HashMap<Vertex, Integer> dist, HashMap<Vertex, Vertex> prev){
	    	this.dist = dist;
	        this.prev = prev;
	    }
	    protected HashMap<Vertex, Integer> getDist() {
	    	return dist;
	    }
	    protected HashMap<Vertex, Vertex> getPrev() {
	    	return prev;
	    }
		@Override
		public String toString() {
			
			String result = "dist[]: " + dist.toString() + "\n" + "prev[]: " + prev.toString();
			return result;
		}

	}

}
