package com.codepath.apps.twitterClone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterClone.R;
import com.codepath.apps.twitterClone.TwitterUtils;
import com.codepath.apps.twitterClone.models.TwitterTweet;
import com.codepath.apps.twitterClone.models.TwitterUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by araiff on 11/9/15.
 */
public class TwitterTweetsAdapter extends RecyclerView.Adapter<TwitterTweetsAdapter.ViewHolder>{

    private List<TwitterTweet> mTweets;

    public TwitterTweetsAdapter(List<TwitterTweet> tweets) {
        mTweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Context context = viewHolder.ivProfilePic.getContext();
        TwitterTweet tweet = mTweets.get(i);
        TwitterUser user = tweet.twitterUser;

        Picasso.with(context).load(user.profileImageUrl).into(viewHolder.ivProfilePic);
        viewHolder.tvUserName.setText(user.screenName);
        viewHolder.tvPost.setText(tweet.body);
        viewHolder.tvTimeStamp.setText(TwitterUtils.getRelativeTimeAgo(tweet.timestamp));
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfilePic;
        public TextView tvUserName;
        public TextView tvPost;
        public TextView tvTimeStamp;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvPost = (TextView) itemView.findViewById(R.id.tvPost);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);

        }
    }
}
