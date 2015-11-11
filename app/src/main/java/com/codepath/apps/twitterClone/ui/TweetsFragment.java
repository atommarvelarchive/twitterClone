package com.codepath.apps.twitterClone.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterClone.MainApplication;
import com.codepath.apps.twitterClone.R;
import com.codepath.apps.twitterClone.adapters.EndlessScrollListener;
import com.codepath.apps.twitterClone.adapters.TwitterTweetsAdapter;
import com.codepath.apps.twitterClone.models.TwitterTweet;
import com.codepath.apps.twitterClone.models.TwitterUser;
import com.codepath.apps.twitterClone.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by araiff on 11/11/15.
 */
public class TweetsFragment extends Fragment {

    public static final int REQUEST_CODE = 20;
    public static final String EXTRA_TWEET = "tweetData";
    private RecyclerView rvTweets;
    private TwitterTweetsAdapter adapter;
    private List<TwitterTweet> mTweets;
    private LinearLayoutManager mManager;
    private String mSource;
    private TwitterUser mUser;

    private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
            Log.d("DEBUG", "timeline: " + jsonArray.toString());
            List<TwitterTweet> twitterTweets = TwitterTweet.fromJson(jsonArray);
            appendTweets(twitterTweets);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    };

    public static TweetsFragment newInstance(String src, TwitterUser user) {

        TweetsFragment fragment = new TweetsFragment();
        fragment.mSource = src;
        fragment.mUser = user;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        initUI(view);
        TwitterClient client = MainApplication.getTwitterClient();
        RequestParams params = new RequestParams();
        if (mUser != null) {
            params.put("screen_name", mUser.screenName);
        }
        client.getTweetsFromSource(mSource, 1, params, handler);
        return view;
    }

    private void initUI(View view) {
        mManager =  new LinearLayoutManager(view.getContext());
        mTweets = new ArrayList<>();
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        rvTweets.setLayoutManager(mManager);
        rvTweets.addOnScrollListener(new EndlessScrollListener(mManager) {
            @Override
            public void onLoadMore(int current_page) {
                TwitterClient client = MainApplication.getTwitterClient();
                RequestParams params = new RequestParams();
                if (mUser != null) {
                    params.put("screen_name", mUser.screenName);
                }
                client.getTweetsFromSource(mSource, current_page, params, handler);
            }
        });
        adapter = new TwitterTweetsAdapter(mTweets);
        rvTweets.setAdapter(adapter);
    }

    private void appendTweets(List<TwitterTweet> tweets) {
        mTweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

    private void prependTweet(TwitterTweet tweet) {
        mTweets.add(0, tweet);
        adapter.notifyItemInserted(0);
    }


}
