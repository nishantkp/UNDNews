package com.example.android.undnews.Object;

import android.graphics.drawable.Drawable;

/**
 * Custom object to store details regarding news like headline, section, author,
 * time and image if present
 * Created by Nishant on 11/13/2017.
 */

public class News {
    private String mHeadline;
    private String mSection;
    private String mAuthor;
    private String mTime;
    private Drawable mThumbnail;
    private String mWebUrl;

    /**
     * Constructor for News object
     *
     * @param headline  news headline
     * @param section   section of news(i.e Technology, Sport, Life and Style etc.)
     * @param author    author of article
     * @param time      time at which article published
     * @param thumbnail image related to news
     * @param webUrl    web URL of news item
     */
    public News(String headline, String section, String author, String time, Drawable thumbnail, String webUrl) {
        mHeadline = headline;
        mSection = section;
        mAuthor = author;
        mTime = time;
        mThumbnail = thumbnail;
        mWebUrl = webUrl;
    }

    // Get the headline of news
    public String getHeadline() {
        return mHeadline;
    }

    // Get the section of news
    public String getSection() {
        return mSection;
    }

    // Get the author's name who has written the article
    public String getAuthor() {
        return mAuthor;
    }

    // Get the time at which article published
    public String getTime() {
        return mTime;
    }

    // Get the drawable related to news
    public Drawable getThumbnail() {
        return mThumbnail;
    }

    // Get the web Url of news article
    public String getWebUrl() {
        return mWebUrl;
    }
}