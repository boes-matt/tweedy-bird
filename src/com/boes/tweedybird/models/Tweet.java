package com.boes.tweedybird.models;

import java.util.ArrayList;

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

	private static final String KEY_ID = "id_str";
	private static final String KEY_BODY = "text";	
	
	private static final String KEY_USER = "user";
	private static final String KEY_USER_NAME = "name";
	private static final String KEY_USER_HANDLE = "screen_name";
	private static final String KEY_USER_PICTURE = "profile_image_url";
	
	@Column(name = "uid")
	private String uid;
	@Column(name = "userName")
	private String userName;
	@Column(name = "userHandle")
	private String userHandle;
	@Column(name = "userPictureUrl")
	private String userPictureUrl;
	@Column(name = "tweetBody")
	private String tweetBody;
		
	public Tweet() {
		super();
	}
	
	public Tweet(JSONObject json) {
		super();
		
		try {
			uid = json.getString(KEY_ID);
			userName = json.getJSONObject(KEY_USER).getString(KEY_USER_NAME);
			userHandle = json.getJSONObject(KEY_USER).getString(KEY_USER_HANDLE);
			userPictureUrl = json.getJSONObject(KEY_USER).getString(KEY_USER_PICTURE);
			tweetBody = json.getString(KEY_BODY);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "Error parsing json tweet", e);			
		}
		
		save();
	}
	
	public static void fromJsonArray(JSONArray array) {		
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Tweet t = Tweet.byTweetUid(object.getString(KEY_ID));
				if (t == null) { 
					t = new Tweet(object);
				}
			}		
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "Error parsing json response array", e);
		}
	}
	
	public static Tweet byTweetUid(String uid) {
		return new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();
	}
	
	public static ArrayList<Tweet> recentItems() {
		return new Select().from(Tweet.class).orderBy("uid DESC").limit("300").execute();
	}

	public String getUserName() {
		return userName;
	}

	public String getUserHandle() {
		return userHandle;
	}

	public String getUserPictureUrl() {
		return userPictureUrl;
	}

	public String getTweetBody() {
		return tweetBody;
	}
	
}
