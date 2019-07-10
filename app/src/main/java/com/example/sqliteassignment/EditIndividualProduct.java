package com.example.sqliteassignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditIndividualProduct extends AppCompatActivity {

    public DBHelper dbHelper;
    public SQLiteDatabase db;
    String TAG = this.getClass().getSimpleName();
    private String name;
    private Double weight;
    private Double price;
    private String desc;
    private int available;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_individual_product);
        Log.d(TAG, "In Edit Individual Product");
        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            desc = getIntent().getStringExtra("desc");
            weight = getIntent().getDoubleExtra("weight", 0);
            price = getIntent().getDoubleExtra("price", 0);
            available = getIntent().getIntExtra("available", 0);
            id = getIntent().getIntExtra("id", -1);

            EditText nameEdtTxt = findViewById(R.id.edtTxtNameOfProduct_EIP);
            EditText weightEdtTxt = findViewById(R.id.edtTxtWeight_EIP);
            EditText priceEdtTxt = findViewById(R.id.edtTxtPrice_EIP);
            EditText descEdtTxt = findViewById(R.id.edtTxtDescription_EIP);

            nameEdtTxt.setText(name);
            weightEdtTxt.setText(weight.toString());
            priceEdtTxt.setText(price.toString());
            descEdtTxt.setText(desc);


            Log.d(TAG, "ID to edit: " + id);

        }

    }

    public void OnSave(View view) {
        EditText nameEdtTxt = findViewById(R.id.edtTxtNameOfProduct_EIP);
        EditText weightEdtTxt = findViewById(R.id.edtTxtWeight_EIP);
        EditText priceEdtTxt = findViewById(R.id.edtTxtPrice_EIP);
        EditText descEdtTxt = findViewById(R.id.edtTxtDescription_EIP);

        String _name = String.valueOf(nameEdtTxt.getText()).trim();
        Double _weight = Double.parseDouble(weightEdtTxt.getText().toString());
        Double _price = Double.parseDouble(priceEdtTxt.getText().toString());
        String _desc = String.valueOf(descEdtTxt.getText()).trim();

        Log.d(TAG, "name: " + _name);
        Log.d(TAG, "weight: " + _weight);
        Log.d(TAG, "price: " + _price);
        Log.d(TAG, "desc: " + _desc);


        dbHelper = new DBHelper(this);

        db = dbHelper.getReadableDatabase();

        db.execSQL("UPDATE productsTable" +
                " SET name = \"" + _name + "\"" +
                " WHERE _id = " + id
        );


        db.execSQL("UPDATE productsTable" +
                " SET description = \"" + _desc + "\"" +
                " WHERE _id = " + id
        );


        db.execSQL("UPDATE productsTable" +
                " SET price = " + _price +
                " WHERE _id = " + id
        );


        db.execSQL("UPDATE productsTable" +
                " SET weight = " + _weight +
                " WHERE _id = " + id
        );


        Toast.makeText(this, name + " was edited and saved to the Database", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, EditProducts.class);
        startActivity(intent);

    }
}
