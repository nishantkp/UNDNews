package com.example.android.undnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private static String SAMPLE = "https://content.guardianapis.com/search?q=apple&show-fields=thumbnail&api-key=test";
    private static final String API_FIRST_PART = "https://content.guardianapis.com/search?q=";
    private static final String API_SECOND_PART = "&show-fields=thumbnail&page-size=20&show-tags=contributor&api-key=test";

    private static final String QUICK_LINK_API_PART_1 = "https://content.guardianapis.com/";
    private static final String QUICK_LINK_API_PART_2 = "?&show-fields=thumbnail&page-size=20&show-tags=contributor&api-key=test";

    private static String LOG_TAG = NewsActivity.class.getName();

    private ListView mListView;
    private NewsAdapter mNewsAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private EditText mUserInputQuery;
    private Button mSearchButton;
    private String mCorrectUserQueryApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        // Find the progress bar with id progress_bar in list.xml
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Find the TextView with id empty_view_text_box in list.xml
        mEmptyView = findViewById(R.id.empty_view_text_box);
        mEmptyView.setVisibility(View.GONE);

        mSwipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange)
                , getResources().getColor(R.color.green)
                , getResources().getColor(R.color.blue));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        // Find the ListView with id list in list.xml
        mListView = findViewById(R.id.list);
        // Set empty view on ListView in order to display "no data" and "check network connection"
        mListView.setEmptyView(mEmptyView);

        // Create a empty custom adapter and set it on ListView
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        mListView.setAdapter(mNewsAdapter);

        // Check a network connection and initialize a loader
        // We have to initialize a loader in NewsActivity in order to load data when activity
        // restarts, meaning when device orientation changes
        checkNetworkConnectionAndInitLoader();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "API : " + mCorrectUserQueryApi);
        return new NewsLoader(this, SAMPLE);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mNewsAdapter.clear();
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(getResources().getString(R.string.news_activity_no_content));

        if (data != null && !data.isEmpty()) {
            mNewsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }

    private void checkNetworkConnectionAndInitLoader() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetworkInfo != null
                && activeNetworkInfo.isConnected();

        if (isConnected) {
            // If the device is connected to network, make progress bar visible and
            // hide empty text view
            // Initialize the loader
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, NewsActivity.this);
        } else {
            // If there is no network, make progress bar invisible and
            // set emptyView TextView text to "Check network connection!"
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(getResources().getString(R.string.news_activity_check_network_connection));
        }
    }

    private String generateCorrectUrlApi(String userQuery) {
        String query;
        query = userQuery.replaceAll(" ", "%20");
        Log.i(LOG_TAG, "\nUser query : " + userQuery);
        Log.i(LOG_TAG, "\nCorrect User Api : " + API_FIRST_PART + query + API_SECOND_PART);

        return API_FIRST_PART + query + API_SECOND_PART;
    }

    // This method is called to refresh the content of loader
    private void refreshContent(){
        // Destroy the loader manager in order to create a new loader to load new batch of data
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        SAMPLE = "https://content.guardianapis.com/search?q=google&show-fields=thumbnail&api-key=test";
        checkNetworkConnectionAndInitLoader();
    }
}

