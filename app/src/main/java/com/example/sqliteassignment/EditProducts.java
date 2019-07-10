package com.example.sqliteassignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

import java.util.HashMap;

public class EditProducts extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public SQLiteDatabase db;
    public DBHelper dbHelper;
    public HashMap<Integer, Integer> idToAvailMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);


        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();


        LinearLayout linear = findViewById(R.id.NestedLinearDisplay_EP);


        Cursor cursor = db.rawQuery("SELECT * FROM productsTable ORDER BY LOWER(name)", null);
        if (cursor != null) {
            cursor.moveToFirst();
//            linear.addView(createHeader());
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

    private LinearLayout createProductDisplay(final String name, final String desc, final double weight,
                                              final double price, final int available, final int id) {

        LinearLayout parentLayout = new LinearLayout(this);
        LinearLayout.LayoutParams parentLinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        parentLayout.setPadding(25, 25, 25, 25);

        parentLayout.setBackgroundResource(R.drawable.border);

        parentLinearParams.setMargins(0, 50, 0, 0);
        parentLayout.setLayoutParams(parentLinearParams);

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

        middleChildLayout.addView(createMiddleInnerTextView(avail),2);

//        middleChildLayout.addView(createMiddleInnerTextView(String.valueOf(available)), 2);
//        middleChildLayout.addView(createMiddleInnerTextView(String.valueOf(id)), 3);

        lowerChildLayout.addView(createBottomInnerTextView(desc), 0);

        childLayout.addView(topChildLayout);
        childLayout.addView(middleChildLayout);
        childLayout.addView(lowerChildLayout);

        parentLayout.addView(childLayout);
        parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProducts.this, EditIndividualProduct.class);
                intent.putExtra("name", name);
                intent.putExtra("desc", desc);
                intent.putExtra("price", price);
                intent.putExtra("weight", weight);
                intent.putExtra("id", id);
                intent.putExtra("available", available);
                startActivity(intent);

            }
        });
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

    }
}
