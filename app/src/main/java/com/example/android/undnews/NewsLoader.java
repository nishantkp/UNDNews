package com.example.android.undnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * AsyncTaskLoader to fetch data from guardian API in background thread
 * Created by Nishant on 11/13/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoader(Context context, String queryUrl) {
        super(context);
        mUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

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
