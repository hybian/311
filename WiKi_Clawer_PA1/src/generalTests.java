import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generalTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str = "tataTAwocaonimadeshabitadiaowanyi";
		int count = 0;
		String[] topics = {"ta", "TA", "yi"};
		for(String word : topics) 
		{
			int index = str.indexOf(word);
			while (index >= 0) {
			    count++;
//			    HTML = HTML.substring(index + 1);
			    index = str.indexOf(word, index+word.length());
			}
		}
		System.out.println(count);

		
//		String s = "<dd><a href=\"/wiki/wtf\" title=\"WTF\">WTF</a></dd>\r\n" + 
//				"<p>\r\n" + 
//				"<dd><a href=\"/wiki/:wtf\" title=\"WTF\">WTF</a></dd>\\r\\n" + 
//				"<dd><a href=\"/wiki/Iowa_State_Cyclones\" title=\"Iowa State Cyclones\">Iowa State Cyclones</a></dd>\r\n" + 
//				"<dd><a href=\"/wiki/Iowa_State_University_College_of_Business\" title=\"Iowa State University College of Business\">Business</a></dd>\r\n" + 
//				"<dd><a href=\"/wiki/Iowa_State_University_College_of_Engineering\" title=\"Iowa State University College of Engineering\">Engineering</a></dd>\r\n" + 
//				"<dd><a href=\"/https/Iowa_State_University_College_of_Human_Sciences\" title=\"Iowa State University College of Human Sciences\">Human Sciences</a></dd>\r\n" + 
//				"<dd><a href=\"/wiki/#Iowa_State_University_College_of_Liberal_Arts_%26_Sciences\" title=\"Iowa State University College of Liberal Arts &amp; Sciences\">Liberal Arts &amp; Sciences</a></dd>";
//		
		
		//String start = "href=\"";
		//String end = "\"";
		//System.out.println(s.split(start)[1].split(end)[0]);
//		for(int i=0; i<5; i++) 
//		{
//			System.out.println("wtf");
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
	}
		

}
