package coms6998;

import java.util.HashMap;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class store{
	
	static DatastoreService datastore;
	
	public store() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	Entity createQuery(String name) {		
		Entity query = new Entity("Queries");
		query.setProperty("query", name);
		datastore.put(query);
		return query;
	}
	
	void localStorage(Entity query, int id, String text, int from_user, String from_user_name, String Created_at, String location, String imageUrl) {
		Entity tweet = new Entity("tweet", query.getKey());
		tweet.setProperty("id", id);
		tweet.setProperty("text", text);
		tweet.setProperty("from_user", from_user);
		tweet.setProperty("from_user_name", from_user_name);
		tweet.setProperty("Created_at", Created_at);
		tweet.setProperty("location", location);
		tweet.setProperty("imageUrl", imageUrl);
		datastore.put(tweet);
		//System.out.println(tweet);
	}
	
	Entity findQuery(String key) throws EntityNotFoundException {
		Query q = new Query("Queries");
		q.addFilter("query", FilterOperator.EQUAL, key);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();	
	}
	
	Iterable<Entity> listQueryResults(Entity query) {
		Query q = new Query("tweet");
		q.setAncestor(query.getKey());
		Iterable<Entity> results = datastore.prepare(q).asIterable(FetchOptions.Builder.withDefaults());			    
		return results;
		/*for (Entity result : pq.asIterable()) {
			  String id = (String) result.getProperty("id");
			  String text = (String) result.getProperty("text");
			  String from_user = (String) result.getProperty("from_user");
			  String from_user_name = (String) result.getProperty("from_user_name");
			  String Created_at = (String) result.getProperty("Created_at");
			  String location = (String) result.getProperty("location");
			  
		}*/
	}
}
