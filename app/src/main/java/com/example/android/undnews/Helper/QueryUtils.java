package com.example.android.undnews.Helper;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.android.undnews.Data.Constants;
import com.example.android.undnews.Data.Keys;
import com.example.android.undnews.Object.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Java class to to get JSON response from guardian API and make List<News> of news
 * Created by Nishant on 11/13/2017.
 */

public class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getName();

    // Empty constructor so on one can accidentally use it
    public QueryUtils() {
    }

    /**
     * This method is used to get the news in form of List<News> from url string
     *
     * @param requestedQueryURLString url string generated as per user input in SearchView
     * @return News data in form of List<News>
     */
    public static List<News> fetchNewsData(String requestedQueryURLString) {
        URL url;
        String JSONResponse = null;

        // If requested URL is null return null object
        if (requestedQueryURLString == null) {
            Log.i(LOG_TAG, "Null url is passed");
            return null;
        } else {
            url = generateUrl(requestedQueryURLString);
        }

        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception occurred due to closing input Stream : fetchNewsData() gets from readFromStream() block");
        }

        List<News> newsList;
        if (JSONResponse != null) {
            // If JSON response is not null return newsList List object
            newsList = extractFeaturesFromJsonResponse(JSONResponse);
            return newsList;
        } else {
            // If JSON response is null, return null
            return null;
        }
    }

    /**
     * Generate a URL object from url string
     *
     * @param requestedUrlString url string as per user query
     * @return URL object
     */
    private static URL generateUrl(String requestedUrlString) {
        URL url = null;

        try {
            url = new URL(requestedUrlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating a URL object : generateURL() block", e);
        }
        return url;
    }

    /**
     * Takes URL object and returns Json response
     *
     * @param url valid URL object
     * @return String JSON response
     * @throws IOException for closing input stream
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String JSONResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Constants.URL_REQUEST_METHOD);
            urlConnection.setReadTimeout(Constants.URL_READ_TIME_OUT);
            urlConnection.setConnectTimeout(Constants.URL_CONNECT_TIME_OUT);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == Constants.URL_SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response code : " + urlConnection.getResponseCode());
                // If received any other code(i.e 400) return null JSON response
                JSONResponse = null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error Solving JSON response : makeHttpConnection() block");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
                // Input stream throws IOException when trying to close, that why method signature
                // specify about IOException
            }
        }
        return JSONResponse;
    }

    /**
     * Get the data from InputStream and convert it to string JSON response
     *
     * @param inputStream InputStream which contains JSON response
     * @return JSON response string
     */
    private static String readFromStream(InputStream inputStream) {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    outputString.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error reading line from reader, readFromStream() block", e);
            }
        }
        return outputString.toString();
    }

    /**
     * Generate the list of News by extracting JSON response
     *
     * @param jsonResponse JSON response received from API in String format
     * @return List of news
     */
    private static List<News> extractFeaturesFromJsonResponse(String jsonResponse) {
        List<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            // If JSON object does not contain "response" return null object
            if (!baseJsonObject.has(Keys.JSON_NEWS_OBJECT_KEY)) {
                return null;
            }

            JSONObject response = baseJsonObject.getJSONObject(Keys.JSON_NEWS_OBJECT_KEY);
            JSONArray resultsArray = response.getJSONArray(Keys.JSON_NEWS_ARRAY_KEY);

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject newsObject = resultsArray.getJSONObject(i);

                // Get section name
                String sectionName = newsObject.getString(Keys.JSON_NEWS_SECTION_KEY);
                // Get Headline
                String newsHeadline = newsObject.getString(Keys.JSON_NEWS_HEADLINE_KEY);
                // Get published time
                String publishedTime = newsObject.getString(Keys.JSON_NEWS_PUBLISHED_TIME_KEY);
                // Get web Url
                String webUrl = newsObject.getString(Keys.JSON_NEWS_URL_KEY);

                Drawable newsThumbnailDrawable = null;
                // Get thumbnail drawable from URL
                if (newsObject.has(Keys.JSON_NEWS_FIELDS_KEY)) {
                    JSONObject field = newsObject.getJSONObject(Keys.JSON_NEWS_FIELDS_KEY);
                    String newsThumbnailUrlString;
                    if (field.has(Keys.JSON_NEWS_THUMBNAIL_KEY)) {
                        newsThumbnailUrlString = field.getString(Keys.JSON_NEWS_THUMBNAIL_KEY);
                        newsThumbnailDrawable = getDrawable(newsThumbnailUrlString);
                    } else {
                        newsThumbnailDrawable = null;
                    }
                }

                // Get author name
                String authorName = null;
                if (newsObject.has(Keys.JSON_NEWS_TAGS_KEY)) {
                    JSONArray tags = newsObject.getJSONArray(Keys.JSON_NEWS_TAGS_KEY);
                    if (!tags.isNull(0)) {
                        // If first element of array is not null then get the author name
                        JSONObject tagsObject = tags.getJSONObject(0);
                        if (tagsObject.has(Keys.JSON_NEWS_AUTHOR_NAME_KEY)) {
                            authorName = tagsObject.getString(Keys.JSON_NEWS_AUTHOR_NAME_KEY);
                        }
                    }
                }
                newsList.add(new News(newsHeadline, sectionName, authorName, publishedTime, newsThumbnailDrawable, webUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error creating JSON object : extractFeaturesFromJsonResponse() method", e);
        }
        return newsList;
    }

    /**
     * This method is used to get Drawable object from a url string
     *
     * @param thumbnailUrl url string of a thumbnail retrieved from JSON response
     * @return drawable of a thumbnail
     */
    private static Drawable getDrawable(String thumbnailUrl) {
        URL url = generateUrl(thumbnailUrl);
        Drawable drawable = null;

        try {
            InputStream inputStream = (InputStream) url.getContent();
            drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving content from URL : getDrawable() method", e);
        }
        return drawable;
    }
}

