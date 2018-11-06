package edu.umd.jchao.healthapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class search2 extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView lv;
    private SearchView s;
    private ArrayList<String> results = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_cal:
                    mTextMessage.setText("Calednar");
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(search2.this, Settings.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lv = findViewById(R.id.exResults);
        s = findViewById(R.id.exSearch);

        itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);

        lv.setAdapter(itemsAdapter);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.exSearch);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                String toAdd = (String) adapter.getItemAtPosition(position);
                MainActivity.todayList.add(toAdd);
                //takes you back to home page after adding an item
                String[] spl = toAdd.split("\n");
                MainActivity.netCalories -= Integer.parseInt(MainActivity.Exercise.get(spl[0]));
                startActivity(new Intent(search2.this, MainActivity.class));
            }
        });

        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
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
            if (s.contains(q)) {
                results.add(s + "\n" + MainActivity.Exercise.get(s) + " Calories");
            }
            else
                Log.d("sss", s);
        }
    }

}
