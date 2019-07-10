package com.example.sqliteassignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

    }

    public void registerProductClick(View view) {
        Log.d(TAG, "Registering Product...");
        Intent intent = new Intent(this, RegisterProduct.class);
        startActivity(intent);
    }


    public void displayProductsClick(View view) {
        Cursor c = db.rawQuery("SELECT * FROM productsTable ", null);
        Log.d(TAG, "# rows:  " + c.getCount());
        if (c.getCount() > 0) {
            Intent intent = new Intent(this, DisplayProducts.class);
            startActivity(intent);
        } else {
            emptyDBToast();
        }
    }

    private void emptyDBToast() {
        Toast.makeText(this, "Database is empty! Please Register a Product First!", Toast.LENGTH_LONG).show();

    }

    public void availabilityClick(View view) {
        Cursor c = db.rawQuery("SELECT * FROM productsTable WHERE available = 1", null);
        Cursor c2 = db.rawQuery("SELECT * FROM productsTable ", null);

        Log.d(TAG, "# rows:  " + c.getCount());
        if (c.getCount() > 0) {
            Intent intent = new Intent(this, Availability.class);
            startActivity(intent);
        } else if(c2.getCount() < 1) {
            emptyDBToast();
        }
        else{
            Toast.makeText(this, "Database has no products available! " +
                            "You can makes items available by clicking Display Products " +
                            "and checking an item.",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void EditProductsClick(View view) {
        Cursor c = db.rawQuery("SELECT * FROM productsTable ", null);
        Log.d(TAG, "# rows:  " + c.getCount());
        if (c.getCount() > 0) {
            Intent intent = new Intent(this, EditProducts.class);
            startActivity(intent);
        }
        else {
            emptyDBToast();
        }
    }

    public void SearchClick(View view) {
        Cursor c = db.rawQuery("SELECT * FROM productsTable ", null);
        Log.d(TAG, "# rows:  " + c.getCount());
        if (c.getCount() > 0) {
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        }
        else {
            emptyDBToast();
        }
    }

    public void RecipesClick(View view) {
        Intent intent = new Intent(this, RecipeSearch.class);
        startActivity(intent);
    }
}
