Twitter-buzz
============

Buzz extraction and sentiment analysis on Twitter API

1. Google App Engine:
In this assignment you are required to learn and make use of Google App Engine and the services it offers. Use Google App Engine's Java or Python SDK to create an application which performs the below mentioned tasks and is deployed on Google App Engine.
- Download the Java SDK Plugin for Eclipse from: http://code.google.com/appengine/downloads.html#Download_the_Google_Plugin_for_Eclipse
Python SDK from: http://code.google.com/appengine/downloads.html#Google_App_Engine_SDK_for_Python
- Create account with Google App Engine @ https://appengine.google.com/start.
 
2. Twitter API
Read about the Twitter API at: https://dev.twitter.com/
In this assignment, you will have to use the Twitter Search API. You can know more about this here:
Twitter API Documentation: https://dev.twitter.com/docs
Twitter Search: https://dev.twitter.com/docs/using-search
Hint: 
(1) Use Google Geocoding API (https://developers.google.com/maps/documentation/geocoding/) to get geolocation information with given state name. The geolocation information doesn't have to be very accurate.
(2) Use following Get API to specify keyword (i.e. category) and geolocation to get tweets: https://dev.twitter.com/docs/api/1/get/search
 
3. Google DataStore
Use Google DataStore Service to cache the returned tweets: create a DataStore class having data members such as: id, text, from_user, from_user_name, Created_at, location. Based on the Twitter response, extract information from the response and store it into an object instance of DataStore class for each tweet. Later, when you are answering the same query, you should reuse the tweets stored in the DataStore rather than sending a new request to Twitter API.
 
4. Buzz Extraction
Write a buzz extraction module in which you extract the top 10 popular buzzes. You could at the minimum determine the frequency of "significant" words and use that count to determine the top buzzes. You are welcome use more sophisticated approaches. For example, if the category is "sports" - you might find "baseball", "basketball", "football" and "miami heats" as high frequency words in tweets. 
 
5. Sentiment Analysis
Write a sentiment analysis module, in which you find out the major sentiment of tweets related to the each buzz. Use the sentiment dictionary for microblog (http://neuro.imm.dtu.dk/wiki/AFINN) to score major sentiment toward a buzz. You can category the sentiments as "very positive", "positive", "neutral", "negative" and "very negative". For example, if the buzz is "Cloud Computing", the corresponding sentiment might be "very positive".
 
6. What to display:
Display the top 10 buzzes and corresponding sentiments in a tabular format. The columns should include 5 (random) users and their tweets that contributed to the buzz, his/her image. You can retrieve the image while you received the tweet from the Twitter search API. Example:
