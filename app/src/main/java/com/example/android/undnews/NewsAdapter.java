package com.example.android.undnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Custom Adapter to update the list-item view
 * Created by Nishant on 11/13/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    // Tag for log messages
    private static final String LOG_TAG = NewsAdapter.class.getName();

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).
                    inflate(R.layout.news_list_item, parent, false);
        }

        News currentNewsDetail = getItem(position);

        // Find the TextView with id news_section and set the section name
        TextView sectionName = listView.findViewById(R.id.news_section);
        assert currentNewsDetail != null;
        sectionName.setText(currentNewsDetail.getSection());

        // Find the text view with id news_headline and set the headline
        TextView headline = listView.findViewById(R.id.news_headline);
        headline.setText(currentNewsDetail.getHeadline());

        // Find the ImageView with id news_thumbnail and set the Drawable object
        ImageView thumbnail = listView.findViewById(R.id.news_thumbnail);
        if (currentNewsDetail.getThumbnail() != null) {
            thumbnail.setImageDrawable(currentNewsDetail.getThumbnail());
        } else {
            // Hide the image view
            thumbnail.setVisibility(View.GONE);
            // Hide the buffer view
            View bufferView = listView.findViewById(R.id.buffer_view);
            bufferView.setVisibility(View.GONE);
        }

        // Find the TextView with od news_time and set the published time
        TextView publishedTime = listView.findViewById(R.id.news_time);
        publishedTime.setText(getTimeAndDate(currentNewsDetail.getTime()));

        // If author name is present then,
        // Find the TextView with id news_contributor_name and set the author name
        TextView authorName = listView.findViewById(R.id.news_contributor_name);
        if(currentNewsDetail.getAuthor() != null){
            authorName.setText(currentNewsDetail.getAuthor());
        } else {
            authorName.setVisibility(View.GONE);
        }

        return listView;
    }

    /**
     * This method is used to convert time format into more human readable one
     *
     * @param timeString    2017-09-13T18:04:29Z
     * @return              12 Sept, 2017
     */
    private String getTimeAndDate(String timeString){

        Date dateObject = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            dateObject = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            Log.e(LOG_TAG,"Error parsing date object : getTimeAndDate() method",e);
        }
        SimpleDateFormat outDate = new SimpleDateFormat("h:mm a  dd MMM, yyyy");
        return outDate.format(dateObject);
    }
}
