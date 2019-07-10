package com.example.sqliteassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Productsdb";
    // Books table name
    private static final String TABLE_NAME = "productsTable";
    // Books Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_PRICE = "price";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_AVAILABLE = "available";

    String tableName = "productsTable";
    private String TAG = this.getClass().getSimpleName();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_TABLE =
                "CREATE TABLE " + tableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "description TEXT, " +
                        "weight DOUBLE, " +
                        "price DOUBLE, " +
                        "available INTEGER )";


        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older  table if existed
        db.execSQL("DROP TABLE IF EXISTS " + tableName);

        // create fresh  table
        this.onCreate(db);
    }

    public void addProduct(Product product) {
        Log.d(TAG, product.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESC, product.getDesc());
        values.put(KEY_WEIGHT, product.getWeight());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_AVAILABLE, product.getAvailability());

        // 3. insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();

    }


}


