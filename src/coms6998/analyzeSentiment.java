package coms6998;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class analyzeSentiment{
	public static String getEvaluation(float s) {
		if(s >= 2.2) {
			return "very positive";
		}
		else if(s >= 1.2 && s < 2.2) {
			return "positive";
		}
		else if(s < 1.2 && s > -1) {
			return "neutral";
		}
		else if(s > -2 && s <= -1) {
			return "negative";
		}
		else if(s <= -2) {
			return "very negative";
		}
		else {
			return null;
		}
	}
	
	public static int getScore(String s) {
		try {
	        BufferedReader ins = new BufferedReader(new FileReader("WEB-INF/AFINN-96.txt"));
	        String str;
	        
	        while ((str = ins.readLine()) != null) {
	        	String[] temp = str.split("\\t");
	        	String word = temp[0].trim();
	        	int score = Integer.parseInt(temp[1].trim());
	            if (word.equals(s)) {
	            	
	            	return score;
	            } 
	        }
	        ins.close();
	    } catch (IOException e) {
	    }						
		return 0;		
	}
	
	public static String fastCompute(String s) {
		try {
	        BufferedReader ins = new BufferedReader(new FileReader("WEB-INF/AFINN-96.txt"));
	        String str;
	        
	        while ((str = ins.readLine()) != null) {
	        	String[] temp = str.split("\\t");
	        	String word = temp[0].trim();
	        	String score = temp[1].trim();
	            if (word.equals(s)) {
	            	if(score.equals("-1")||score.equals("-2")) {
	            		return "negative";
	            	}
	            	
	            	else if(score.equals("-3")) {
	            		return "very negative";
	            	}
	            	
	            	else if(score.equals("1")||score.equals("2")) {
	            		return "positive";
	            	}
	            	else if(score.equals("3")) {
	            		return "very positive";
	            	}
	            	else{
	            		return "neutral";
	            	}
	            }
	            else  {
	            	return "neutral";
	            }
	        }
	        ins.close();
	    } catch (IOException e) {
	    }
		return "neutral";
	}
	
	public static float computeScore(String s) throws IOException {
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
		
		//parse tweets and calculate overall sentiment score
		float score = 0;
		int count = 0;
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
				float tempScore = getScore(temp);
				if (tempScore == 0) {					
				}
				else {
					count++;
					score = score + tempScore;
				}	
				
			}

		}
	    
	    score = score/count;
	    
	    return score;
	}
}
