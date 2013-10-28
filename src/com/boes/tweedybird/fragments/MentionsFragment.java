package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {

	private static final String TAG = "MentionsFragment";
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TweedyBirdApp.getRestClient().getMentionsTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, "Response: " + response.toString());
				ArrayList<Tweet> mentions = Tweet.fromJsonArray(response);
				getAdapter().addAll(mentions);
			}
			
		});
	}
	
	
}
