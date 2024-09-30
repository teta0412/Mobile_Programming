package com.map.nguyenanhduc.bt1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;
    private String databasePath;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    private boolean checkDatabase() {
        File dbFile = new File(databasePath);
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(databasePath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDatabase() throws IOException {
        createDatabase();
        return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "name TEXT, " +
                "email TEXT, " +
                "telephoneNum TEXT, " +
                "gender TEXT, " +
                "dob TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
