import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

/**
 * WikiCrawler class
 * @author Hongyi Bian
 * @author Yuanbo Zheng
 */
public class WikiCrawler {

	
	/** Base URL for wikipedia.org */
	static final String BASE_URL = "https://en.wikipedia.org";
	
	/** count number of requests */
	private int requestCount;
	
	/** seed URL */
	private String seed;
	
	/** max number of pages to crawl */
	private int max;
	
	/** list of topics to match */
	private String[] topics;
	
	/** output file name */
	private String output;
	
	
	/**
	 * Constructor
	 * 
	 * @param seed - related address of seed URL (within wiki domain)
	 * @param max - maximum number of pages to consider
	 * @param topics - array of strings representing keywords in a topic-list
	 * @param output - string representing the lename where the web graph over discovered pages are written
	 */
	public WikiCrawler(String seed, int max, String[] topics, String output) 
	{
		this.seed = seed;
		this.max = max;
		this.topics = topics;
		this.output = output;
		this.requestCount = 0;
	}
		
	
	/**
	 * This method is used to extract links related to /wiki/XXX
	 * Only extracting links appears after the first <p> tag
	 * Ignore any link contains '#' or ':'
	 * 
	 * @param document - a string represent HTML of a page
	 * 
	 * @return ArrayList<Sting> - a list of wiki links
	 */
	public ArrayList<String> extractLinks(String document)
	{
		ArrayList<String> links = new ArrayList<>();
		int indexOf_p = document.indexOf("<p>");		// first occurrence of <p>
		int indexOf_P = document.indexOf("<P>");		// first occurrence of <P>
		int indexP = Math.min(indexOf_p, indexOf_P); 	// find the true p/P
		int index = document.indexOf("<a href=\"/wiki/", indexP+1);		// bypass any content before <p> or <P>
		while(index >= 0) 
		{
			String link = "";
			boolean flag = false;
			for(int i=index+9; i<document.length(); i++) 
			{
				if( document.charAt(i) == '"')	// end of the link
				{
					break;
				}
				if( document.charAt(i) == '#' || document.charAt(i) == ':' )	// ignore the link
				{
					flag = true;
					break;
				}
				link += document.charAt(i);	// form the link
			}
			if(!flag) {
				System.out.println("Extracted: "+ link);
				links.add(link);	// add the valid link to list
			}
		    index = document.indexOf("<a href=\"/wiki/", index+1);	// find next link
		}
		
		return links;
	}
	
	
	/**
	 * This method is used to crawls the web pages starting from the seed URL
	 * Crawl the first max number of pages (include the seed page)
	 * Only consider pages that contains every keywords in the Topics[]
	 * If Topics[] is empty, this condition is vacuously considered true
	 * 
	 * @param focused
	 * If focused is false 
	 * 		- explore in a BFS fashion with FIFO queue
	 * If focused is true
	 * 		- explore with priority queue with relevance as the priority of each page
	 */
	public void crawl(boolean focused) 
	{	
		StringBuilder sb = new StringBuilder();
		sb.append(max);		// first line with the max number
		sb.append(System.lineSeparator());
		
		if(!focused) 	// FIFO queue
		{
			ArrayList<String> queue = new ArrayList<>();	// ArrayList to represent FIFO queue
			ArrayList<String> discovered = new ArrayList<>();
			queue.add(seed);
			discovered.add(seed);
			while(!queue.isEmpty()) 
			{
				String currLink = queue.remove(0);	// pop the first link
				String currHTML = parseHTML(currLink);	// parse the HTML for link
				System.out.println("\n\nCurrent on:" + currLink + "\n-----------------------------------\n");
				if ( isValidTopic(currHTML) ) 	// check if the page contains all keywords
				{
					ArrayList<String> written = new ArrayList<>();	// a list with written edges, to avoid rewrite
					ArrayList<String> subLinks = extractLinks(currHTML);	// extract v' from v
					for(String link : subLinks)	// go through every v'
					{
						if (written.contains(link)) continue;	// bypass already written links
						if( discovered.size() < max && !discovered.contains(link) )	// explore new pages
						{
							String subHTML = parseHTML(link);
							if( isValidTopic(subHTML) )	// check if this v' contains every keyword
							{
								queue.add(link);
								discovered.add(link);
								sb.append(currLink +" "+ link);	// print edge
								sb.append(System.lineSeparator());
								written.add(link);
							}
						}
						else if( discovered.contains(link) && !link.equals(currLink))	// complete the web graph
						{
							sb.append(currLink +" "+ link);
							sb.append(System.lineSeparator());
							written.add(link);
						}		
					}
				}
			}
			System.out.println("\n\n\n");
			for(String str : discovered) {
				System.out.println("discovered: "+str);
			}
		}
		else // priority queue 
		{
			PriorityQ queue = new PriorityQ();
			ArrayList<String> discovered = new ArrayList<>();
			
			String seedHTML = parseHTML(seed);	// parse seed HTML
			int seedRel = relevance(seedHTML);	// calculate seed's relevance
			queue.add(seed, seedRel);
			discovered.add(seed);
			
			while(!queue.isEmpty()) 
			{
				String currLink = queue.extractMax();	// pop link with max priority
				System.out.println("\n\nCurrent on:" + currLink + "\n-----------------------------------\n");
				String currHTML = parseHTML(currLink);	// parse link HTML
				if ( isValidTopic(currHTML) ) 	// check if page contains every keyword
				{
					ArrayList<String> written = new ArrayList<>();	// keep track of written links to aovid rewrite
					ArrayList<String> subLinks = extractLinks(currHTML);	// extract v' from v
					for(String link : subLinks)	// go through every v'
					{
						if (written.contains(link)) continue;	// continue of already written
						if( discovered.size() < max && !discovered.contains(link) ) 	// explore the web
						{
							String subHTML = parseHTML(link);
							if( isValidTopic(subHTML) ) 
							{
								int subRel = relevance(subHTML);
								queue.add(link, subRel);
								discovered.add(link);
								sb.append(currLink +" "+ link);
								sb.append(System.lineSeparator());
								written.add(link);
							}
						}
						else if( discovered.contains(link) && !link.equals(currLink))	// complete the web graph
						{
							sb.append(currLink +" "+ link);
							sb.append(System.lineSeparator());
							written.add(link);
						}
					}
				}
			}
			System.out.println("\n\n\n");
			for(String str : discovered) {
				System.out.println("discovered: "+str);
			}
		}

		// write the output to file
		try {
			writeOutput(sb.toString()) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to parse HTML of a given link
	 * Wait for 3 seconds on every 20 requests
	 * @param link - a relevant wiki link in the form of /wiki/XXX
	 * 
	 * @return String - HTML of the given link
	 */
	private String parseHTML(String link) 
	{
		StringBuilder sb = null;
		try 
		{
			if(requestCount >= 20) {
				try {
					Thread.sleep(3000);	// wait for 3 secs on every 20 requests
					requestCount=0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			URL url = new URL(BASE_URL + link);
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			requestCount++;
			String line;
			sb = new StringBuilder();
			while ( (line = br.readLine()) != null ) // parse HTML
			{
				sb.append(line);
				sb.append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * This method is used to check if the given page contains every keyword
	 * 
	 * @return true - if the page contains every keyword in Topics[]
	 * 		   false - if any keyword is not included in the page
	 */
	private boolean isValidTopic(String html) 
	{
		if(topics.length == 0)
			return true;
		for (String s : topics){
		  if (!html.contains(s)){
			return false;
		  }
		}
		return true;
	}
	
	/**
	 * This method is used to calculate the relevance of a given page
	 * Only count words appear after the first <p> tag
	 * 
	 * @return int - relevance of the page
	 */
	private int relevance(String HTML) 
	{
		int count = 0;
		for(String word : topics) // go through every keyword
		{
			int index = HTML.indexOf(word);	// first appearance of the keyword
			while (index >= 0) {
			    count++;
			    index = HTML.indexOf(word, index+word.length());	// next appearance of the keyword
			}
		}
		return count;
	}
	
	/**
	 * This method is used to print the out put of crawling to a file
	 * file name - output from constructor
	 * 
	 * @param str - string to write to the file
	 */
	private void writeOutput(String str) throws FileNotFoundException, UnsupportedEncodingException 
	{
		PrintWriter writer = new PrintWriter(output, "UTF-8");	// write to output
		writer.print(str);
		writer.close();
	}
		
}
