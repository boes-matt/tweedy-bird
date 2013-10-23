package com.boes.tweedybird.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model {

	private static final String TAG = "Tweet";

	@Column(name = "uid")
	public Long uid;
	
	@Column(name = "body")
	public String body;
	
	@Column(name = "timestamp")
	public Long timestamp;
	
	@Column(name = "user")
	public User user;
	
	public Tweet() {
		super();
	}
	
	public static Tweet insert(JSONObject object) {
		try {
			Long tweetId = object.getLong("id");
			Tweet t = getTweet(tweetId);
			if (t != null) return t;
			
			t = new Tweet();
			t.uid = tweetId;
			t.body = object.getString("text");
			t.timestamp = getDate(object.getString("created_at")).getTime();
			t.user = User.insert(object.getJSONObject("user"));

			t.save();
			return t;
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing tweet json object", e);
			return null;
		}
	}
	
	public static HashMap<String, Long> insert(JSONArray array) {
		HashMap<String, Long> idBounds = new HashMap<String, Long>();
		
		try {
			idBounds.put("newest", Tweet.insert(array.getJSONObject(0)).uid);
			for (int i = 1; i < array.length(); i++) {
				Tweet t = insert(array.getJSONObject(i));
				idBounds.put("oldest", t.uid);
			}			
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing json array of tweets", e);
		}
		
		return idBounds;
	}
	
	private static Date getDate(String dateStr) {
		// for example, dateStr is "Tue Aug 28 21:16:23 +0000 2012"
		String pattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
		df.setLenient(false);
		
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			Log.e(TAG, "Error parsing date", e);
			return new Date();
		}
	}

	public static Tweet getTweet(Long uid) {
		return new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();		
	}
	
	public static ArrayList<Tweet> getTweets() {
		return new Select().from(Tweet.class).orderBy("uid DESC").execute();		
	}

	public static ArrayList<Tweet> getTweetsSince(Long sinceId) {
		return new Select().from(Tweet.class).where("uid > ?", sinceId).orderBy("uid DESC").execute();
	}
	
	public static ArrayList<Tweet> getTweetsBefore(Long maxId) {
		return new Select().from(Tweet.class).where("uid < ?", maxId).orderBy("uid DESC").execute();
	}
}
