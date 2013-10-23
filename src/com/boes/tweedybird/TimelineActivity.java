package com.boes.tweedybird;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.boes.tweedybird.models.Tweet;
import com.boes.tweedybird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	
	// TODO: On network failure -> offline access
	// TODO: Pull down to refresh
	// TODO: Counter for 140 characters or less
	// TODO: Link Library sources into Eclipse
	
	private static final String TAG = "TimelineActivity";
	
	private TwitterClient client;
	private ActionBar mActionBar;
	
	private User user;	
	private ArrayList<Tweet> tweetItems;
	private HashMap<String, Long> tweetBounds;
	
	private ListView lvTweets;
	private TweetAdapter adapter;
	private boolean loading;
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		client = TweedyBirdApp.getRestClient();
		tweetItems = new ArrayList<Tweet>();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = new TweetAdapter(this, tweetItems);
		lvTweets.setAdapter(adapter);
		loading = false;
		
		mActionBar = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mActionBar = getActionBar();
		}
		
		loadUser();
		loadTweets();
		setupOnScroll();
	}
	
	private void loadUser() {
		client.getAuthenticatedUser(new JsonHttpResponseHandler() {
			
			@TargetApi(11)
			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, response.toString());
				user = User.insert(response);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mActionBar.setTitle("@" + user.handle);
				}			
			}
			
		});
	}

	private void loadTweets() {
		loading = true;
		client.getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				Log.d(TAG, "Length of array: " + response.length());
				
				tweetBounds = Tweet.insert(response);
				adapter.addAll(Tweet.getTweets());
				
				Log.d(TAG, "Total items: " + adapter.getCount());
				loading = false;
			}
			
		});
	}
		
	private void loadMoreTweets() {
		loading = true;
		client.getHomeTimelineBefore(tweetBounds.get("oldest"), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				Log.d(TAG, "Length of array: " + response.length());
				
				Long last = Tweet.insert(response).get("oldest");
				adapter.addAll(Tweet.getTweetsBefore(tweetBounds.get("oldest")));
				tweetBounds.put("oldest", last);

				Log.d(TAG, "Total items: " + adapter.getCount());
				loading = false;
			}
			
		});
	}
	
	private void setupOnScroll() {
		lvTweets.setOnScrollListener(new OnScrollListener() {

			private static final int THRESHOLD = 6;
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!loading && 
				    visibleItemCount != 0 && 
				    firstVisibleItem + visibleItemCount > totalItemCount - THRESHOLD) {
					loadMoreTweets();					
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
			
		});
	}
	
	private void refreshTweets() {
		loading = true;
		client.getHomeTimelineSince(tweetBounds.get("newest"), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				Log.d(TAG, "Length of array: " + response.length());

				Long first = Tweet.insert(response).get("newest");
				tweetItems.addAll(0, Tweet.getTweetsSince(tweetBounds.get("newest")));
				tweetBounds.put("newest", first);
				adapter.notifyDataSetChanged();
				
				Log.d(TAG, "Total items: " + adapter.getCount());
				loading = false;
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void refresh(MenuItem item) {
		refreshTweets();
	}
		
	public void composeTweet(MenuItem item) {
    	Intent i = new Intent(this, ComposeActivity.class);
    	i.putExtra("uid", user.uid);
    	startActivityForResult(i, 0);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) 
			refreshTweets();
	}
	
}
