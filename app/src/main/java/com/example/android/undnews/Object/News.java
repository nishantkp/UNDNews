package com.example.android.undnews.Object;

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
    private String mWebUrl;
    private String mThumbnailUrl;

    /**
     * Constructor for News object
     *
     * @param headline  news headline
     * @param section   section of news(i.e Technology, Sport, Life and Style etc.)
     * @param author    author of article
     * @param time      time at which article published
     * @param webUrl    web URL of news item
     * @param thumbnailUrl url string for thumbnail
     */
    public News(String headline, String section, String author, String time, String webUrl, String thumbnailUrl) {
        mHeadline = headline;
        mSection = section;
        mAuthor = author;
        mTime = time;
        mWebUrl = webUrl;
        mThumbnailUrl = thumbnailUrl;
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

    // Get the web Url of news article
    public String getWebUrl() {
        return mWebUrl;
    }

    // Get the url for news article thumbnail
    public String getThumbnailUrl(){
        return mThumbnailUrl;
    }
}