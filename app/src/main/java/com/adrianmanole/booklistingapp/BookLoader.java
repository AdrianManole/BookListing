package com.adrianmanole.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Populates the list of books using an AsyncTask
 * to perform network request to Google Books API
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    public static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;

    /**
     * Constructs a new {@link BookLoader} object
     *
     * @param context
     * @param url
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() called ...");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() called...");

        if (mUrl == null) {
            return null;
        }

        // Performs network request, parse the response and extracts
        // a list of books matching the search criteria
        List<Book> books = QueryUtils.fetchBookData(mUrl, getContext());
        return books;

    }
}