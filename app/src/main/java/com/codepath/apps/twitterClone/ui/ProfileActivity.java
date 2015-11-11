package com.codepath.apps.twitterClone.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterClone.MainApplication;
import com.codepath.apps.twitterClone.R;
import com.codepath.apps.twitterClone.models.TwitterUser;
import com.codepath.apps.twitterClone.networking.TwitterClient;
import com.loopj.android.http.RequestParams;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_TUSER = "twitterUser";
    private TwitterUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        mUser = (TwitterUser) intent.getSerializableExtra(EXTRA_TUSER);

        ((ImageView) findViewById(R.id.ivProfilePic)).setImageURI(Uri.parse(mUser.profileImageUrl));
        ((TextView) findViewById(R.id.tvName)).setText(mUser.name);
        ((TextView) findViewById(R.id.tvFollowers)).setText(mUser.followersCount + "Followers");
        ((TextView) findViewById(R.id.tvFollowing)).setText(mUser.friendsCount + "Following");
        ((TextView) findViewById(R.id.tvBio)).setText(mUser.description);

        //Tweets Fragment
        TwitterClient  client = MainApplication.getTwitterClient();
        RequestParams params = new RequestParams();
        params.put("screen_name", mUser.screenName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, TweetsFragment.newInstance(client.getUserTimeLineUrl(), mUser));
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
