package com.boes.tweedybird.fragments;

import java.util.HashMap;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.TwitterClient;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {

	private static final String TAG = "HomeTimelineFragment";	
	
	private TwitterClient client;
	private HashMap<String, Long> tweetBounds;	
	private boolean loading;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TweedyBirdApp.getRestClient();
		loading = false;
		
		loadTweets();
		//setupOnScroll();
	}
	
	private void loadTweets() {
		loading = true;
		client.getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				Log.d(TAG, "Length of array: " + response.length());
				
				tweetBounds = Tweet.insert(response);
				getAdapter().addAll(Tweet.getTweets());
				
				Log.d(TAG, "Total items: " + getAdapter().getCount());
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
				getAdapter().addAll(Tweet.getTweetsBefore(tweetBounds.get("oldest")));
				tweetBounds.put("oldest", last);
	
				Log.d(TAG, "Total items: " + getAdapter().getCount());
				loading = false;
			}
			
		});
	}

	/*
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
	*/
	
	private void refreshTweets() {
		loading = true;
		client.getHomeTimelineSince(tweetBounds.get("newest"), new JsonHttpResponseHandler() {
	
			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, response.toString());
				Log.d(TAG, "Length of array: " + response.length());
	
				Long first = Tweet.insert(response).get("newest");    // FIX: Case when response is empty []
				//tweetItems.addAll(0, Tweet.getTweetsSince(tweetBounds.get("newest")));
				getAdapter().clear();
				getAdapter().addAll(Tweet.getTweets());
				tweetBounds.put("newest", first);
				//adapter.notifyDataSetChanged();
				
				Log.d(TAG, "Total items: " + getAdapter().getCount());
				loading = false;
			}
			
		});
	}
	
}
