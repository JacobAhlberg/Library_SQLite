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

        thisBooks = dataSource.getBook(bookId);

        titleTxt.setText(thisBooks.getTitle());
        authorTxt.setText(thisBooks.getAuthor());


        //Remove book from database
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteBook(thisBooks);
                Toast.makeText(Information.this, "Book deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Update book from dataBase
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBookTitle = newTitle.getText().toString();
                String newAuthorName = newAuthor.getText().toString();

                if (newBookTitle.isEmpty() && newAuthorName.isEmpty())
                    Toast.makeText(Information.this, "Wrooong", Toast.LENGTH_LONG).show();
                else {
                    dataSource.updateBook(thisBooks.getBook_id(), newBookTitle, newAuthorName);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
