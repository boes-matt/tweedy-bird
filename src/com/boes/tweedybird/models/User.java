package com.boes.tweedybird.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model {

	private static final String TAG = "User";
	
	@Column(name = "uid")
	public Long uid;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "handle")
	public String handle;
	
	@Column(name = "imageUrl")
	public String imageUrl;
	
	@Column(name = "tagline")
	public String tagline;
	
	@Column(name = "followersCount")
	public int followersCount;
	
	@Column(name = "followingCount")
	public int followingCount;

	public User() {
		super();
	}
	
	public User(JSONObject object) {
		try {
			this.uid = object.getLong("id");
			this.name = object.getString("name");
			this.handle = object.getString("screen_name");
			this.imageUrl = object.getString("profile_image_url");
			
			this.tagline = object.getString("description");
			this.followersCount = object.getInt("followers_count");
			this.followingCount = object.getInt("friends_count");
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing user json object", e);
		}		
	}

	public static User insert(JSONObject object) {
		//try {
			/*
			Long userId = object.getLong("id");
			User u = getUser(userId);
			if (u == null) {
				u = new User();
				u.uid = userId;				
			}
			
			u.name = object.getString("name");
			u.handle = object.getString("screen_name");
			u.imageUrl = object.getString("profile_image_url");	
			*/
			
			User u = new User(object);
			u.save();
			return u;
		//} catch (JSONException e) {
			//Log.e(TAG, "Error parsing user json object", e);
			//return null;
		//}
	}

	public static User getUser(Long uid) {
		return new Select().from(User.class).where("uid = ?", uid).executeSingle();		
	}

	public static User fromJson(JSONObject object) {
		return new User(object);
	}

	public String getScreenName() {
		return handle;
	}

	public String getName() {
		return name;
	}
	
	public String getTagline() {
		return tagline;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFollowingCount() {
		return followingCount;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	
	public Long getUid() {
		return uid;
	}
	
}
