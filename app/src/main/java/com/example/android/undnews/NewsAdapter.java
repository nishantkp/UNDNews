package com.example.android.undnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
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

    /**
     * ViewHolder for list item in res/layout/news_list_item.xml
     * View holder that cashes the views so we do not need to use findViewById every time
     * So scrolling of list view becomes more smooth
     */
    static class ViewHolderListItem{
        TextView sectionName;
        TextView headline;
        ImageView thumbnail;
        View bufferView;
        TextView publishedTime;
        TextView authorName;
    }

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderListItem viewHolder;
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).
                    inflate(R.layout.news_list_item, parent, false);

            viewHolder = new ViewHolderListItem();
            // Find all the views in list item by their IDs
            viewHolder.sectionName = listView.findViewById(R.id.news_section);
            viewHolder.headline = listView.findViewById(R.id.news_headline);
            viewHolder.thumbnail = listView.findViewById(R.id.news_thumbnail);
            viewHolder.bufferView = listView.findViewById(R.id.buffer_view);
            viewHolder.publishedTime = listView.findViewById(R.id.news_time);
            viewHolder.authorName = listView.findViewById(R.id.news_contributor_name);
            // Store the holder with the view
            listView.setTag(viewHolder);
        } else {
            // We've just avoided calling findViewByID every time
            // So when the listView is present just use the ViewHolder
            viewHolder = (ViewHolderListItem) listView.getTag();
        }

        News currentNewsDetail = getItem(position);

        // Set the section name
        assert currentNewsDetail != null;
        viewHolder.sectionName.setText(currentNewsDetail.getSection());

        // Set the headline
        viewHolder.headline.setText(currentNewsDetail.getHeadline());

        // Set the Drawable object
        if (currentNewsDetail.getThumbnail() != null) {
            viewHolder.thumbnail.setVisibility(View.VISIBLE);
            viewHolder.bufferView.setVisibility(View.VISIBLE);
            viewHolder.thumbnail.setImageDrawable(currentNewsDetail.getThumbnail());
        } else {
            // Hide the image view
            viewHolder.thumbnail.setVisibility(View.GONE);
            // Hide the buffer view
            viewHolder.bufferView.setVisibility(View.GONE);
        }

        // Set the published time
        viewHolder.publishedTime.setText(getTimeAndDate(currentNewsDetail.getTime()).toString());

        // If author name is present then set the author name
        if (currentNewsDetail.getAuthor() != null) {
            viewHolder.authorName.setVisibility(View.VISIBLE);
            viewHolder.authorName.setText(currentNewsDetail.getAuthor());
        } else {
            viewHolder.authorName.setVisibility(View.GONE);
        }

        return listView;
    }

    /**
     * This method is used to convert time format into more human readable one
     * This method uses DataUtils class's GetRelativeTimeSpanString method
     *
     * @param timeString 2017-09-13T18:04:29Z
     * @return Time spans in the past are formatted like "42 minutes ago".
     * Time spans in the future are formatted like "In 42 minutes"
     */
    private CharSequence getTimeAndDate(String timeString) {

        // Parse the current date-time string into Date object of format {yyyy-MM-dd'T'hh:mm:ss'Z'}
        Date dateObject = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            dateObject = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date object : getTimeAndDate() method", e);
        }

        // Convert the Date object of format {yyyy-MM-dd'T'hh:mm:ss'Z'} into
        // string format of {yyyy-MM-dd HH:mm:ss}
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outDate
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = outDate.format(dateObject);

        // Parse the date-time string into Date object of format {yyyy-MM-dd HH:mm:ss}
        // And get the time in milliseconds from Date object
        long currentTimeInMillis = 0;
        try {
            Date currentDateObject = outDate.parse(currentDate);
            currentTimeInMillis = currentDateObject.getTime();
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date object : getTimeAndDate() method,e");
        }
        // Returns the date-time in more human readable format
        return DateUtils.getRelativeTimeSpanString(
                currentTimeInMillis
                , System.currentTimeMillis()
                , DateUtils.MINUTE_IN_MILLIS);
    }
}
