package com.boes.tweedybird;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	// TODO: Load more tweets; Infinite scrolling
	// TODO: Load @User data
	// TODO: Show Tweet timestamp
	
	// TODO: On network failure; offline access
	// TODO: Pull down to refresh
	// TODO: Clear DB menu item for debugging?
	
	// TODO: Link Library sources into Eclipse
	
	private static final String TAG = "TimelineActivity";
	
	private TwitterClient client;
	private ArrayList<Tweet> tweetItems;
	private ListView lvTweets;
	private TweetAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TweedyBirdApp.getRestClient();
		tweetItems = new ArrayList<Tweet>();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = new TweetAdapter(this, tweetItems);
		lvTweets.setAdapter(adapter);
		loadTweets();
	}
	
	private void loadTweets() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				
				// Add tweets to DB
				Tweet.fromJsonArray(response);
				
				// Load items into ListView from DB
				adapter.clear();
				adapter.addAll(Tweet.recentItems());
				Log.d(TAG, "Total items: " + adapter.getCount());
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void composeTweet(MenuItem item) {
    	Intent i = new Intent(this, ComposeActivity.class);
    	startActivityForResult(i, 0);		
	}
	
	public void refresh(MenuItem item) {
		loadTweets();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) loadTweets();
	}
	
}
