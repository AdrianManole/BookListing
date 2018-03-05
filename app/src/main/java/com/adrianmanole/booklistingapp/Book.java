package com.adrianmanole.booklistingapp;

/**
 * An {@link Book} object contains details of a book fetched from Google Books API
 */

public class Book {

    /**
     * Book title
     */

    private String mTitle;

    /**
     * Book author name
     */

    private String mAuthor;

    /**
     * Image Thumbnail link
     */

    private String mThumbnail;

    /**
     * Book preview link
     */

    private String mPreviewLink;

    /**
     * Constructor for a new {@link Book} object
     *
     * @param title       - Title of the book
     * @param author      - Author name of the book
     * @param Thumbnail   - Link for the image thumbnail of the book cover
     * @param previewLink - Link for preview of the book
     */

    public Book(String title, String author, String Thumbnail, String previewLink) {
        mTitle = title;
        mAuthor = author;
        mThumbnail = Thumbnail;
        mPreviewLink = previewLink;
    }

    /**
     * Returns title of the book
     */
    public String getTitle() {
        return mTitle;
    }


    /**
     * Returns author name of the book
     */
    public String getAuthor() {
        return mAuthor;
    }


    /**
     * Returns thumbnail of the book cover
     */
    public String getThumbnail() {
        return mThumbnail;
    }


    /**
     * Returns preview of the book
     */
    public String getPreviewLink() {
        return mPreviewLink;
    }

}