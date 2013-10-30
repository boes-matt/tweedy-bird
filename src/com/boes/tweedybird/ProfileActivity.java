package com.boes.tweedybird;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boes.tweedybird.fragments.UserTimelineFragment;
import com.boes.tweedybird.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	public static final String KEY_UID = "uid";
	
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Long uid = getIntent().getLongExtra(KEY_UID, 0);
		mUser = User.getUser(uid);
		getActionBar().setTitle("@" + mUser.getHandle());
		populateProfileHeader();
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.container);
		
		if (fragment == null) {
			fragment = UserTimelineFragment.newInstance(uid);
			fm.beginTransaction()
			.add(R.id.container, fragment)
			.commit();
		}
	}
	
	private void populateProfileHeader() {
		ImageView ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
		TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		
		ImageLoader.getInstance().displayImage(mUser.getImageUrl(), ivUserImage);
		tvUserName.setText(mUser.getName());
		tvTagline.setText(mUser.getTagline());
		tvFollowers.setText(mUser.getFollowersCount() + " Followers");
		tvFollowing.setText(mUser.getFollowingCount() + " Following");
	}
	
}
