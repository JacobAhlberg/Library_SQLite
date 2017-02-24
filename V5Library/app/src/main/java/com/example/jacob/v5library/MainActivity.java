package com.example.jacob.v5library;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ListActivity {
    Boolean firstStart = true;
    Button addBtn;
    EditText titleTxt, authorTxt;
    String title = null, author = null;
    protected static Database dataSource;
    private List<Books> values;
    protected static ArrayAdapter<Books> arrayAdapter = null;

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

        //Initiating buttons and edittexts
        addBtn = (Button) findViewById(R.id.addBtn);
        titleTxt = (EditText) findViewById(R.id.nameTxt);
        authorTxt = (EditText) findViewById(R.id.authorTxt);

        //Creating the database
        dataSource = new Database(this);
        //Opening the database
        dataSource.open();
        //The list grabs all the current books i the database
        values = dataSource.getAllBooks();
        arrayAdapter = new ArrayAdapter<Books>(this, android.R.layout.simple_list_item_1, values);

        /*Using a boolean with the value of true to create six default
        * books the very first time the database creates.
        * A sharedpreferences are used to store the boolean value to false after
        * the database has been created.
        * */
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (firstStart){
            createBooks("Tapeter och solsken", "Janne Andersson");
            createBooks("Böcker jag läst", "Jag");
            createBooks("Statistik över något", "Nisse");
            createBooks("Snö eller regn?", "Av författare");
            createBooks("Halt eller springa?", "Nils Dacke");
            createBooks("Gurkor eller tomater", "Astrid Lindgren");

            editor.putBoolean("firstStart", false).apply();
        }
        setListAdapter(arrayAdapter);

        /*A button listener where you can add a new book.
        * Uses if-statement to check if either title or  author String contains at least
        * a single sign. If not the user will receive a toast-message.
        * */
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
                    Toast.makeText(MainActivity.this, "Please type in a title and an author.", Toast.LENGTH_SHORT).show();

                //Updates the listView
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }

    /*A method to create a new book to the database and also updates
    * the listView with the arrayAdapter
    */
    void createBooks(String title, String author){
        Books newBook = dataSource.createBook(title, author);
        arrayAdapter.add(newBook);
        arrayAdapter.notifyDataSetChanged();
    }


    /*When resuming from the different activity, the mainActivity should
    * grab all the books from the list, if the user has been either updating or removing a book.
    * */
    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        values = dataSource.getAllBooks();
        arrayAdapter = new ArrayAdapter<Books>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    //Closes the database when the user enters a nwe activity or closing the application.
    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
