package com.codepath.apps.twitterClone;

import android.content.Context;

import com.codepath.apps.twitterClone.networking.TwitterClient;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = MainApplication.getTwitterClient();
 *     // use client to send requests to API
 *
 */
public class MainApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		MainApplication.context = this;
	}

	public static TwitterClient getTwitterClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, MainApplication.context);
	}
}