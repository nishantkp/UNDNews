package com.example.android.undnews;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.android.undnews.Data.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private static String LOG_TAG = NewsActivity.class.getName();

    private NewsAdapter mNewsAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private String mCorrectUserQueryApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Menu mMenu;
    // Header for list
    private View mListViewHeader;

    /* User preference for displaying author name */
    private boolean mAuthorNamePreference;

    /* User preference for showing thumbnail for news article */
    private boolean mThumbnailPreference;

    /* User preference for showing number of articles per page */
    private String mArticleNumberPreference;

    /* User preference for ordering news articles */
    private String mOrderByPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        Toolbar toolbar = findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        // Find the progress bar with id progress_bar in list.xml
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Find the TextView with id empty_view_text_box in list.xml
        mEmptyView = findViewById(R.id.empty_view_text_box);
        mEmptyView.setVisibility(View.GONE);

        // Find the swipe to refresh layout in list.xml
        mSwipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        // Set the color scheme of refresh icon
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange)
                , getResources().getColor(R.color.green)
                , getResources().getColor(R.color.blue));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        // Find the layout for list_header
        mListViewHeader = View.inflate(this, R.layout.list_header, null);

        // Find the ListView with id list in list.xml
        ListView listView = findViewById(R.id.list);
        // Attach a header to listView
        listView.addHeaderView(mListViewHeader);
        // Set empty view on ListView in order to display "no data" and "check network connection"
        listView.setEmptyView(mEmptyView);

        // Create a empty custom adapter and set it on ListView
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mNewsAdapter);

        // Attach a listener on list item to open a link for the news item in web browser
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int currentPosition, long l) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                News currentNewsObject = (News) adapterView.getItemAtPosition(currentPosition);
                webIntent.setData(Uri.parse(currentNewsObject.getWebUrl()));
                startActivity(webIntent);
            }
        });

        // Get the user preferences
        getUserPreference();
        // When user first starts the app, make the API URL to show some of the latest news
        mCorrectUserQueryApi = getTopHeadlines();
        // Check a network connection and initialize a loader
        // We have to initialize a loader in NewsActivity in order to load data when activity
        // restarts, meaning when device orientation changes
        checkNetworkConnectionAndInitLoader();

        // Get the search intent
        handleSearchIntent(getIntent());
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "API : " + mCorrectUserQueryApi);
        return new NewsLoader(this, mCorrectUserQueryApi);
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

    /**
     * This method is called to check network connection and initialize the loader
     */
    private void checkNetworkConnectionAndInitLoader() {
        if (isNetworkAvailable()) {
            // If the device is connected to network, make progress bar visible and
            // hide empty text view and initialize the loader
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

    /**
     * This method is called to check network connection and restart the existing loader
     * to load new batch of data
     */
    private void checkNetworkConnectionAndRestartLoader() {
        if (isNetworkAvailable()) {
            // If the device is connected to network, make progress bar visible and
            // hide empty text view and restart the loader
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
        } else {
            // If there is no network, make progress bar invisible and
            // set emptyView TextView text to "Check network connection!"
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(getResources().getString(R.string.news_activity_check_network_connection));
        }
    }

    /**
     * This method is called to generate the correct url string when user provides a search query
     * and user preference
     * Generates the url of type
     * https://content.guardianapis.com/search?q=USER_QUERY&show-fields=thumbnail,trailText&page-size=20&show-tags=contributor&order-by=relevance&api-key=test
     *
     * @param userQuery search query submitted by user
     * @return correct url string as per query
     */
    private String generateCorrectUrlApi(String userQuery) {
        String query;
        // Get the user preferences
        getUserPreference();
        query = userQuery.replaceAll(" ", "%20");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(Constants.URL_PATH);
        builder.appendQueryParameter("q", query);
        if (mThumbnailPreference) {
            builder.appendQueryParameter("show-fields", "thumbnail,trailText");
        } else {
            builder.appendQueryParameter("show-fields", "trailText");
        }
        builder.appendQueryParameter("page-size", mArticleNumberPreference);
        if (mAuthorNamePreference) {
            builder.appendQueryParameter("show-tags", "contributor");
        }
        builder.appendQueryParameter("order-by", mOrderByPreference);
        builder.appendQueryParameter("api-key", "test");
        return builder.toString();
    }

    /**
     * This method is called to get the url string to show top headlines as per user preference
     * Generates the url of type
     * "https://content.guardianapis.com/search?q=&show-fields=thumbnail,trailText&page-size=20&show-tags=contributor&order-by=newest&api-key=test";
     *
     * @return url string for top headlines
     */
    private String getTopHeadlines() {
        // Get the user preferences
        getUserPreference();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(Constants.URL_PATH);
        builder.appendQueryParameter("q", "");
        if (mThumbnailPreference) {
            builder.appendQueryParameter("show-fields", "thumbnail,trailText");
        } else {
            builder.appendQueryParameter("show-fields", "trailText");
        }
        builder.appendQueryParameter("page-size", mArticleNumberPreference);
        if (mAuthorNamePreference) {
            builder.appendQueryParameter("show-tags", "contributor");
        }
        builder.appendQueryParameter("order-by", "newest");
        builder.appendQueryParameter("api-key", "test");
        return builder.toString();
    }

    // Inflate the actionbar with search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        mMenu = menu;
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        // Set SearchView to take full screen width when clicked on
        searchView.setMaxWidth(Integer.MAX_VALUE);

        return true;
    }

    // Add action for items in ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // When user clicks on home icon in ActionBar, destroy the current loader and
            // initialize a new loader with API for top headlines to display top headlines
            case R.id.home:
                // Update the API url to display top headlines
                mCorrectUserQueryApi = getTopHeadlines();
                // Find the TextView in list_header and set the text to search query
                TextView userQueryTextHeader = mListViewHeader.findViewById(R.id.list_header);
                userQueryTextHeader.setText(getResources().getString(R.string.list_header_title));
                // Check the network connection and restart the loader to display top headlines
                mNewsAdapter.clear();
                checkNetworkConnectionAndRestartLoader();
                break;
            case R.id.settings:
                // When user clicks on settings icon in ActionBar,
                // start the explicit intent to SettingsActivity
                Intent settingsIntent = new Intent(NewsActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called to refresh the content of loader
     */
    private void refreshContent() {
        // Check network connection and restart the loader to refresh news headlines
        checkNetworkConnectionAndRestartLoader();
        // Set visibility of ProgressBar to GONE when refreshing the content
        mProgressBar.setVisibility(View.GONE);
    }

    // Receive a intent which is triggered by user when performing a search
    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }

    // When user performs search through search widget, generate the correct API url
    // and load new batch of news articles
    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String userQuery = intent.getStringExtra(SearchManager.QUERY);

            // When user submits the query hide the SearchView
            if (mMenu != null) {
                MenuItem menuItem = mMenu.findItem(R.id.search);
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.onActionViewCollapsed();
            }
            // Find the TextView in list_header and set the text to search query
            TextView userQueryTextHeader = mListViewHeader.findViewById(R.id.list_header);
            userQueryTextHeader.setText(userQuery);

            // Generate a Url API with user query
            mCorrectUserQueryApi = generateCorrectUrlApi(userQuery);

            // Check the network connection and restart the loader
            mNewsAdapter.clear();
            checkNetworkConnectionAndRestartLoader();
        }
    }

    /**
     * This method is called to check network is available or not
     *
     * @return If network is available method returns TRUE otherwise false
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null
                && activeNetworkInfo.isConnected();
    }

    /**
     * This method is used to get the user preference
     */
    private void getUserPreference(){
        // Get the preferences provided by user
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Author name preference
        mAuthorNamePreference = sharedPreferences.getBoolean(
                getString(R.string.settings_show_author_name_key), true);
        // News article thumbnail preference
        mThumbnailPreference = sharedPreferences.getBoolean(
                getString(R.string.settings_show_thumbnail_key), true);
        // Number of articles displayed in one page
        mArticleNumberPreference = sharedPreferences.getString(
                getString(R.string.settings_number_of_articles_key)
                , getString(R.string.settings_number_of_articles_default_value));
        // Sort article by order type
        mOrderByPreference = sharedPreferences.getString(
                getString(R.string.settings_order_by_key)
                , getString(R.string.settings_order_by_default_value));
    }
}

