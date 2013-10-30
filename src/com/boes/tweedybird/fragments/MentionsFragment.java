package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.TwitterClient;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {

	private static final String TAG = "MentionsFragment";
	private TwitterClient client;
	private static final long UNSET = -1;
	private Long maxId;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TweedyBirdApp.getRestClient();		
		maxId = UNSET;
		loadMore();
	}

	@Override
	public void refresh() {
		maxId = UNSET;
		getAdapter().clear();
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
				ArrayList<Tweet> mentions = Tweet.fromJsonArray(array, false);
				if (!mentions.isEmpty()) {
					maxId = mentions.get(mentions.size() - 1).getUid();
					getAdapter().addAll(mentions);
				}
				setLoading(false);
			}
		};
		
		if (maxId == UNSET) client.getMentionsTimeline(handler);
		else client.getMentionsTimelineBefore(maxId, handler);
	}
		
}
