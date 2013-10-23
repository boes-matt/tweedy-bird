package com.boes.tweedybird;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boes.tweedybird.models.Tweet;
import com.boes.tweedybird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {

	private User user;
	private ImageView ivPicture;
	private TextView tvHandle;
	private EditText etTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		user = User.getUser(getIntent().getLongExtra("uid", 0));
		
		ivPicture = (ImageView) findViewById(R.id.ivPicture);
		ImageLoader imageLoader = ImageLoader.getInstance();
		ivPicture.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(user.imageUrl, ivPicture);
		
		tvHandle = (TextView) findViewById(R.id.tvHandle);
		tvHandle.setText(user.handle);
		
		etTweet = (EditText) findViewById(R.id.etTweet);
	}
	
	public void cancelTweet(View v) {
		finish();
	}
	
	public void sendTweet(View v) {
		String body = etTweet.getText().toString();
		TwitterClient client = TweedyBirdApp.getRestClient();
		
		client.postTweet(body, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject response) {
				Tweet.insert(response);
			}
			
		});

		setResult(Activity.RESULT_OK);
		finish();
	}

}
