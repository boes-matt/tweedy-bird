package com.boes.tweedybird.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.TwitterClient;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {

	private static final String TAG = "HomeTimelineFragment";		
	private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TweedyBirdApp.getRestClient();
		refresh();
	}
	
	@Override()
	public void onResume() {
		super.onResume();
		getAdapter().clear();
		getAdapter().addAll(Tweet.getTweets());
	}
	
	public void loadMore() {
		if (isLoading()) return;
		setLoading(true);
		
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray array) {
				Log.d(TAG, array.toString());
				Tweet.fromJsonArray(array, true);
				getAdapter().clear();
				getAdapter().addAll(Tweet.getTweets());
				setLoading(false);
			}
		};
		
		Tweet oldest = Tweet.getOldest();
		if (oldest == null) client.getHomeTimeline(handler);
		else client.getHomeTimelineBefore(oldest.getUid(), handler);
	}
	
	public void refresh() {
		if (isLoading()) return;
		
		Tweet mostRecent = Tweet.getMostRecent();
		if (mostRecent == null) loadMore();
		else {
			setLoading(true);
			client.getHomeTimelineSince(mostRecent.getUid(), new JsonHttpResponseHandler() {
				
				@Override
				public void onSuccess(JSONArray array) {
					Log.d(TAG, array.toString());
					Tweet.fromJsonArray(array, true);
					getAdapter().clear();
					getAdapter().addAll(Tweet.getTweets());
					setLoading(false);
				}
				
			});
		}	
	}
	
}
