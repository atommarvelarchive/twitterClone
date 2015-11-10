package com.codepath.apps.twitterClone.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.twitterClone.MainApplication;
import com.codepath.apps.twitterClone.R;
import com.codepath.apps.twitterClone.models.TwitterTweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends AppCompatActivity {

    private EditText etComposer;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        etComposer = (EditText) findViewById(R.id.etComposer);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = etComposer.getText().toString();
                if (tweet.length() > 150) {
                    Toast.makeText(ComposeTweetActivity.this, "You're tweet is toooooo long!", Toast.LENGTH_SHORT).show();
                } else {
                    MainApplication.getTwitterClient().postTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            TwitterTweet tweet = new TwitterTweet(response);
                            Intent data = new Intent();

                            // Pass relevant data back as a result
                            data.putExtra(HomeActivity.EXTRA_TWEET, tweet);
                            // Activity finished ok, return the data
                            setResult(RESULT_OK, data); // set result code and bundle data for response
                            finish(); // closes the activity, pass data to parent
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
