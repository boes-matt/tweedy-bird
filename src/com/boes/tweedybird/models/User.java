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

	public User() {
		super();
	}
	
	public static User insert(JSONObject object) {
		try {
			Long userId = object.getLong("id");
			User u = getUser(userId);
			if (u == null) {
				u = new User();
				u.uid = userId;				
			}
			
			u.name = object.getString("name");
			u.handle = object.getString("screen_name");
			u.imageUrl = object.getString("profile_image_url");	
						
			u.save();
			return u;
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing user json object", e);
			return null;
		}
	}

	public static User getUser(Long uid) {
		return new Select().from(User.class).where("uid = ?", uid).executeSingle();		
	}
		
}
