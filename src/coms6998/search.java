package coms6998;

import java.io.BufferedReader;  
import java.io.IOException;
import java.io.InputStreamReader;  
import java.io.PrintWriter;
import java.net.URL;  
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;  
import org.json.JSONException;
import org.json.JSONObject;  

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.apphosting.api.DeadlineExceededException;


import javax.servlet.http.*;
   
public class search extends HttpServlet {
	public search() {
	    super();
	//TODO Auto-generated constructor stub
	}
	
	private Key createKey(String name) {
		Key key = KeyFactory.createKey("Queries", name);
		return key;
	}
	
	public String getLocation(String s) throws IOException {
		String l = s.replaceAll(" ", "+");
		
		String urlstr = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ l + "&sensor=false";
		URL url = new URL(urlstr);
		BufferedReader br = new BufferedReader(  
                 new InputStreamReader(  
                 url.openConnection().getInputStream()));
		StringBuffer buff = new StringBuffer();
		int c;  
		while((c=br.read())!=-1)  
		{  
			buff.append((char)c);  
		}  
		br.close();  

		JSONObject js = new JSONObject(buff.toString());
		JSONArray tweets = js.getJSONArray("results");  
        JSONObject tweet;
        String location = "";
        for(int i=0;i<tweets.length();i++) {  
            tweet = tweets.getJSONObject(i);  
            location = tweet.getJSONObject("geometry").getJSONObject("location").get("lat")
            		+","+ tweet.getJSONObject("geometry").getJSONObject("location").get("lng");  
            
        }  
		
		
		
		return location;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, DeadlineExceededException {
		//this part remains the same for 10 queries
		PrintWriter pw = new PrintWriter(resp.getOutputStream());
		String urlstr = "http://search.twitter.com/search.json?";
		String state = req.getParameter("location");
		String query1 = req.getParameter("query").replace(" ", "+");
		String locaNumber = getLocation(state);
		String location = "geocode=" + locaNumber + ",100mi";
		store ds = new store();
		resp.setContentType("text/html");
		pw.println("<html><link type=\"text/css\" rel=\"stylesheet\" href=\"result.css\" />");
		pw.println("<form action=\"/search\" method=\"GET\">");
		pw.println("<div class = \"center\"><input name = \"query\" value = \"\" " +
				"class = \"center text\" placeholder=\"keyword\">&nbsp;&nbsp;&nbsp;&nbsp;");
		
		pw.println("<select name=\"location\" class = \"center text\">");
		pw.println("<option value=\"AL\">Alabama</option>");
		pw.println("<option value=\"AK\">Alaska</option>");
		pw.println("<option value=\"AZ\">Arizona</option>");
		pw.println("<option value=\"AR\">Arkansas</option>");
		pw.println("<option value=\"CA\">California</option>");
		pw.println("<option value=\"CO\">Colorado</option>");
		pw.println("<option value=\"CT\">Connecticut</option>");
		pw.println("<option value=\"DE\">Delaware</option>");
		pw.println("<option value=\"DC\">District of Columbia</option>");
		pw.println("<option value=\"FL\">Florida</option>");
		pw.println("<option value=\"GA\">Georgia</option>");
		pw.println("<option value=\"HI\">Hawaii</option>");
		pw.println("<option value=\"ID\">Idaho</option>");
		pw.println("<option value=\"IL\">Illinois</option>");
		pw.println("<option value=\"IN\">Indiana</option>");
		pw.println("<option value=\"IA\">Iowa</option>");
		pw.println("<option value=\"KS\">Kansas</option>");
		pw.println("<option value=\"KY\">Kentucky</option>");
		pw.println("<option value=\"LA\">Louisiana</option>");
		pw.println("<option value=\"ME\">Maine</option>");
		pw.println("<option value=\"MD\">Maryland</option>");
		pw.println("<option value=\"MA\">Massachusetts</option>");
		pw.println("<option value=\"MI\">Michigan</option>");
		pw.println("<option value=\"MN\">Minnesota</option>");
		pw.println("<option value=\"MS\">Mississippi</option>");
		pw.println("<option value=\"MO\">Missouri</option>");
		pw.println("<option value=\"MT\">Montana</option>");
		pw.println("<option value=\"NE\">Nebraska</option>");
		pw.println("<option value=\"NV\">Nevada</option>");
		pw.println("<option value=\"NH\">New Hampshire</option>");
		pw.println("<option value=\"NJ\">New Jersey</option>");
		pw.println("<option value=\"NM\">New Mexico</option>");
		pw.println("<option value=\"NY\">New York</option>");
		pw.println("<option value=\"NC\">North Carolina</option>");
		pw.println("<option value=\"ND\">North Dakota</option>");
		pw.println("<option value=\"OH\">Ohio</option>");
		pw.println("<option value=\"OK\">Oklahoma</option>");
		pw.println("<option value=\"OR\">Oregon</option>");
		pw.println("<option value=\"PA\">Pennsylvania</option>");
		pw.println("<option value=\"RI\">Rhode Island</option>");
		pw.println("<option value=\"SC\">South Carolina</option>");
		pw.println("<option value=\"SD\">South Dakota</option>");
		pw.println("<option value=\"TN\">Tennessee</option>");
		pw.println("<option value=\"TX\">Texas</option>");
		pw.println("<option value=\"UT\">Utah</option>");
		pw.println("<option value=\"VT\">Vermont</option>");
		pw.println("<option value=\"VA\">Virginia</option>");
		pw.println("<option value=\"WA\">Washington</option>");
		pw.println("<option value=\"WV\">West Virginia</option>");
		pw.println("<option value=\"WI\">Wisconsin</option>");
		pw.println("<option value=\"WY\">Wyoming</option>");
		pw.println("</select>");
		pw.println("&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"image\" id=\"logo\" src=\"https://twitter.com/images/resources/twitter-bird-white-on-blue.png\"/></form></div>");			
		
		pw.println("<br><h1>hot BUZZes extracted for \"" + req.getParameter("query") + "\" in "+ state + " </h1>");
		pw.println("<center><img src=\"http://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + state + "&zoom=6&size=600x300&markers=color:blue%7Clabel:T%7C"
				+ locaNumber + "&sensor=false\"></center>");
		//buzz extraction, get 10 words in the category
		List<String> WORDS = parseWords.buzzExtraction(query1);
		int count = 0;
		String space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		for(String s : WORDS) {
			count++;
			String query = "q=" + s;
			String datastoreKey = s + " " + state;
			//use this key to interact with the datastore
			String key = datastoreKey;
			float score = analyzeSentiment.computeScore(s);
			pw.println("<h2> No." + count + " BUZZ: " + s + space + analyzeSentiment.getEvaluation(score)
					+ space + score + "</h2>");
			/**if the query has been initialized before
			 * then retrieve the record from datastore
			 */
			try {
				if (ds.findQuery(key)!=null) {
					Entity temp = ds.findQuery(key);
					Iterable<Entity> pq = ds.listQueryResults(temp);
						
					if(count%2 == 0) {
						pw.println("<br><table class=\"tablesalt\">");
					}
					else {
						pw.println("<br><table class=\"tablestyle\">");
					}
					
					//System.out.println("TEST LOCAL STORAGE!");
					
					for (Entity result : pq) {
						pw.println("<tr>");
						java.lang.Long Id = (Long) result.getProperty("id");
						String Text = (String) result.getProperty("text");
						java.lang.Long From_user = (Long) result.getProperty("from_user");
						String From_user_name = (String) result.getProperty("from_user_name");
						String Created_at = (String) result.getProperty("Created_at");
						String Location = (String) result.getProperty("location");
						String imageUrl = (String) result.getProperty("imageUrl");
						/*pw.println("<td>" + Id + "</td><td>" + Text + "</td><td>"
								+ From_user + "</td><td>" + From_user_name
								+ "</td><td>" + Created_at + "</td><td>"
								+ Location + "</td></tr>");*/
						pw.println("<td>" + From_user + "</td><td>" + Text + "</td><td><img src=\""
								+ imageUrl + "\" /></td><td>" + From_user_name
								+ "</td></tr>");	
					}
					pw.println("</table>");
						
				    
				}
				
				/**if the query hasn't been initialized before
				 * then search Twitter, store the result in datastore
				 */
				else {
					urlstr = "http://search.twitter.com/search.json?";
					urlstr = urlstr + query + "&" + location;
					URL url = new URL(urlstr);
					BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
					StringBuffer buff = new StringBuffer();
					int c;  
					while((c=br.read())!=-1)  
					{  
						buff.append((char)c);  
					}  
					br.close();  					
											
					//System.out.println("new query here!");
					
					
					//resp.getWriter().println(buff.toString());
					JSONObject js = new JSONObject(buff.toString());
					
					JSONArray tweets = js.getJSONArray("results");  
				    JSONObject tweet;
				    Entity temp = ds.createQuery(key);
				    if(count%2 == 0) {
						pw.println("<br><table class=\"tablesalt\">");
					}
					else {
						pw.println("<br><table class=\"tablestyle\">");
					}
				    for(int i=0;i<tweets.length() || i<6;i++) {  		        	
				            tweet = tweets.getJSONObject(i);
				            int Id = tweet.getInt("id");
							String Text = tweet.getString("text");
							int From_user = tweet.getInt("from_user_id");
							String From_user_name = tweet.getString("from_user");
							String Created_at = tweet.getString("created_at");
							String Location = tweet.get("geo").toString();
							String imageUrl = tweet.getString("profile_image_url");
							ds.localStorage(temp, Id, Text, From_user, From_user_name, Created_at, Location, imageUrl);
							/*pw.println("<td>" + Id + "</td><td>" + Text + "</td><td>"
									+ From_user + "</td><td>" + From_user_name
									+ "</td><td>" + Created_at + "</td><td>"
									+ Location + "</td></tr>");	*/
							pw.println("<td>" + From_user + "</td><td>" + Text + "</td><td><img src=\""
									+ imageUrl + "\" /></td><td>" + From_user_name
									+ "</td></tr>");	
				     }
				    pw.println("</table>");
				    //resp.sendRedirect("test.jsp");
				        
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}										
		
		pw.println("</html>");
		pw.close();
		
		
	}
	
}  