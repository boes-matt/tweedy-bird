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
	
	//@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	@Column(name = "uid")
	private Long uid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "handle")
	private String handle;
	
	@Column(name = "imageUrl")
	private String imageUrl;
	
	@Column(name = "tagline")
	private String tagline;
	
	@Column(name = "followersCount")
	private int followersCount;
	
	@Column(name = "followingCount")
	private int followingCount;

	public User() {
		super();
	}
	
	public User(JSONObject object) {
		try {
			uid = object.getLong("id");
			name = object.getString("name");
			handle = object.getString("screen_name");
			imageUrl = object.getString("profile_image_url");
			tagline = object.getString("description");
			followersCount = object.getInt("followers_count");
			followingCount = object.getInt("friends_count");
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing user json object", e);			
		}
	}
	
	public static User fromJson(JSONObject object, boolean persist) {
		User u = new User(object);
		if (persist) {
			User existing = getUser(u.getUid());
			if (existing != null) return existing;
			else u.save();
		}
		return u;
	}
	
	public static User getUser(Long uid) {
		return new Select().from(User.class).where("uid = ?", uid).executeSingle();		
	}

	public Long getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getHandle() {
		return handle;
	}

	public String getImageUrl() {
		return imageUrl;
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
	
	@Override
	public String toString() {
		return name;
	}

}
