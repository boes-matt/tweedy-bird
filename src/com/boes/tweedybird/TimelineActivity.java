package com.boes.tweedybird;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boes.tweedybird.fragments.HomeTimelineFragment;
import com.boes.tweedybird.fragments.MentionsFragment;
import com.boes.tweedybird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	
	// TODO: Debug: Redundant tweets in DB or redundant views?  Save out max_id and since_id in onPause?
	
	// TODO: On network failure -> offline access
	// TODO: Handle network failures.  Display Toasts.
	// TODO: Refactor tweet bounds
		
	private static final String TAG = "TimelineActivity";
	
	private TwitterClient client;	
	private User user;	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
		
		client = TweedyBirdApp.getRestClient();
		loadUser();
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment")
				.setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMentons = actionBar.newTab().setText("Mentions").setTag("MentionsFragment")
				.setIcon(R.drawable.ic_atsign).setTabListener(this);

		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentons);
		actionBar.selectTab(tabHome);
	}

	private void loadUser() {
		client.getAuthenticatedUser(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, response.toString());
				user = User.insert(response);
				getActionBar().setTitle("@" + user.handle);
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
		//refreshTweets();
	}
		
	public void composeTweet(MenuItem item) {
    	Intent i = new Intent(this, ComposeActivity.class);
    	i.putExtra("uid", user.uid);
    	startActivityForResult(i, 0);		
	}
	
	public void onProfileView(MenuItem item) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra(ProfileActivity.KEY_UID, user.getUid());
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			//refreshTweets();			
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction sft = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			// set fragmentContainer in framelayout to home timeline
			sft.replace(R.id.fragmentContainer, new HomeTimelineFragment());
		} else {
			// set fragmentContainer in framelayout to mentions timeline
			sft.replace(R.id.fragmentContainer, new MentionsFragment());
		}
		sft.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
}
