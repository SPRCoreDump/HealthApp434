package edu.umd.jchao.healthapp;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class SearchEx extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView lv;
    private SearchView s;
    private ArrayList<String> results = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;
    private int mins;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(SearchEx.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    //mTextMessage.setText("CalendarBarGraph");
                    startActivity(new Intent(SearchEx.this, CalendarBarGraph.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(SearchEx.this, Settings.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercise);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lv = findViewById(R.id.exResults);

        itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);

        lv.setAdapter(itemsAdapter);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.exSearch);
        // Assumes current activity is the searchable activity
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                String temp = (String) adapter.getItemAtPosition(position);
                final String toAdd = temp.split("\n")[0];

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 8;
                lp.rightMargin = 8;

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchEx.this);
                builder.setMessage("How many minutes did you spend doing this activity?");
                final EditText input = new EditText(SearchEx.this);
                input.setLayoutParams(lp);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mins = Integer.parseInt(input.getText().toString());
                        MainActivity.todayList.add(toAdd + " \n" + mins + " Minutes");
                        //takes you back to home page after adding an item
                        String[] spl = toAdd.split("\n");
                        MainActivity.netCalories -= (Integer.parseInt(Objects.requireNonNull(MainActivity.Exercise.get(spl[0]))) / 15) * mins;
                        startActivity(new Intent(SearchEx.this, MainActivity.class));

                    }
                });
                builder.show();
                AlertDialog dialog = builder.create();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    results.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                Log.d("sss", "Getting results " + query);
                doMySearch(query);
                Log.d("sss", results.toString());
                itemsAdapter.notifyDataSetChanged();
                return false;
            }

        });



    }

    private void doMySearch(String q) {
        results.clear();

        for(String s : MainActivity.Exercise.keySet()) {
            String[] splited = s.split(" ");

            if (splited[0].contains(q)) {
                int perMin = Integer.parseInt(MainActivity.Exercise.get(s)) / 15;
                results.add(s + "\n" + perMin + " Calories per minute");
            }
            else
                Log.d("sss", s);
        }

        if(results.isEmpty())
            results.add("No results found.");
    }

}
