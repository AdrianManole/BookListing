package com.adrianmanole.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Context of the app
     */
    final Context mContext = this;

    private String mBookTitle;
    private String mAuthorName;

    private EditText mEditBookTitle;
    private EditText mEditAuthorName;
    private Button mBookSearchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** Initialize UI components */

        mEditBookTitle = (EditText) findViewById(R.id.edit_book_title);
        mEditAuthorName = (EditText) findViewById(R.id.edit_author_name);
        mBookSearchButton = (Button) findViewById(R.id.search_books_button);

        /** Set onClickListener on search books button view */
        mBookSearchButton.setOnClickListener(this);

        /** Add TextChangeListener to EditText fields */
        mEditBookTitle.addTextChangedListener(new BookTextWatcher(mEditBookTitle));
        mEditAuthorName.addTextChangedListener(new BookTextWatcher(mEditAuthorName));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_books_button:
                showSearchResults();
                break;
        }
    }


    /**
     * This method start BookListActivity after the book title and author are introduced
     */

    public void showSearchResults() {
        if (validateInput()) {
            Intent intent = new Intent(mContext, BookListActivity.class);

            // Replace spaces with + sign for URL to be used to fetch data
            intent.putExtra("bookTitle", mBookTitle.replaceAll(" ", "+"));
            intent.putExtra("bookAuthor", mAuthorName.replaceAll(" ", "+"));
            startActivity(intent);
        }
    }

    /**
     * This method checks if the input contains invalid characters for a string.
     */

    public boolean validateInput() {
        mBookTitle = mEditBookTitle.getText().toString().trim();
        mAuthorName = mEditAuthorName.getText().toString().trim();

        /** Check if the book title is entered */

        if (TextUtils.isEmpty(mBookTitle)) {
            mEditBookTitle.setBackgroundResource(R.color.error);
            mEditBookTitle.setError("Please enter the title again");
            return false;
        }

        /** Check if the author name entered is valid */

        if (TextUtils.isEmpty(mBookTitle)) {
            mEditAuthorName.setBackgroundResource(R.color.error);
            mEditAuthorName.setError("Please enter the author name again");
            return false;
        }

        return true;

    }

    /**
     * Extends TextWatcher class for EditText fields
     */
    private class BookTextWatcher implements TextWatcher {
        private View view;

        private BookTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mEditBookTitle.setBackgroundResource(R.color.background_color);
            mEditAuthorName.setBackgroundResource(R.color.background_color);
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}