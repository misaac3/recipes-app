package com.example.sqliteassignment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipes extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final String API_KEY = "92f869348956dc10872d17f99150fcb9";
    public SQLiteDatabase db;
    public DBHelper dbHelper;
    public String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        if (getIntent() != null) {
            query = getIntent().getStringExtra("query");
            Log.d(TAG, "onCreate: " + query);
            new Retrievedata().execute(query);
        }
    }


//    public void onSearchRecipeClick(View view) {
//        EditText searchRecipesEditText = findViewById(R.id.searchRecipesEditText);
//        String searchParams = String.valueOf(searchRecipesEditText.getText()).toLowerCase().trim();
//        String[] split = searchParams.split(" ");
//        new Retrievedata().execute(split);
//    }


    public class Retrievedata extends AsyncTask<String, Void, String> {
        String TAG = this.getClass().getSimpleName();
        HttpURLConnection httpConn;
        URL url;
        ArrayList<String> titlesList = new ArrayList<>();
        ArrayList<String> URLList = new ArrayList<>();

        @Override
        protected void onPostExecute(String s) {
            createTitleDisplay(titlesList, URLList);
        }

        @Override
        protected String doInBackground(String... params) {
            String apiCallURL = "https://www.food2fork.com/api/search?key=92f869348956dc10872d17f99150fcb9&q=";
            Log.d(TAG, "params.length " + params.length);
            try {
//                boolean isFirstTerm = true;
//                for (String param : params) {
//                    if (isFirstTerm) {
//                        apiCallURL += param;
//                        isFirstTerm = false;
//                    } else {
//                        apiCallURL += "," + param;
//                    }
//                }
                 apiCallURL = "https://www.food2fork.com/api/search?key=d4be9fd01ca564cb475ed525ed153b50&q=" + params[0];

                Log.d(TAG, "URL: " + apiCallURL);
                url = new URL(apiCallURL);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpConn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                Log.d(TAG, "onCreate: " + response);

                try {
                    JSONObject reader = new JSONObject(String.valueOf(response));
                    JSONArray recipes = reader.getJSONArray("recipes");
                    for (int i = 0; i < recipes.length(); i++) {
                        JSONObject recipe = recipes.getJSONObject(i);
                        titlesList.add(recipe.getString("title"));
                        URLList.add(recipe.getString("source_url"));
                        Log.d(TAG, "Title: " + recipe.getString("title"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpConn.disconnect();
            }
            String str = "";
            for (String x : params) {
                str += x + ", ";
            }
            Log.d(TAG, "doInBackground: " + apiCallURL);
            Log.d(TAG, "doInBackground: " + str);
            return null;
        }

        private void createTitleDisplay(ArrayList<String> titlesList, ArrayList<String> URLList) {
            LinearLayout parentLayout = findViewById(R.id.NestedLinearDisplay_R);
            for (int i = 0; i < titlesList.size(); i++) {
                String title = titlesList.get(i);
                String URL = URLList.get(i);
                parentLayout.addView(createTitleTextView(title, URL));
            }
        }


        public TextView createTitleTextView(String title, final String URL) {
            TextView tv = new TextView(Recipes.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            tv.setPadding(20, 20, 20, 20);
            tv.setLayoutParams(params);
            tv.setBackgroundResource(R.drawable.border);
            tv.setText(title);
            tv.setTextSize(20);
            tv.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                    startActivity(browserIntent);
                }
            }));
            return tv;
        }
    }
}