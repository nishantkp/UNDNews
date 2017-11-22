package com.example.android.undnews.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Helper class for fragments
 * Created by Nishant on 11/21/2017.
 */

public class FragmentHelper {

    public FragmentHelper() {
        // Empty constructor so no one can initialize it
    }

    /**
     * This method is called to generate the correct url string when user provides a search query
     * and user preference
     * Generates the url of type
     * https://content.guardianapis.com/search?q=USER_QUERY&show-fields=thumbnail,trailText&page-size=20&show-tags=contributor&order-by=relevance&api-key=test
     *
     * @param userQuery               query submitted by user from search widget
     * @param thumbnailPreference     thumbnail preference
     * @param authorPreference        author name preference
     * @param articleNumberPreference article numbers per page
     * @param orderByPreference       order by preference
     * @return url string as per user query
     */
    public String generateCorrectUrlApi(String userQuery, boolean thumbnailPreference
            , boolean authorPreference, String articleNumberPreference, String orderByPreference) {
        String query;
        query = userQuery.replaceAll(" ", "%20");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(Constants.URL_PATH);
        builder.appendQueryParameter(Constants.URL_QUERY_PARAMETER, query);
        if (thumbnailPreference) {
            builder.appendQueryParameter(Constants.URL_SHOW_FIELDS, Constants.URL_SHOW_FIELDS_WITH_THUMBNAIL);
        } else {
            builder.appendQueryParameter(Constants.URL_SHOW_FIELDS, Constants.URL_SHOW_FIELDS_NO_THUMBNAIL);
        }
        builder.appendQueryParameter(Constants.URL_PAGE_SIZE, articleNumberPreference);
        if (authorPreference) {
            builder.appendQueryParameter(Constants.URL_SHOW_TAGS, Constants.URL_SHOW_TAGS_CONTRIBUTOR);
        }
        builder.appendQueryParameter(Constants.URL_ORDER_BY, orderByPreference);
        builder.appendQueryParameter(Constants.URL_API_KEY, Constants.URL_API_KEY_VALUE);
        return builder.toString();
    }

    /**
     * This method is called to get the url string to show top headlines
     * for section as per user preference
     *
     * @param section                 section name according to fragments
     * @param thumbnailPreference     thumbnail preference
     * @param authorPreference        author preference
     * @param articleNumberPreference article per page
     * @return url string for provided section as per user preference
     */
    public String getSectionTopHeadlines(String section, boolean thumbnailPreference
            , boolean authorPreference, String articleNumberPreference) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(section);
        builder.appendQueryParameter(Constants.URL_QUERY_PARAMETER, "");
        if (thumbnailPreference) {
            builder.appendQueryParameter(Constants.URL_SHOW_FIELDS, Constants.URL_SHOW_FIELDS_WITH_THUMBNAIL);
        } else {
            builder.appendQueryParameter(Constants.URL_SHOW_FIELDS, Constants.URL_SHOW_FIELDS_NO_THUMBNAIL);
        }
        builder.appendQueryParameter(Constants.URL_PAGE_SIZE, articleNumberPreference);
        if (authorPreference) {
            builder.appendQueryParameter(Constants.URL_SHOW_TAGS, Constants.URL_SHOW_TAGS_CONTRIBUTOR);
        }
        builder.appendQueryParameter(Constants.URL_ORDER_BY, Constants.URL_ORDER_BY_NEWEST);
        builder.appendQueryParameter(Constants.URL_API_KEY, Constants.URL_API_KEY_VALUE);
        return builder.toString();
    }

    /**
     * This method is called to check network is available or not
     * @param context context of the app
     * @return If network is available method returns TRUE otherwise false
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null
                && activeNetworkInfo.isConnected();
    }
}
