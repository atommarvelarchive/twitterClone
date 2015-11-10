package com.codepath.apps.twitterClone.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by araiff on 11/8/15.
 */
@Table(name = "TwitterUsers")
public class TwitterUser extends Model implements Serializable {
    @Column(name = "userId")
    public String userId;
    @Column(name = "name")
    public String name;
    @Column(name = "screenName")
    public String screenName;
    @Column(name = "description")
    public String description;
    @Column(name = "followersCount")
    public String followersCount;
    @Column(name = "friendsCount")
    public String friendsCount;
    @Column(name = "profileImageUrl")
    public String profileImageUrl;

    public TwitterUser() {
        super();
    }

    public TwitterUser(JSONObject object) {
        super();

        try {
            this.userId = object.getString("id");
            this.name = object.getString("name");
            this.screenName = object.getString("screen_name");
            this.description = object.getString("description");
            this.followersCount = object.getString("followers_count");
            this.friendsCount = object.getString("friends_count");
            this.profileImageUrl = object.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
