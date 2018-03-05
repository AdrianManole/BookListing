package com.adrianmanole.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An {@link BookAdapter} creates a list of {@link Book} objects
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();

    /**
     * Creates a new {@link BookAdapter} object
     *
     * @param context
     * @param books
     */

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        Context mContext = context;
    }

    public static class BookViewHolder {

        TextView textViewTitle;
        TextView textViewAuthor;
        ImageView imageViewBook;

        public BookViewHolder(View itemView) {
            textViewTitle = (TextView) itemView.findViewById(R.id.book_title_text_view);
            textViewAuthor = (TextView) itemView.findViewById(R.id.author_text_view);
            imageViewBook = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    /**
     * Returns a list item that displays details about the book at a given position
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String image = "";
        BookViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
            holder = new BookViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BookViewHolder) convertView.getTag();
        }

        // Find a book at a given position
        Book currentBook = getItem(position);


        /** Set data to items from ListView */

        // Set title
        holder.textViewTitle.setText(currentBook.getTitle());

        // Set author
        holder.textViewAuthor.setText(currentBook.getAuthor());

        // Set thumbnail image if available
        image = currentBook.getThumbnail();
        if (image != null && image.length() > 0) {
            Picasso.with(getContext()).load(currentBook.getThumbnail()).into(holder.imageViewBook);
        } else {
            Picasso.with(getContext()).load(R.drawable.no_image_found).into(holder.imageViewBook);
        }

        return convertView;

    }

}