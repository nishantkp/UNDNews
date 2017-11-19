package com.example.android.undnews.Data;

/**
 * Public Constants
 * Created by Nishant on 11/18/2017.
 */

public class Constants {

    // Constants for making an http connection
    public static final int URL_READ_TIME_OUT = 10000;
    public static final int URL_CONNECT_TIME_OUT = 15000;
    public static final int URL_SUCCESS_RESPONSE_CODE = 200;
    public static final String URL_REQUEST_METHOD = "GET";

    // URL data from building uri
    public static final String URL_SCHEME = "https";
    public static final String URL_AUTHORITY = "content.guardianapis.com";
    public static final String URL_PATH = "search";

    Constants() {
        // Empty constructor so no one can accidentally use it
    }
}
