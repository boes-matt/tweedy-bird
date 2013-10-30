package com.boes.tweedybird.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.boes.tweedybird.R;
import com.boes.tweedybird.TweetAdapter;
import com.boes.tweedybird.models.Tweet;

public abstract class TweetsListFragment extends Fragment {

	private TweetAdapter adapter;
	private ListView lvTweets;
	private boolean loading;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tweets_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ArrayList<Tweet> tweetItems = new ArrayList<Tweet>();
		adapter = new TweetAdapter(getActivity(), tweetItems);
		lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		
		setupOnScroll();
		setLoading(false);
	}
	
	/*
	 * Implementations of refresh and loadMore must setLoading(boolean) appropriately.
	 */
	public abstract void refresh();
	
	public abstract void loadMore();
	
	protected boolean isLoading() {
		return loading;
	}

	protected void setLoading(boolean loading) {
		this.loading = loading;
	}	
	
	public TweetAdapter getAdapter() {
		return adapter;
	}
	
	private void setupOnScroll() {
		lvTweets.setOnScrollListener(new OnScrollListener() {
	
			private static final int THRESHOLD = 6;
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!isLoading() && 
				    visibleItemCount != 0 && 
				    firstVisibleItem + visibleItemCount > totalItemCount - THRESHOLD) {
					loadMore();					
				}
			}
	
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
	
			}
			
		});
	}
	
}
