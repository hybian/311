import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
* This is the implementation of the ImageProcessor
*
*@author Hongyi Bian
*@author Yuanbo Zheng
*/

public class ImageProcessor {

	private boolean TEST = false;	// to enable test prints
	private int H;
	private int W;
	private ArrayList<ArrayList<Pixel>> M;
	private ArrayList<ArrayList<Integer>> I;
	private String graphFile;
	
	
	/**
	 * Constructor ImageProcessor
	 * 
	 * @param FName input file name representing the image
	 */
	public ImageProcessor(String FName) 
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
		
		H = Integer.parseInt(lines.get(0));
		W = Integer.parseInt(lines.get(1));
		M = new ArrayList<ArrayList<Pixel>>();
		
		int k=0;
		for(int i=0; i<H; i++)	// fill M with the pixels as from file
		{
			ArrayList<Pixel> inner = new ArrayList<>();
			for(int j=0; j<W; j++) 
			{
				String[] elements = lines.get(i+2).split(" ");
				Pixel p = new Pixel( Integer.parseInt(elements[k]), Integer.parseInt(elements[k+1]),  Integer.parseInt(elements[k+2]) );
				inner.add(p);				
				k=k+3;
			}
			M.add(inner);
			k=0;
		}
		
		long end_time = System.nanoTime();
		double difference = (end_time - start_time) / 1e6;
		if(TEST) 
		{
			System.out.println("Successfully created " + H + "*" + W + " matrix");
			System.out.println(FName + ": " + difference + "ms");
			
			System.out.println("===========================================");
			System.out.println("Original Image:");
			for(int i=0; i<H; i++) 
			{
				for(int j=0; j<W; j++) 
				{
					System.out.print(M.get(i).get(j).toString() + " ");
				}
				System.out.println();
			}
		} 

		this.graphFile = "ImageToGraph.txt";
		calculateImportance(this.H, this.W, this.M); 
//		outputGraph(this.H, this.W, this.M);
		
	} // end of constructor
	

	/**
	 * public method for getImportance
	 *
	 * @return ArrayList<Integer> I - returns the 2-D matrix I as per its definition
	 */
	public ArrayList<ArrayList<Integer>> getImportance()
	{
		return I;
	}
	

	/**
	 * public method for writeReduced
	 * precondition: W-k > 1, k < W-1
	 *
	 * @param k - k < W-1
	 * @param FName - Compute the new image matrix after reducing the width by k 
	 * 				  Follow the method for reduction described above
	 * 				  Write the result in a file named FName
	 * 				  in the same format as the input image matrix
	 */
	public void writeReduced(int k, String FName) 
	{	
		int newW = W;
		ArrayList<ArrayList<Pixel>> newM = new ArrayList<ArrayList<Pixel>>();
		// deep copy M to newM
		for(int i=0; i<M.size(); i++)	
		{
			ArrayList<Pixel> inner = new ArrayList<>();
			for(int j=0; j<W; j++) 
			{
				Pixel p = new Pixel( M.get(i).get(j).getR(), M.get(i).get(j).getG(), M.get(i).get(j).getB() );
				inner.add(p);				
			}
			newM.add(inner);
		}
			
//		ArrayList<ArrayList<Pixel>> newM = new ArrayList<ArrayList<Pixel>>(M);
		WGraph graph;
		
		int counter = 1;
		while( counter <= k ) 
		{
			
			if(newW==0) break;
			calculateImportance( H, newW, newM); 
			outputGraph(H, newW, newM);	// generate graphFile from here
			graph = new WGraph(graphFile);	// new graph after each cut
			ArrayList<Integer> S1 = new ArrayList<>();
			ArrayList<Integer> S2 = new ArrayList<>();
			
			for(int i=0; i<newW; i++)	// S1 = first row; S2 = last row; all coordinates
			{
				S1.add(0);
				S1.add(i);
				S2.add(H-1);
				S2.add(i);
			}
			ArrayList<Integer> minCost = graph.S2S(S1,S2);	// calculate shortest path, i.e. minCost
				
			int toRemove = minCost.size() / 2;
			if( toRemove != H ) System.out.println("Error");	// some row not cut
			for(int i=0, j=1; i<H; i++, j+=2) 
			{
				int col = minCost.get(j);
				newM.get(i).remove( col );	// remove the column from tempM, no change to M
			}
			
			newW--;	// width - 1 each cut
			
			if(TEST) 
			{
				System.out.println("===========================================");
				System.out.println("CUT#" + counter + ": ");
				System.out.println("New Width: " + newW);
				System.out.println("minCost: " + minCost.toString());
				for(int i=0; i<H; i++) 
				{
					for(int j=0; j<newW; j++) 
					{
						System.out.print(newM.get(i).get(j).toString() + "\t");
					}
					System.out.println();
				}
			}
			counter++;
		}
		
		outputImage(H, newW, newM, FName); 
	}
	
	
	/**
	 * private method for PDist
	 * 
	 * @param p1, p2 - two pixel objects
	 * @return int - PDist defined as in the description
	 */
	private int PDist(Pixel p1, Pixel p2) 
	{
		int r = (p1.getR() - p2.getR()) * (p1.getR() - p2.getR());
		int g = (p1.getG() - p2.getG()) * (p1.getG() - p2.getG());
		int b = (p1.getB() - p2.getB()) * (p1.getB() - p2.getB());
		return r+g+b;
	}
	
	private void calculateImportance(int H, int W, ArrayList<ArrayList<Pixel>> M) 
	{
		I = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<H; i++) 
		{
			ArrayList<Integer> inner = new ArrayList<>();
			for(int j=0; j<W; j++) 
			{
				int XImportance = 0;
				int YImportance = 0;
				
				// computing for YImportance
				if( i==0 ) {
					YImportance =  PDist(M.get(H-1).get(j), M.get(i+1).get(j));
				}
				else if( i == H-1 ) {
					YImportance =  PDist(M.get(i-1).get(j), M.get(0).get(j));
				}
				else {
					YImportance =  PDist(M.get(i-1).get(j), M.get(i+1).get(j));
				}
				
				// computing for XImportance
				if( j==0 ) {
					XImportance =  PDist(M.get(i).get(W-1), M.get(i).get(j+1));
				}
				else if( j == W-1 ) {
					XImportance =  PDist(M.get(i).get(j-1), M.get(i).get(0));
				}
				else {
					XImportance =  PDist(M.get(i).get(j-1), M.get(i).get(j+1));
				}
				
				// computing for Importance
				inner.add(XImportance + YImportance);
			}
			I.add(inner);
		}
		
		if(TEST) 
		{
			// print the importance grid
			System.out.println("===========================================");
			System.out.println("Importance: ");
			for(int i=0; i<H; i++) 
			{
				for(int j=0; j<W; j++) 
				{
					System.out.print(I.get(i).get(j) + "\t");
				}
				System.out.println();
			}
		}
	}

	private void outputImage(int newH, int newW, ArrayList<ArrayList<Pixel>> newM, String FName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(newH);		// first line = new image H
		sb.append(System.lineSeparator());
		sb.append(newW);		// second line = new image W
		sb.append(System.lineSeparator());
		
		for(int i=0; i<newH; i++) 
		{
			for(int j=0; j<newW; j++) 
			{
				String R = String.valueOf( newM.get(i).get(j).getR() );
				String G = String.valueOf( newM.get(i).get(j).getG() );
				String B = String.valueOf( newM.get(i).get(j).getB() );
				sb.append(R + " " + G + " " + B);
				if(j!=newW-1)
					sb.append(" ");
			}
			if(i!=newH-1)
				sb.append(System.lineSeparator());
		}
		
		// write the image to file
		try {
			writeOutput(sb.toString(), FName) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void outputGraph(int H, int W, ArrayList<ArrayList<Pixel>> M) 
	{
		int numVertices = H*W;
		int numEdges = (H-1)*4 + (W-2)*3*(H-1);
		StringBuilder sb = new StringBuilder();
		sb.append(numVertices);		// first line = number of V
		sb.append(System.lineSeparator());
		sb.append(numEdges);		// second line = number of E
		sb.append(System.lineSeparator());
		
		for(int i=0; i<H; i++) 
		{
			for(int j=0; j<W; j++) 
			{
				if( i == H-1 ) {	// last row has no outgoing edge
					break;
				}
				else if( j == 0 )	// left-most column only has two outgoing edges
				{
					for(int k=0; k<2; k++) 
					{
						String edgeOf_ij = "";
						edgeOf_ij += i + " " + j + " ";
						int cost = -1;
						if(i==0) {
							cost = I.get(i).get(j) + I.get(i+1).get(j+k);	// j+k = bottom/bottom-right index 
						} else {
							cost = I.get(i+1).get(j+k);	// j+k = bottom/bottom-right index

						}
						edgeOf_ij += String.valueOf(i+1) + " " + String.valueOf(j+k) + " " + String.valueOf(cost);
						sb.append(edgeOf_ij);
						sb.append(System.lineSeparator());
					}
				}
				else if( j == W-1 ) // right-most column only has two outgoing edges
				{
					for(int k=1; k>=0; k--) 
					{
						String edgeOf_ij = "";
						edgeOf_ij += i + " " + j + " ";
						int cost = -1;
						if(i==0) {
							cost = I.get(i).get(j) + I.get(i+1).get(j-k);	// j-k = bottom/bottom-left index
						} else {
							cost = I.get(i+1).get(j-k);	// j-k = bottom/bottom-left index
						}
						edgeOf_ij += String.valueOf(i+1) + " " + String.valueOf(j-k) + " " + String.valueOf(cost);
						sb.append(edgeOf_ij);
						sb.append(System.lineSeparator());
					}
				}
				else	// other columns/rows got three outgoing edges
				{
					for(int k=-1; k<2; k++) 
					{
						String edgeOf_ij = "";
						edgeOf_ij += i + " " + j + " ";
						int cost = -1;
						if(i==0) {
							cost = I.get(i).get(j) + I.get(i+1).get(j+k);	// j+k = bottom/bottom-right index
						} else {
							cost = I.get(i+1).get(j+k);	// j+k = bottom/bottom-right index
						}
						edgeOf_ij += String.valueOf(i+1) + " " + String.valueOf(j+k) + " " + String.valueOf(cost);
						sb.append(edgeOf_ij);
						sb.append(System.lineSeparator());
					}
				}
			}
		}
		
		// write the graph to file
		try {
			writeOutput(sb.toString(), graphFile) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to print the output to a file
	 * 
	 * @param str - string to write to the file
	 * @param filename - output file name
	 */
	private void writeOutput(String str, String fileName) throws FileNotFoundException, UnsupportedEncodingException 
	{
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");	// write to output
		writer.print(str);
		writer.close();
	}
	
	/**
	 * Inner class represent a pixel
	 */
	protected class Pixel
	{
		private int R;
		private int G;
	    private int B;
	 
	    protected Pixel(int R, int G, int B){
	    	this.R = R;
	    	this.G = G;
	        this.B = B;
	    }
	    protected int getR() {
	    	return R;
	    }
	    protected int getG() {
	    	return G;
	    }
	    protected int getB() {
	    	return B;
	    }
	    @Override
		public String toString() {
			return "Pixel: ["+R+ "," +G+ "," +B+"]";
		}
	}
	
	
}

