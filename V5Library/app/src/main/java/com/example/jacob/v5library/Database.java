package com.example.jacob.v5library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 2017-02-14.
 */

class Database extends SQLiteOpenHelper {
    final static String DB_NAME = "Database";
    final static String TABLE_NAME = "BooksLibrary";
    final static String COLUMN_BOOK = "title";
    final static String COLUMN_AUTHOR = "author";
    final static String _ID = "id";
    SQLiteDatabase database;

    Database(Context context) {
        super(context, "\"" + DB_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE = "CREATE table " + TABLE_NAME + "(id integer primary key autoincrement, title varchar not null," +
                "author varchar not null);";



        //We need to add the books here.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        db.execSQL(DATABASE_CREATE);
    }
    public void open() throws SQLException{
        database = getWritableDatabase();
    }

    //Creates book
    Books createBook(String bookTitle, String author){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COLUMN_BOOK, bookTitle);
        contentValues.put(Database.COLUMN_AUTHOR, author);
        long insertId = database.insert(Database.TABLE_NAME, null, contentValues);

        Books book = new Books();
        book.setBook_id(insertId);
        book.setTitle(bookTitle);
        book.setAuthor(author);
        return book;
    }


    List<Books> getAllBooks(){
        List<Books> books = new ArrayList<Books>();
        String[] allColumns = {_ID, COLUMN_BOOK};

        Cursor cursor = database.query(Database.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Books bookShow = cursorToBooks(cursor);
            books.add(bookShow);
            cursor.moveToNext();
        }
        cursor.close();
        return books;
    }

    Books getBook(long id){
        Books thisBook = new Books();
        String[] allColumns = {_ID, COLUMN_BOOK, COLUMN_AUTHOR};
        String whereClause = _ID + " = ?";
        String[] whereArgs = {"" + id};

        Cursor cursor = database.query(TABLE_NAME, allColumns, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        thisBook.setBook_id(cursor.getLong(0));
        thisBook.setTitle(cursor.getString(1));
        thisBook.setAuthor(cursor.getString(2));
        cursor.close();
        return thisBook;
    }


    private Books cursorToBooks(Cursor cursor) {
        Books books = new Books();
        books.setBook_id(cursor.getLong(0));
        books.setTitle(cursor.getString(1));
        return books;
    }

    void deleteBook(Books book){
        long id = book.getBook_id();
        database.delete(Database.TABLE_NAME, Database._ID + " = " + id, null);

    }

    void updateBook(Long id, String newTitle, String newAuthor){
        ContentValues contentValues = new ContentValues();
        String select = _ID + " = " + id;
        if (newTitle.isEmpty()){
            contentValues.put(Database.COLUMN_AUTHOR, newAuthor);
            database.update(TABLE_NAME, contentValues, select, null);
        }
        else if (newAuthor.isEmpty()){
            contentValues.put(Database.COLUMN_BOOK, newTitle);
            database.update(TABLE_NAME, contentValues, select, null);
        }
        else{
            contentValues.put(Database.COLUMN_AUTHOR, newAuthor);
            contentValues.put(Database.COLUMN_BOOK, newTitle);
            database.update(TABLE_NAME, contentValues, select, null);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
