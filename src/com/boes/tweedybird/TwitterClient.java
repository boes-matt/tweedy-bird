package com.boes.tweedybird;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
	
	// Supported APIs at: https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    
    // Key and secret provided at developer site for specific API
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "3YkPRnjqpLggbryVyWrBQ";
    public static final String REST_CONSUMER_SECRET = "Xwtx1r4pjXF8UCGpUFWJELPZF9MA8oKZ989NHWX8xk";
    public static final String REST_CALLBACK_URL = "oauth://tweedybird";
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
        
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", "25");
    	client.get(apiUrl, params, handler);
    }
    
    public void getHomeTimelineSince(Long sinceId, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("since_id", sinceId.toString());
    	client.get(apiUrl, params, handler);
    }

    public void getHomeTimelineBefore(Long maxId, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("max_id", String.valueOf(maxId - 1));
    	params.put("count", "25");
    	client.get(apiUrl, params, handler);
    }
        
    public void getAuthenticatedUser(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("account/verify_credentials.json");
    	client.get(apiUrl, handler);
    }
    
    public void postTweet(String body, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", body);
    	client.post(apiUrl, params, handler);
    }

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", "25");
    	client.get(apiUrl, params, handler);	
	}
	
	public void getUserTimeline(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", "25");
    	client.get(apiUrl, params, handler);	
	}
    
}