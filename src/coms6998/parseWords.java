package coms6998;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;


import org.json.JSONArray;
import org.json.JSONObject;

public class parseWords{
/*	public static boolean check_for_word(String word){
	    try {
	        BufferedReader in = new BufferedReader(new FileReader("WEB-INF/dict.txt"));
	        String str;
	        while ((str = in.readLine()) != null) {
	            if (str.equalsIgnoreCase(word)) {
	            	in.close();
	            	return true;
	            } 
	        }
	        in.close();
	    } catch (IOException e) {
	    	
	    }
	    
	return false;
	}*/
	
	static List<String> buzzExtraction(String s) throws IOException {
		
		
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		
		//search recent 100 tweets that contain the query term
		String urlstr = "http://search.twitter.com/search.json?q=" + s;
		URL url = new URL(urlstr);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		StringBuffer buff = new StringBuffer();
		int c;  
		while((c=br.read())!=-1)  
		{  
			buff.append((char)c);  
		}  
		br.close(); 
		
		//parse tweets and calculate frequency
		JSONObject js = new JSONObject(buff.toString());		
		JSONArray tweets = js.getJSONArray("results");  
	    JSONObject tweet;
	    for(int i = 0;i < tweets.length();i++) {  		        	
            tweet = tweets.getJSONObject(i);
			String Text = tweet.getString("text").trim().toLowerCase();
			String regex = "[^a-zA-Z0-9]";
			String tempArray [] = Text.split(regex);
			for(int j = 0; j < tempArray.length; j++) {
				String temp = tempArray[j].toLowerCase();
				//System.out.println(temp);
				//if (check_for_word(temp))
				if(temp.length()>2){
						if(temp != "" && temp != null) {
							Integer freq = (Integer) words.get(temp);
							words.put(tempArray[j], freq == null? 1 : (freq+1));
						}					
					}
				}
				
			}

		
	    
	    //get top 10 buzzes, store them in buzz list
	    List<Map.Entry> list = new ArrayList<Map.Entry>(words.entrySet());
	    System.out.println(list.size());
		Collections.sort(list, new Comparator() {
			public int compare(Object o2, Object o1) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				return ((Comparable) e1.getValue()).compareTo(e2.getValue());
				
			}
		});
		//System.out.println(list.size());
		List<String> buzz = new ArrayList<String>();
		for (int i = 0, j = 0; i < 10; j++) {
			String word = (String) list.get(j).getKey();
			//System.out.println(word);
			if(!StopWords.stopWordsList.contains(word)) {
				buzz.add(word);
				i++;
			}	
			
				
			
			
		}
		
	    return buzz;
	}
}
