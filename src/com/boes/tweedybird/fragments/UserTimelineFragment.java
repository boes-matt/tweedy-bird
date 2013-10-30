package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.TwitterClient;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	private static final String TAG = "UserTimelineFragment";
	private TwitterClient client;
	private static final long UNSET = -1;
	private Long maxId;
	private Long userId;
	
	public static final String KEY_UID = "uid";

	public static UserTimelineFragment newInstance(Long uid) {
		Bundle args = new Bundle();
		args.putLong(KEY_UID, uid);
		UserTimelineFragment fragment = new UserTimelineFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TweedyBirdApp.getRestClient();
		maxId = UNSET;
		userId = getArguments().getLong(KEY_UID);
		loadMore();		
	}

	@Override
	public void loadMore() {
		if (isLoading()) return;
		setLoading(true);
		
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray array) {
				Log.d(TAG, array.toString());
				ArrayList<Tweet> tweets = Tweet.fromJsonArray(array, false);
				if (!tweets.isEmpty()) {
					maxId = tweets.get(tweets.size() - 1).getUid();
					getAdapter().addAll(tweets);
				}
				setLoading(false);
			}
		};
		
		if (maxId == UNSET) client.getUserTimeline(userId, handler);
		else client.getUserTimelineBefore(userId, maxId, handler);
	}
	
	@Override
	public void refresh() {
		
	}
	
}
