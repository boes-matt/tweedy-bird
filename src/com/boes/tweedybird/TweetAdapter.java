package com.boes.tweedybird;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boes.tweedybird.models.Tweet;
import com.boes.tweedybird.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter<Tweet> {

	private static final String TAG = "TweetsAdapter";

	public TweetAdapter(Context context, ArrayList<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout itemView = (RelativeLayout) convertView;
		if (itemView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			itemView = (RelativeLayout) inflater.inflate(R.layout.item_tweet, parent, false);
		}

		ImageView ivUserPicture = (ImageView) itemView.findViewById(R.id.ivUserPicture);
		TextView tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
		TextView tvUserHandle = (TextView) itemView.findViewById(R.id.tvUserHandle);
		TextView tvDate = (TextView) itemView.findViewById(R.id.tvDate);
		TextView tvTweet = (TextView) itemView.findViewById(R.id.tvTweet);
		
		Tweet tweet = getItem(position);		
		final User user = tweet.getUser();
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		ivUserPicture.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(user.getImageUrl(), ivUserPicture);
		ivUserPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), ProfileActivity.class);
				i.putExtra(ProfileActivity.KEY_UID, user.getUid());
				getContext().startActivity(i);
			}
		});
		
		tvUserName.setText(user.getName());
		tvUserHandle.setText("@" + user.getHandle());
		tvTweet.setText(tweet.getBody());

		Date date = new Date(tweet.getTimestamp());
		String dateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime());
		tvDate.setText(dateStr);

		return itemView;
	}

	
}
