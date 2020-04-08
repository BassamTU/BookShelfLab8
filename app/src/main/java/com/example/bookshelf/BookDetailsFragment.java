package com.example.bookshelf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
public class BookDetailsFragment extends Fragment {

    private static final String BOOK_TITLE_KEY = "bookKey";

    private Book book;
    TextView titleTextView, authorTextView, yearTextView;
    ImageView coverImageView;

    public BookDetailsFragment() {}

    public static BookDetailsFragment newInstance(Book bk) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_TITLE_KEY, bk);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(BOOK_TITLE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        titleTextView = v.findViewById(R.id.textView1);
        authorTextView = v.findViewById(R.id.textView2);
        yearTextView = v.findViewById(R.id.textView3);
        coverImageView = v.findViewById(R.id.imageView1);
        if (book != null)
            displayBook(book);

        return v;
    }

    public void displayBook(Book book) {
        titleTextView.setText(book.name);
        authorTextView.setText(book.author);
        yearTextView.setText(book.published+"");
        Picasso.get().load(book.coverURL).into(coverImageView);
    }
}
