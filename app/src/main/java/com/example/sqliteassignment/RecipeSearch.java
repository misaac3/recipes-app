package com.example.sqliteassignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeSearch extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    public SQLiteDatabase db;
    public DBHelper dbHelper;
    public HashMap<Integer, Integer> idToInSearchMap = new HashMap<>();
    public HashMap<Integer, String> idToTitleMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);


        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();


        LinearLayout linear = findViewById(R.id.NestedLinearDisplay_RS);
        Cursor cursor = db.rawQuery("SELECT * FROM productsTable WHERE available = 1 ORDER BY LOWER(name)", null);
        if (cursor.getCount() > 0) {
            if (cursor != null) {
                Log.d(TAG, "onCreate: DB query is NOT EMPTY");
                cursor.moveToFirst();
                do {
                    linear.addView(
                            createProductDisplay(
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getDouble(3),

                                    cursor.getDouble(4),
                                    cursor.getInt(5),
                                    cursor.getInt(0)));

                    idToInSearchMap.put(cursor.getInt(0), 0
                    );
                    idToTitleMap.put(cursor.getInt(0), cursor.getString(1));
                } while (cursor.moveToNext());
            }
            Log.d(TAG, "onCreate: " + idToInSearchMap.toString());
        }
    }


    private LinearLayout createProductDisplay(String name, String desc, double weight,
                                              double price, int available, int id) {


        LinearLayout parentLayout = new LinearLayout(this);
        LinearLayout.LayoutParams parentLinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        parentLayout.setBackgroundResource(R.drawable.border);
        parentLinearParams.setMargins(0, 50, 0, 0);
        parentLayout.setLayoutParams(parentLinearParams);


        final CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        checkBoxParams.setMargins(30, 0, 30, 0);
        checkBox.setLayoutParams(checkBoxParams);
        checkBox.setGravity(Gravity.CENTER);

        final int ID = id;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {

//                    Toast.makeText(Availability.this, "Checked", Toast.LENGTH_LONG).show();
                    idToInSearchMap.put(ID, 1);
                } else {
//                    Toast.makeText(Availability.this, "Un-Checked", Toast.LENGTH_LONG).show();
                    idToInSearchMap.put(ID, 0);
                }
            }
        });

        parentLayout.addView(checkBox);

        LinearLayout childLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.setMargins(0, 10, 0, 10);


        childLayout.setOrientation(LinearLayout.VERTICAL);

        childLayout.setLayoutParams(linearParams);

        LinearLayout topChildLayout = new LinearLayout(this);
        LinearLayout.LayoutParams topLinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                5f);
        topChildLayout.setLayoutParams(topLinearParams);

        LinearLayout middleChildLayout = new LinearLayout(this);
        LinearLayout.LayoutParams middleLinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                5f);
        middleLinearParams.setMargins(0, 5, 0, 0);


        LinearLayout lowerChildLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lowerLinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lowerLinearParams.setMargins(0, 5, 0, 0);
        lowerChildLayout.setLayoutParams(lowerLinearParams);


        topChildLayout.addView(createTopInnerTextView(name), 0);


        middleChildLayout.addView(createMiddleInnerTextView("Weight: " + String.valueOf((double) Math.round(weight * 100d) / 100d) + "g"), 0);

        middleChildLayout.addView(createMiddleInnerTextView("Price " + "£" + String.valueOf((double) Math.round(price * 100d) / 100d)), 1);

        String avail = available == 0 ? "Not Available" : "Available";

        middleChildLayout.addView(createMiddleInnerTextView(avail), 2);


        lowerChildLayout.addView(createBottomInnerTextView(desc), 0);

        childLayout.addView(topChildLayout);
        childLayout.addView(middleChildLayout);
        childLayout.addView(lowerChildLayout);

        parentLayout.addView(childLayout);
        return parentLayout;
    }

    private TextView createBottomInnerTextView(String text) {
        TextView mType = new TextView(this);

        mType.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

        mType.setGravity(Gravity.CENTER);
        mType.setText(text);
        return mType;
    }

    private TextView createMiddleInnerTextView(String text) {
        TextView mType = new TextView(this);
        mType.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        mType.setGravity(Gravity.CENTER);
        mType.setText(text);
        return mType;
    }

    private TextView createTopInnerTextView(String text) {

        TextView mType = createMiddleInnerTextView(text);
        mType.setTypeface(Typeface.DEFAULT_BOLD);
        mType.setTextSize(17);
        mType.setPadding(0, 10, 0, 10);


        return mType;
    }


    public void onSearchRecipeClick(View view) {
        String query = "";
        boolean queryExists = false;
        for (Integer id : idToInSearchMap.keySet()) {
            queryExists = true;
            if (idToInSearchMap.get(id) == 1) {
                String temp = idToTitleMap.get(id) + ",";
                temp = temp.replace(" ", ",");
                query += temp;
                Log.d(TAG, "onSearchRecipeClick: " + query);
            }
        }
        if (queryExists && query.trim().length() > 0) {
            query = query.substring(0, query.length() - 1);
            Log.d(TAG, "onSearchRecipeClick: " + query);
            Intent intent = new Intent(this, Recipes.class);
            intent.putExtra("query", query);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Select Items and Click Find Recipes to see recipes using these products", Toast.LENGTH_LONG).show();
        }

    }
}
