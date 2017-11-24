package com.example.android.undnews;

import android.content.Context;

import java.util.List;

/**
 * News loader form fragments
 * Created by Nishant on 11/21/2017.
 */

public class NewsLoaderFragment
        extends android.support.v4.content.AsyncTaskLoader<List<News>> {

    private String mUrl;

    /**
     * NewsLoaderFragment constructor
     *
     * @param context  context of app
     * @param queryUrl url string as per user input in SearchView
     */
    public NewsLoaderFragment(Context context, String queryUrl) {
        super(context);
        mUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * This method is called to load data from url string in background thread
     *
     * @return News detail in form of List<News>
     */
    @Override
    public List<News> loadInBackground() {

        // If url string is null return null object
        if (mUrl == null) {
            return null;
        }

        // Get the List<News> from URL string queried by user
        List<News> newsData = QueryUtils.fetchNewsData(mUrl);
        return newsData;
    }
}
