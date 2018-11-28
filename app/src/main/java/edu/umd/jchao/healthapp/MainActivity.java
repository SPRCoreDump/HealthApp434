package edu.umd.jchao.healthapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    static Map<String, String> Nutrition;
    static Map<String, String> Exercise;
    static ArrayList<String> todayList = new ArrayList<>(); //list of exercises and foods eaten today
    private ArrayAdapter<String> todayAdapter;
    private ListView lv;
    public static int netCalories = 0; //what will be displayed on top of main page


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    //mTextMessage.setText(R.string.title_cal);
                    startActivity(new Intent(MainActivity.this, CalendarBarGraph.class));
                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    startActivity(new Intent(MainActivity.this, Settings.class));
                    return true;
            }
            return false;
        }
    };


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int maxProgress;
        TextView max;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar pb = findViewById(R.id.progressBar);

        lv = findViewById(R.id.food);

        maxProgress = Settings.getCals();
        Log.d("Cals", String.valueOf(maxProgress));

        max = findViewById(R.id.max);
        Log.d("Cals", String.valueOf(max.getText()));
        max.setText(String.valueOf(maxProgress));
        //Log.d("Cals", "HERE");

        TextView tv1 = findViewById(R.id.calories);
        tv1.setText("Net Calories: " + netCalories);

        pb.setMin(0);
        pb.setMax(maxProgress);
        pb.setProgress(netCalories);



        todayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todayList);

        lv.setAdapter(todayAdapter);


        Nutrition = new HashMap<>();
        Exercise = new HashMap<>();

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button foodBtn = findViewById(R.id.NewFood);
        Button activityBtn = findViewById(R.id.NewActivity);

        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchFood.class));
            }
        });

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchEx.class));
            }
        });


        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapter, View v, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Would you like to delete this?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String rmv = (String) adapter.getItemAtPosition(position);
                        todayList.remove(rmv);
                        String[] k = rmv.split("\n");
                        String key = k[0];
                        String cal = Nutrition.get(key);
                        if(cal == null) {
                            cal = Exercise.get(key);
                            assert cal != null;
                            netCalories += Integer.parseInt(cal);
                        }
                        else
                            netCalories -= Integer.parseInt(cal);

                        TextView tv1 = findViewById(R.id.calories);
                        tv1.setText("Net Calories: " + netCalories);



                        todayAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.show();

                AlertDialog dialog = builder.create();

            }
        });

        openCSV("Nutrition.csv", Nutrition, 0, 2);
        openCSV("Exercise.csv", Exercise, 0, 1);



    }

    private void openCSV(String fileLoc, Map<String, String> m, int columnA, int columnB) {
        /* Opening CSV and parsing it into a hash table
         * FoodName => calories
         */
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(fileLoc)));
            Log.d("Food", "Starting");
            while ((line = br.readLine()) != null) {
                Log.d("Food", line);
                String[] food = line.split(",");
                Log.d("Food", food[0] + " " + food[1]);
                m.put(food[columnA].toLowerCase(), food[columnB].toLowerCase()); //maps column 0 to column 1 in csv
            }


        } catch (FileNotFoundException e) {
            Log.d("File", "COULD NOT FIND FILE!!! " + getAssets());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
