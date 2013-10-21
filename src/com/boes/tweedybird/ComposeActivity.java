package com.boes.tweedybird;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boes.tweedybird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

	private EditText etTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
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
				// Saved to DB
				Tweet posted = new Tweet(response);
			}
			
		});
		setResult(Activity.RESULT_OK);
		finish();
	}

}
