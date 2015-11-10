package com.codepath.apps.twitterClone.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

// models/TwitterTweet.java
@Table(name = "TwitterTweets")
public class TwitterTweet extends Model implements Serializable{
    // Define database columns and associated fields
    @Column(name = "twitterUser")
    public TwitterUser twitterUser;
    @Column(name = "timestamp")
    public String timestamp;
    @Column(name = "body")
    public String body;

    // Make sure to always define this constructor with no arguments
    public TwitterTweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public TwitterTweet(JSONObject object){
        super();

        try {
            this.twitterUser = new TwitterUser(object.getJSONObject("user"));
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TwitterTweet> fromJson(JSONArray jsonArray) {
        ArrayList<TwitterTweet> twitterTweets = new ArrayList<TwitterTweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            TwitterTweet twitterTweet = new TwitterTweet(tweetJson);
            twitterTweet.save();
            twitterTweets.add(twitterTweet);
        }

        return twitterTweets;
    }
}