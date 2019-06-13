import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTest {

	static final String BASE_URL = "https://en.wikipedia.org";
	private static ArrayList<String> links;
	private static String document;
	
	private static String test = "<dd><a href=\"/wiki/wtf\" title=\"WTF\">WTF</a></dd>\r\n" + 
			"<p>\r\n" + 
			"<dd><a href=\"/wiki/:wtf\" title=\"WTF\">WTF</a></dd>\\r\\n" + 
			"<dd><a href=\"/wiki/Iowa_State_Cyclones\" title=\"Iowa State Cyclones\">Iowa State Cyclones</a></dd>\r\n" + 
			"<dd><a href=\"/wiki/Iowa_State_University_College_of_Business\" title=\"Iowa State University College of Business\">Business</a></dd>\r\n" + 
			"<dd><a href=\"/wiki/Iowa_State_University_College_of_Engineering\" title=\"Iowa State University College of Engineering\">Engineering</a></dd>\r\n" + 
			"<dd><a href=\"/https/Iowa_State_University_College_of_Human_Sciences\" title=\"Iowa State University College of Human Sciences\">Human Sciences</a></dd>\r\n" + 
			"<dd><a href=\"/wiki/#Iowa_State_University_College_of_Liberal_Arts_%26_Sciences\" title=\"Iowa State University College of Liberal Arts &amp; Sciences\">Liberal Arts &amp; Sciences</a></dd>";
	
	
	public static void main(String[] args) 
	{			
		String seed = "/wiki/Complexity_theory";
		String[] topics = {};
		WikiCrawler wc = new WikiCrawler(seed, 100, topics, "false-100-noTopic.txt");
		WikiCrawler wc_test = new WikiCrawler("/wiki/A.html", 6, topics, "wtf.txt");
		
		wc.crawl(false);
		
	
	
	}
	

	
	
}
