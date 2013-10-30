package com.boes.tweedybird.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	// @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	@Column(name = "uid")
	private Long uid;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "timestamp")
	private Long timestamp;
	
	@Column(name = "user")
	private User user;
	
	public Tweet() {
		super();
	}

	public Tweet(JSONObject object) {
		try {
			uid = object.getLong("id");
			body = object.getString("text");
			timestamp = getDate(object.getString("created_at")).getTime();
			user = User.fromJson(object.getJSONObject("user"), true);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing tweet json object", e);
		}
	}
	
	public static Tweet fromJson(JSONObject object, boolean persist) {
		Tweet t = new Tweet(object);
		if (persist) {
			Tweet existing = getTweet(t.getUid());
			if (existing != null) return existing;
			else t.save();
		}
		return t;
	}
	
	public static ArrayList<Tweet> fromJsonArray(JSONArray array, boolean persist) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject object = array.getJSONObject(i);
				Tweet t = Tweet.fromJson(object, persist);
				tweets.add(t);				
			} catch (JSONException e) {
				Log.e(TAG, "Error parsing json array of tweets", e);
			}
		}
		
		return tweets;
	}
	
	public static Tweet getTweet(Long uid) {
		return new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();		
	}
	
	public static List<Tweet> getTweets() {
		return new Select().from(Tweet.class).orderBy("uid DESC").execute();
	}	
	
	public static Tweet getMostRecent() {
		return new Select().from(Tweet.class).orderBy("uid DESC").executeSingle();
	}
	
	public static Tweet getOldest() {
		return new Select().from(Tweet.class).orderBy("uid ASC").executeSingle();
	}
		
	public Long getUid() {
		return uid;
	}

	public String getBody() {
		return body;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public User getUser() {
		return user;
	}
	
	private Date getDate(String dateStr) {
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
	
	@Override
	public String toString() {
		return body;
	}

}
