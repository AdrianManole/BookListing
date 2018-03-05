package com.adrianmanole.booklistingapp;

import android.content.Context;
import android.text.TextUtils;
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
 * Helper methods related to requesting and receiving book data from Google Books API
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private static Context mContext;

    // Google Books API keys
    private static final String KEY_ITEMS = "items";
    private static final String KEY_VOLUMEINFO = "volumeInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_PREVIEWLINK = "previewLink";
    private static final String KEY_IMAGELINKS = "imageLinks";
    private static final String KEY_THUMBNAIL = "smallThumbnail";

    private QueryUtils() {
    }

    /**
     * Query the URL and return a list of {@link Book}`s objects.
     */

    public static List<Book> fetchBookData(String requestUrl, Context context) {

        mContext = context;

        // Create the URL object
        URL url = createUrl(requestUrl);

        // Perform the HTTP request to the URL and receive a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_http_request), e);
        }

        // Extract the specified fields from JSON response and create a list of {@link Book}`s objects
        List<Book> books = extractFeatureFromJson(jsonResponse);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return a list of {@link Book}`s objects
        return books;
    }

    /**
     * This method returns a new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_invalid_url), e);
        }
        return url;
    }

    /**
     * Make HTTP request to the given URL and return a String as response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, mContext.getString(R.string.exception_response_code)
                        + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_json_response), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the JSON response
     * from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book}`s objects from parsing the JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {

        /** If JSON string is empty or null, return early */
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        /** Create an empty ArrayList for adding books */
        List<Book> books = new ArrayList<>();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray booksArray;        // Array of books returned in the JSON object
            JSONObject currentBook;      // Single book at a specific position in the bookArray
            JSONObject volumeInfo;       // VolumeInfo object of the currentBook
            String title;                // Title of the currentBook
            JSONArray authorsArray;      // Author Array of the currentBook
            String authorList = "";      // Authors of the currentBook - obtained from authorsArray
            String previewLink = "";     // Preview Link of the currentBook
            JSONObject imageLinks;       // JSON Object with various image links
            String thumbnailLink = "";   // Thumbnail Link of the currentBook - obtained from imageLinks


            if (baseJsonResponse.has(KEY_ITEMS)) {
                booksArray = baseJsonResponse.getJSONArray(KEY_ITEMS);

                for (int i = 0; i < booksArray.length(); i++) {

                    currentBook = booksArray.getJSONObject(i);
                    volumeInfo = currentBook.getJSONObject(KEY_VOLUMEINFO);
                    title = volumeInfo.getString(KEY_TITLE);

                    // Get value for author if the key exists
                    if (volumeInfo.has(KEY_AUTHORS)) {
                        authorsArray = volumeInfo.getJSONArray(KEY_AUTHORS);

                        if (authorsArray.length() > 1) {
                            authorList = authorsArray.join(", ").replaceAll("\"", "");
                        } else if (authorsArray.length() == 1) {
                            authorList = authorsArray.getString(0);
                        } else if (authorsArray.length() == 0) {
                            authorList = "";
                        }
                    }

                    // Get value for preview link if the key exists
                    if (volumeInfo.has(KEY_PREVIEWLINK)) {
                        previewLink = volumeInfo.getString(KEY_PREVIEWLINK);
                    } else {
                        previewLink = "";
                    }

                    // Get value for smallThumbnail if the key exists
                    if (volumeInfo.has(KEY_IMAGELINKS)) {
                        imageLinks = volumeInfo.getJSONObject(KEY_IMAGELINKS);
                        if (imageLinks.has(KEY_THUMBNAIL)) {
                            thumbnailLink = imageLinks.getString(KEY_THUMBNAIL);
                        } else {
                            thumbnailLink = "";
                        }
                    }

                    // Create a new {@link Book} object with parameters obtained from JSON response
                    Book book = new Book(
                            title,
                            authorList,
                            thumbnailLink,
                            previewLink
                    );

                    // Add the new {@link Book} object to the list of books
                    books.add(book);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_json_response), e);
        }

        // Return the list of books
        return books;

    }
}