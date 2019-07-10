package com.example.sqliteassignment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class DisplayProducts extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public SQLiteDatabase db;
    public DBHelper dbHelper;
    public HashMap<Integer, Integer> idToAvailMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);


        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();


        LinearLayout linear = findViewById(R.id.NestedLinearDisplay);


        Cursor cursor = db.rawQuery("SELECT * FROM productsTable ORDER BY LOWER(name)", null);
        if (cursor != null) {
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

                idToAvailMap.put(cursor.getInt(0), cursor.getInt(5));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "onCreate: " + idToAvailMap.toString());
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

        if (available == 1) checkBox.setChecked(true);
        final int ID = id;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
//                    Toast.makeText(DisplayProducts.this, "Checked", Toast.LENGTH_LONG).show();
                    idToAvailMap.put(ID, 1);
                } else {
//                    Toast.makeText(DisplayProducts.this, "Un-Checked", Toast.LENGTH_LONG).show();
                    idToAvailMap.put(ID, 0);
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


        mType.setGravity(Gravity.LEFT | Gravity.CENTER);


        mType.setText(text);


        return mType;
    }

    private TextView createMiddleInnerTextView(String text) {


        TextView mType = new TextView(this);

        mType.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));


//        mType.setTextSize(17);
//        mType.setPadding(5, 3, 0, 3);
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

    public void addToKitchen(View view) {
        Log.d(TAG, "addToKitchen: " + idToAvailMap.toString());
//        Iterator idToAvailMapItr = idToAvailMap.entrySet().iterator();
        for (Integer id : idToAvailMap.keySet()) {
            db.execSQL("UPDATE productsTable" +
                    " SET available = " + idToAvailMap.get(id) +
                    " WHERE _id = " + id
            );
        }
        Toast.makeText(this, "All items checked above have been added to the kitchen", Toast.LENGTH_LONG).show();
    }
}
