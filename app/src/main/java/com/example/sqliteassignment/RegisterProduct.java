package com.example.sqliteassignment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterProduct extends AppCompatActivity {
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);
    }

    public void OnSave(View view) {
        EditText nameEdtTxt = findViewById(R.id.edtTxtNameOfProduct_RP);
        EditText weightEdtTxt = findViewById(R.id.edtTxtWeight_RP);
        EditText priceEdtTxt = findViewById(R.id.edtTxtPrice_RP);
        EditText descEdtTxt = findViewById(R.id.edtTxtDescription_RP);

        String name = String.valueOf(nameEdtTxt.getText()).trim();
        Double weight = Double.parseDouble(weightEdtTxt.getText().toString());
        Double price = Double.parseDouble(priceEdtTxt.getText().toString());
        String desc = String.valueOf(descEdtTxt.getText()).trim();
        Log.d(TAG, "name: " + name);
        Log.d(TAG, "weight: " + weight);
        Log.d(TAG, "price: " + price);
        Log.d(TAG, "desc: " + desc);

        dbHelper = new DBHelper(this);
        Product prod = new Product(name, desc, weight, price, 0);
        dbHelper.addProduct(prod);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM productsTable", null);

        if (cursor != null) {
            cursor.moveToFirst();
            int row = 1;
            do {
                Log.d(TAG, String.valueOf(row++) + ":" + cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Toast.makeText(this, name + " was saved to the Database", Toast.LENGTH_LONG).show();
    }

}
