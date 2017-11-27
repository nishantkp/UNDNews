# UNDNews - A news app

Project as a part of nano-degree at [Udacity](https://www.udacity.com).

### Project Rubic

The goal is to create a News Feed app which gives a user regularly-updated news from the internet related to a particular topic, person, or location. The presentation of the information as well as the topic is up to you.

#### Helpers :smiley:

- Guardugn API
- JSON
- Web Intent
- Exception handeling
- AsynckTask
- Network call on background thread
- Swipe list view to refresh
- ViewHolder
- Uri builder
- Gradient drawable
- Navigation drawer
- Shared preference
- Fragments
- Piccaso library

#### New class

Returns a string describing 'time' as a time relative to 'now'.
Time spans in the past are formatted like "42 minutes ago". Time spans in the future are formatted like "In 42 minutes".
```java
DateUtils.getRelativeTimeSpanString (long time, long now, long minResolution, int flags);
```

#### SwipeRefreshLayout Widget

The following example demonstrates how to add the SwipeRefreshLayout widget to an existing layout file containing a ListView:
[developer.android link](https://developer.android.com/training/swipe/add-swipe-interface.html)

```xml
<android.support.v4.widget.SwipeRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/swiperefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@android:id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</android.support.v4.widget.SwipeRefreshLayout>
```

#### URI builder

Let's say that I want to create the following URL:

`https://www.myawesomesite.com/turtles/types?type=1&sort=relevance#section-name`

To build this with the `Uri.Builder`, following should be done.
Refere this [stack overflow](https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables/19168199#19168199) post.
```java
Uri.Builder builder = new Uri.Builder();
builder.scheme("https")
    .authority("www.myawesomesite.com")
    .appendPath("turtles")
    .appendPath("types")
    .appendQueryParameter("type", "1")
    .appendQueryParameter("sort", "relevance")
    .fragment("section-name");
String myUrl = builder.build().toString();
```

#### API

Well-maintained API which returns information in a JSON format.<br />
[Guardian - API](http://open-platform.theguardian.com/documentation/)

```
https://content.guardianapis.com/search?q=debates&api-key=test
https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test
```

#### Picasso library

- Handling ImageView recycling and download cancelation in an adapter.
- Complex image transformations with minimal memory use.
- Automatic memory and disk caching.

For more information click [here](http://square.github.io/picasso/).

```gradle
compile 'com.squareup.picasso:picasso:2.5.2'
```
Adapter Download

```java
@Override public void getView(int position, View convertView, ViewGroup parent) {
  SquaredImageView view = (SquaredImageView) convertView;
  if (view == null) {
    view = new SquaredImageView(context);
  }
  String url = getItem(position);

  Picasso.with(context).load(url).into(view);
}
```

## UX Design

<img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/top-headlines.png" width="275" height="475"> <img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/nav-drawer-main.png" width="275" height="475"> <img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/nav-drawer-fashion.png" width="275" height="475"> <img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/fashion-top-headlines.png" width="275" height="475"> <img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/fashion-search.png" width="275" height="475"> <img src="https://github.com/nishantkp/UNDNews/blob/master/ux-design/user-preference.png" width="275" height="475">


## PROJECT LICENSE

```
This project was submitted by Nishant Patel as part of the Nanodegree At Udacity.

As part of Udacity Honor code, your submissions must be your own work, hence
submitting this project as yours will cause you to break the Udacity Honor Code
and the suspension of your account.

Me, the author of the project, allow you to check the code as a reference, but if
you submit it, it's your own responsibility if you get expelled.

MIT License

Besides the above notice, the following license applies and this license notice
must be included in all works derived from this project.

Copyright (c) 2017 Nishant Patel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
