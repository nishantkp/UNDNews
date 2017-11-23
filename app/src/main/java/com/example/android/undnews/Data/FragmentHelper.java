package com.example.android.undnews.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.undnews.R;

/**
 * Helper class for fragments
 * Created by Nishant on 11/21/2017.
 */

public class FragmentHelper {

    public FragmentHelper() {
        // Empty constructor so no one can initialize it
    }

    /**
     * This method is called to get the url string to show top headlines as per user preference
     * Generates the url of type
     * "https://content.guardianapis.com/search?q=&show-fields=thumbnail,trailText&page-size=20&show-tags=contributor&order-by=newest&api-key=test";
     *
     * @param thumbnailPreference     thumbnail preference provided by user in Settings
     * @param authorPreference        author preference provided by user in Settings
     * @param articleNumberPreference news article per page provided by user in Settings
     * @return url string to get general top headlines
     */
    public static String getTopHeadlines(boolean thumbnailPreference
            , boolean authorPreference, String articleNumberPreference) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(Constants.URL_PATH);
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
    public static String generateCorrectUrlApi(String userQuery, boolean thumbnailPreference
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
    public static String getSectionTopHeadlines(String section, boolean thumbnailPreference
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
     * This method is called to get the url string to show news for user query query
     * for section as per user preference
     *
     * @param section                 section name according to fragments
     * @param userQuery               query input by user
     * @param thumbnailPreference     thumbnail preference
     * @param authorPreference        author preference
     * @param articleNumberPreference article per page
     * @return url string for provided section as per user preference
     */
    public static String getSectionSearchQueryNews(String section, String userQuery
            , boolean thumbnailPreference, boolean authorPreference
            , String articleNumberPreference) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME);
        builder.authority(Constants.URL_AUTHORITY);
        builder.appendPath(section);
        builder.appendQueryParameter(Constants.URL_QUERY_PARAMETER, userQuery);
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
     *
     * @param context context of the app
     * @return If network is available method returns TRUE otherwise false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null
                && activeNetworkInfo.isConnected();
    }

    /**
     * This method is used to get the user preference from Settings
     *
     * @param context context of the app
     * @return UserPreference object containing all the preferences selected by user in Settings
     */
    public static UserPreference getUserPreference(Context context) {
        // Get the preferences provided by user
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Author name preference
        boolean authorNamePreference = sharedPreferences.getBoolean(
                context.getString(R.string.settings_show_author_name_key), true);
        // News article thumbnail preference
        boolean thumbnailPreference = sharedPreferences.getBoolean(
                context.getString(R.string.settings_show_thumbnail_key), true);
        // Number of articles displayed in one page
        String articleNumberPreference = sharedPreferences.getString(
                context.getString(R.string.settings_number_of_articles_key)
                , context.getString(R.string.settings_number_of_articles_default_value));
        // Sort article by order type
        String orderByPreference = sharedPreferences.getString(
                context.getString(R.string.settings_order_by_key)
                , context.getString(R.string.settings_order_by_default_value));

        return new UserPreference(
                authorNamePreference
                , thumbnailPreference
                , articleNumberPreference
                , orderByPreference);
    }
}
