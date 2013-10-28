package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import com.boes.tweedybird.R;
import com.boes.tweedybird.TweetAdapter;
import com.boes.tweedybird.models.Tweet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TweetsListFragment extends Fragment {

	private TweetAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tweets_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<Tweet> tweetItems = new ArrayList<Tweet>();
		adapter = new TweetAdapter(getActivity(), tweetItems);
		ListView lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);		
	}
	
	public TweetAdapter getAdapter() {
		return adapter;
	}
}
