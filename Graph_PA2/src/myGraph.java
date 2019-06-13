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

public class myGraph {

	private boolean TEST = true;
	
	private int numVertices;
	private int numEdges;
	private HashMap<Vertex, LinkedList<QueueVertex>> adjList;
	
	
	public myGraph(String FName) 
	{
		long start_time = System.nanoTime();
		ArrayList<String> lines = new ArrayList<>();
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
		
		adjList = new HashMap<Vertex, LinkedList<QueueVertex>>();
		for(int i=0; i<lines.size(); i++) 
		{
			if(i==0) 
				numVertices = Integer.parseInt(lines.get(0));
			else if(i==1)
				numEdges = Integer.parseInt(lines.get(1));
			else 
			{
				String[] elements = lines.get(i).split(" ");				
				Vertex src = new Vertex( Integer.parseInt(elements[0]), Integer.parseInt(elements[1]) );
				Vertex dest = new Vertex( Integer.parseInt(elements[2]), Integer.parseInt(elements[3]) );
				QueueVertex toDest = new QueueVertex( dest, Integer.parseInt(elements[4]) );
								
				if( !adjList.containsKey(dest) ) 
				{
					LinkedList<QueueVertex> list = new LinkedList<>();
					adjList.put(dest, list);
				}
				if( !adjList.containsKey(src) ) 
				{
					LinkedList<QueueVertex> list = new LinkedList<>();
					list.addFirst(toDest);
					adjList.put(src, list);
				}
				else 
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
			System.out.println(FName + ": " + difference + "ms");
		} 
		
	} // end of constructor
	
	
	private Comparator<QueueVertex> vertexCompartor = new Comparator<QueueVertex>() {
		@Override
		public int compare(QueueVertex qv1, QueueVertex qv2) {
			return qv1.getDistance() - qv2.getDistance();
		}
    };
	
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
			if(TEST)
				System.out.println("\n*********" + temp.toString() + "*********");
			LinkedList<QueueVertex> neighbors = adjList.get(temp.getVertex());			// get all neighbor edges of the v
			for (int i = 0; i < neighbors.size(); i++) 
			{
				QueueVertex neighbor = neighbors.get(i);
				int distU_weightUV = Math.abs(dis.get(temp.getVertex()) + neighbor.getDistance());
				int alt = Math.min(distU_weightUV, Integer.MAX_VALUE);
				if ( alt < dis.get(neighbor.getVertex()) ) 
				{
					dis.put(neighbor.getVertex(), alt);
					prev.put(neighbor.getVertex(), temp.getVertex());
				}
				if(TEST) 
				{
					System.out.println(neighbor.toString() + "; Edge weight: " + neighbor.getDistance());
					System.out.println("----------Distance Table-----------");
					System.out.println("(1,2) dis: "+dis.get(new Vertex(1,2)));
					System.out.println("(3,4) dis: "+dis.get(new Vertex(3,4)));
					System.out.println("(5,6) dis: "+dis.get(new Vertex(5,6)));
					System.out.println("(7,8) dis: "+dis.get(new Vertex(7,8)));
					System.out.println("-----------------------------------\n");
				}
			}	
		}
		
		DijkstraReturn result = new DijkstraReturn(dis, prev);
		return result;
	}
	
	
	// pre: ux, uy, vx, vy are valid coordinates of vertices u and v
	// in the graph
	// post: return arraylist contains even number of integers,
	// for any even i,
	// i-th and i+1-th integers in the array represent
	// the x-coordinate and y-coordinate of the i-th vertex
	// in the returned path (path is an ordered sequence of vertices)
	public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy)
	{
		Vertex src = new Vertex(ux, uy);												// src vertex
		Vertex dest = new Vertex(vx, vy);												// dest vertex
		
		if(src.equals(dest)) return new ArrayList<Integer>();
		
		DijkstraReturn result = Dijkstra(src);
		HashMap<Vertex, Integer> dis = result.getDist();
		HashMap<Vertex, Vertex> prev = result.getPrev();

		if(TEST)
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
				
		if(TEST) 
		{
			System.out.println("\n\n-------------result--------------");
			System.out.println("----------Distance Table-----------");
			System.out.println("(1,2) dis: "+dis.get(new Vertex(1,2)));
			System.out.println("(3,4) dis: "+dis.get(new Vertex(3,4)));
			System.out.println("(5,6) dis: "+dis.get(new Vertex(5,6)));
			System.out.println("(7,8) dis: "+dis.get(new Vertex(7,8)));
			System.out.println("-----------------------------------");
			System.out.println("V2V Path: "+path.toString());
		}
		
		return path;
	}
	
	
	// pre: ux, uy are valid coordinates of vertex u from the graph
	// S represents a set of vertices.
	// The S arraylist contains even number of intergers
	// for any even i,
	// i-th and i+1-th integers in the array represent
	// the x-coordinate and y-coordinate of the i-th vertex
	// in the set S.
	// post: same structure as the last method's post.
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
			if (TEST) System.out.println(temp.toString());
			if(setVertices.contains(temp.getVertex()) && !temp.getVertex().equals(src)) 
			{
				dest = temp.getVertex();
				break;
			}
		}
		
		if(dest.equals(null) ) 
			return new ArrayList<Integer>(); // if no desired destination
		if ( dis.get(dest)!=0 && Integer.MAX_VALUE / dis.get(dest) <=1 ) 
			return new ArrayList<Integer>();
		
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
		//if ( path.size()==2 || Integer.MAX_VALUE / dis.get(dest) <=1 ) path = new ArrayList<>(); // no path OR disconnected
				
		if(TEST) 
		{
			System.out.println("\n\n-------------result--------------");
			System.out.println("----------Distance Table-----------");
			System.out.println("(1,2) dis: "+dis.get(new Vertex(1,2)));
			System.out.println("(3,4) dis: "+dis.get(new Vertex(3,4)));
			System.out.println("(5,6) dis: "+dis.get(new Vertex(5,6)));
			System.out.println("(7,8) dis: "+dis.get(new Vertex(7,8)));
			System.out.println("-----------------------------------");
			System.out.println("\n\nSRC: "+ src.toString());
			System.out.println("S: "+setVertices.toString());
			System.out.println("Closest vertex in S: " + dest.toString());
			System.out.println("V2S Path: "+path.toString());
		}

		return path;
	}
	
	
	// pre: S1 and S2 represent sets of vertices (see above for
	// the representation of a set of vertices as arrayList)
	// post: same structure as the last method's post.
	public ArrayList<Integer> S2S(ArrayList<Integer> S1, ArrayList<Integer> S2)
	{
        /* Put S1 & S2 into a format of ArrayList<Vertex> AND adj list */
        ArrayList<Vertex> setVertices1 = new ArrayList<>();
        ArrayList<Vertex> setVertices2 = new ArrayList<>();
		
		Vertex s = new Vertex(-1, -1);
		Vertex t = new Vertex(-2, -2);
		LinkedList<QueueVertex> sEdge = new LinkedList<>();
		LinkedList<QueueVertex> tEdge = new LinkedList<>();
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
				return dupVertex;
			}
			setVertices2.add(v);
			QueueVertex qv = new QueueVertex(v, 0);
			tEdge.add(qv);
		}
		
		adjList.put(s, sEdge);
		adjList.put(t, tEdge);
		
		if( setVertices1.isEmpty() || setVertices2.isEmpty() ) return new ArrayList<Integer>();	// if S is empty, return empty arraylist
		
		ArrayList<Integer> result1 = this.V2S(s.getX(), s.getY(), S2);
		ArrayList<Integer> result2 = this.V2S(t.getX(), t.getY(), S1);
		
		int costFromS1ToS2 = 0;
		int costFromS2ToS1 = 0;
		for(int i=2; i<result1.size(); i+=4) 
		{
			Vertex src = new Vertex(result1.get(i), result1.get(i+1));
			Vertex dest = new Vertex(result1.get(i+2), result1.get(i+3));
			LinkedList<QueueVertex> edges = adjList.get(src);
			for(int j=0; j<edges.size(); j++) {
				if(edges.get(j).getVertex().equals(dest)) {
					costFromS1ToS2 += edges.get(j).getDistance();
				}
			}
		}
		for(int i=2; i<result2.size(); i+=4) 
		{
			Vertex src = new Vertex(result2.get(i), result2.get(i+1));
			Vertex dest = new Vertex(result2.get(i+2), result2.get(i+3));
			LinkedList<QueueVertex> edges = adjList.get(src);
			for(int j=0; j<edges.size(); j++) {
				if(edges.get(j).getVertex().equals(dest)) {
					costFromS2ToS1 += edges.get(j).getDistance();
				}
			}
		}
		ArrayList<Integer> path = new ArrayList<>();
		if( costFromS1ToS2 < costFromS2ToS1 ) 
		{
			path = new ArrayList<Integer>(result1.subList(2, result1.size()));
		}
		else 
		{
			path = new ArrayList<Integer>(result2.subList(2, result1.size()));
		}
		
		
		if(TEST) 
		{
			System.out.println("S1 to S2: " + result1.toString());
			System.out.println("cost= " + costFromS1ToS2);
			System.out.println("S2 to S1: " + result2.toString());
			System.out.println("cost= " + costFromS2ToS1);
			System.out.println("\n\n-------------result--------------");
			System.out.println("S2S Path: "+path.toString());
		}
		
		adjList.remove(s);
		adjList.remove(t);
		
		return path;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Inner class represent a node
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
	 * Inner class represent a edge
	 */
	protected class Edge
	{
		private Vertex src;
		private Vertex dest;
	    private int weight;
	 
	    protected Edge(Vertex src, Vertex dest, int weight){
	    	this.src = new Vertex(src);
	    	this.dest = new Vertex(dest);
	        this.weight = weight;
	    }
	    protected Vertex getSrc() {
	    	return src;
	    }
	    protected Vertex getDest() {
	    	return dest;
	    }
	    protected int getWeight() {
	    	return weight;
	    }
	    @Override
		public String toString() {
			return "Edge: ["+src.toString()+" ==> "+dest.toString()+"]";
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
