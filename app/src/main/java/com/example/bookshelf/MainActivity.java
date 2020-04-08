package com.example.bookshelf;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookInterface {
    FragmentManager fm;
    boolean twoPane;
    BookDetailsFragment detailsFragment;
    ArrayList<Book> books;
    JSONArray bookjson;
    BookListFragment listFragment;
    EditText searchString;
    Button searchButton;
    String searchText="";

    Handler urlHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                bookjson = new JSONArray((String) msg.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            books.clear();
            for (int i = 0; i < bookjson.length(); i++) {
                try {
                    JSONObject jb = bookjson.getJSONObject(i);
                    //Log.e("HANDLER", jb.getString("title"));
                    books.add(new Book(jb));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            listFragment.getBooks(books);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        searchString = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        twoPane = findViewById(R.id.detailfrag) == null;

        books = new ArrayList<>();
        searchText = "";
        detailsFragment = new BookDetailsFragment();
        listFragment = new BookListFragment();
        


        if (!twoPane) {
            addFragment(listFragment, R.id.listfrag);
            addFragment(detailsFragment, R.id.detailfrag);
        } else {
            addFragment(listFragment, R.id.listfrag);
        }
        getBook("");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = searchString.getText().toString();
                getBook(searchText);
            }
        });


    }
    public void getBook(final String text){
        new Thread() {
            public void run() {
                try {
                    String urlString = "https://kamorris.com/lab/audlib/booksearch.php?search="+text;
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String tmpString;
                    while ((tmpString = reader.readLine()) != null) {
                        builder.append(tmpString);
                    }
                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    urlHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void addFragment(Fragment fragment, int id) {
        getSupportFragmentManager().
                beginTransaction().
                replace(id, fragment).
                addToBackStack(null).
                commit();
    }

    @Override
    public void bookSelected(Book bookObj) {
        detailsFragment.displayBook(bookObj);
    }

}
