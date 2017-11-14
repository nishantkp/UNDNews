package com.example.android.undnews;

import android.graphics.drawable.Drawable;
import android.util.Log;

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
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
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
     * @param   jsonResponse JSON response received from API in String format
     * @return  List of news
     */
    private static List<News> extractFeaturesFromJsonResponse(String jsonResponse) {
        List<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            // If JSON object does not contain "response" return null object
            if (!baseJsonObject.has("response")) {
                return null;
            }

            JSONObject response = baseJsonObject.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject newsObject = resultsArray.getJSONObject(i);

                // Get section name
                String sectionName = newsObject.getString("sectionName");
                // Get Headline
                String newsHeadline = newsObject.getString("webTitle");
                // Get published time
                String publishedTime = newsObject.getString("webPublicationDate");

                Drawable newsThumbnailDrawable = null;
                // Get thumbnail drawable from URL
                if (newsObject.has("fields")) {
                    JSONObject field = newsObject.getJSONObject("fields");
                    String newsThumbnailUrlString;
                    if (field.has("thumbnail")) {
                        newsThumbnailUrlString = field.getString("thumbnail");
                    } else {
                        newsThumbnailUrlString = null;
                    }
                    newsThumbnailDrawable = getDrawable(newsThumbnailUrlString);
                }

                // Get author name
                String authorName = null;
                if (newsObject.has("tags")) {
                    JSONArray tags = newsObject.getJSONArray("tags");
                    if (!tags.isNull(0)) {
                        // If first element of array is not null then get the author name
                        JSONObject tagsObject = tags.getJSONObject(0);
                        if (tagsObject.has("webTitle")) {
                            authorName = tagsObject.getString("webTitle");
                        }
                    }
                }

                Log.i(LOG_TAG, "\nNews HeadLine : " + newsHeadline);
                Log.i(LOG_TAG, "\nSection Name : " + sectionName);
                Log.i(LOG_TAG, "\nPublished Time : " + publishedTime);
                Log.i(LOG_TAG, "\nThumbnail : " + newsThumbnailDrawable);
                Log.i(LOG_TAG, "\nAuthor name : " + authorName);

                newsList.add(new News(newsHeadline, sectionName, authorName, publishedTime, newsThumbnailDrawable));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error creating JSON object : extractFeaturesFromJsonResponse() method", e);
        }
        return newsList;
    }

    // Generates Drawable object from url string
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

