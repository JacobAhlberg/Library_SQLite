package com.example.jacob.v5library;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends ListActivity {
    Boolean firstStart;
    Button addBtn;
    EditText titleTxt, authorTxt;
    String title = null, author = null;
    protected static Database dataSource;
    private List<Books> values;
    protected static ArrayAdapter<Books> arrayAdapter = null;
    private SharedPreferences pref = null;
    ListView listView;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(MainActivity.this, Information.class);
        Books thisBook = values.get(position);
        long bookId = thisBook.getBook_id();
        intent.putExtra("bookId", bookId);
        l.getItemAtPosition(position);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = (Button) findViewById(R.id.addBtn);
        titleTxt = (EditText) findViewById(R.id.nameTxt);
        authorTxt = (EditText) findViewById(R.id.authorTxt);


        dataSource = new Database(this);
        dataSource.open();
        values = dataSource.getAllBooks();
        arrayAdapter = new ArrayAdapter<Books>(this, android.R.layout.simple_list_item_1, values);

        firstStart = true;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        firstStart = pref.getBoolean("firstTime", false);
        if (firstStart){
            createBooks("Tapeter och solsken", "Janne Andersson");
            createBooks("Böcker jag läst", "Jag");
            createBooks("Statistik över något", "Nisse");
            createBooks("Snö eller regn?", "Av författare");
            createBooks("Halt eller springa?", "Nils Dacke");
            createBooks("Gurkor eller tomater", "Astrid Lindgren");

            pref.edit().putBoolean("firstTime", false).commit();
        }

        setListAdapter(arrayAdapter);

        //Add a book and an author
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleTxt.getText().toString();
                author = authorTxt.getText().toString();

                if (!title.equals("") && !author.equals("")) {
                    Books book = dataSource.createBook(title, author);
                    arrayAdapter.add(book);
                    arrayAdapter.notifyDataSetChanged();
                    title = null;
                    author = null;
                    titleTxt.setText("");
                    authorTxt.setText("");
                }
                else
                    Toast.makeText(MainActivity.this, "ESCANDALOOOOOOO", Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }

    void createBooks(String title, String author){
        Books newBook = dataSource.createBook(title, author);
        arrayAdapter.add(newBook);
        arrayAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        values = dataSource.getAllBooks();
        arrayAdapter = new ArrayAdapter<Books>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
