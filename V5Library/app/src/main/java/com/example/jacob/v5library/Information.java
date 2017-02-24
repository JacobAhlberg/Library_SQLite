package com.example.jacob.v5library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Information extends AppCompatActivity {
    Button removeBtn, updateBtn;
    EditText newTitle, newAuthor;
    TextView titleTxt, authorTxt;
    long bookId;
    Database dataSource = MainActivity.dataSource;
    Books thisBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information2);

        removeBtn = (Button)findViewById(R.id.removeBtn);
        updateBtn = (Button)findViewById(R.id.updateBtn);
        newTitle = (EditText)findViewById(R.id.newTitleTxt);
        newAuthor = (EditText)findViewById(R.id.newAuthorTxt);
        titleTxt = (TextView)findViewById(R.id.titleTxt);
        authorTxt = (TextView)findViewById(R.id.authorTxt);
        bookId = getIntent().getExtras().getLong("bookId");
        dataSource.open();

        //Get the book object that the user clicked on.
        thisBooks = dataSource.getBook(bookId);

        //Gets the title and author from the book
        titleTxt.setText(thisBooks.getTitle());
        authorTxt.setText(thisBooks.getAuthor());


        //Remove book from database and gives a toast-message
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteBook(thisBooks);
                Toast.makeText(Information.this, "Book deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /*Grabs the new context of the editTexts and puts it in two different Strings and checks
        * that at least one of the Strings contains at least a single sign. Then updates the book.
        * */
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBookTitle = newTitle.getText().toString();
                String newAuthorName = newAuthor.getText().toString();

                if (newBookTitle.isEmpty() && newAuthorName.isEmpty())
                    Toast.makeText(Information.this, "Either title or author text is empty", Toast.LENGTH_LONG).show();
                else {
                    dataSource.updateBook(thisBooks.getBook_id(), newBookTitle, newAuthorName);
                    finish();
                }
            }
        });
    }

    //Starts the database on start
    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }

    //Closes the database on pause
    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
