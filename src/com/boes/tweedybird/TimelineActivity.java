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
import com.boes.tweedybird.fragments.TweetsListFragment;
import com.boes.tweedybird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	
	private static final String TAG = "TimelineActivity";
	private static final int REQUEST_COMPOSE = 1;
	
	private TwitterClient client;	
	private User user;	
	private TweetsListFragment tweetsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TweedyBirdApp.getRestClient();
		loadUser();
		setupNavigationTabs();
	}

	private void loadUser() {
		client.getAuthenticatedUser(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, response.toString());
				user = User.fromJson(response, true);
				getActionBar().setTitle("@" + user.getHandle());
			}
			
		});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void refresh(MenuItem item) {
		if (tweetsFragment != null) tweetsFragment.refresh();
	}
		
	public void composeTweet(MenuItem item) {
    	Intent i = new Intent(this, ComposeActivity.class);
    	i.putExtra("uid", user.getUid());
    	startActivityForResult(i, REQUEST_COMPOSE);		
	}
	
	public void onProfileView(MenuItem item) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra(ProfileActivity.KEY_UID, user.getUid());
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_COMPOSE) {
				if (tweetsFragment != null) tweetsFragment.refresh();							
			}
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction sft = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			tweetsFragment = new HomeTimelineFragment();
		} else {
			tweetsFragment = new MentionsFragment();
		}
		sft.replace(R.id.fragmentContainer, tweetsFragment);
		sft.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
	
}
