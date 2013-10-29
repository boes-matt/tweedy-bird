package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.boes.tweedybird.TweedyBirdApp;
import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	private static final String TAG = "UserTimelineFragment";
	
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

		Long uid = getArguments().getLong(KEY_UID);
		TweedyBirdApp.getRestClient().getUserTimeline(uid, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, "Response: " + response.toString());
				ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
				getAdapter().addAll(tweets);
			}
			
		});
	}
	
}
