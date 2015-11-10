package com.codepath.apps.twitterClone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterClone.MainApplication;
import com.codepath.apps.twitterClone.R;
import com.codepath.apps.twitterClone.adapters.EndlessScrollListener;
import com.codepath.apps.twitterClone.adapters.TwitterTweetsAdapter;
import com.codepath.apps.twitterClone.models.TwitterTweet;
import com.codepath.apps.twitterClone.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 20;
    public static final String EXTRA_TWEET = "tweetData";

    private RecyclerView rvTweets;
    private TwitterTweetsAdapter adapter;
    private List<TwitterTweet> mTweets;
    private LinearLayoutManager mManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TwitterClient client = MainApplication.getTwitterClient();
        initUI();
        client.getHomeTimeline(1, handler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            Intent intent = new Intent(this, ComposeTweetActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        mManager =  new LinearLayoutManager(this);
        mTweets = new ArrayList<>();
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        rvTweets.setLayoutManager(mManager);
        rvTweets.addOnScrollListener(new EndlessScrollListener(mManager) {
            @Override
            public void onLoadMore(int current_page) {
                TwitterClient client = MainApplication.getTwitterClient();
                client.getHomeTimeline(current_page, handler);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            TwitterTweet tweet = (TwitterTweet) data.getExtras().getSerializable(EXTRA_TWEET);
            prependTweet(tweet);
            mManager.scrollToPosition(0);
            // Toast the name to display temporarily on screen
            //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
    }
}
