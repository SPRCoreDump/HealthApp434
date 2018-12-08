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
import android.widget.TextView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFood extends AppCompatActivity {

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
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(SearchFood.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    //mTextMessage.setText("CalendarBarGraph");
                    startActivity(new Intent(SearchFood.this, CalendarBarGraph.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(SearchFood.this, Settings.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mTextMessage = findViewById(R.id.message);
        lv = findViewById(R.id.results);

        itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);

        lv.setAdapter(itemsAdapter);



        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.foodSearch);
        // Assumes current activity is the searchable activity
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                String toAdd = (String) adapter.getItemAtPosition(position);
                MainActivity.todayList.add(toAdd);
                //takes you back to home page after adding an item
                String[] spl = toAdd.split("\n");
                MainActivity.netCalories += Integer.parseInt(Objects.requireNonNull(MainActivity.Nutrition.get(spl[0])));
                startActivity(new Intent(SearchFood.this, MainActivity.class));
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    results.clear();
                    itemsAdapter.notifyDataSetChanged();
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


        for(String s : MainActivity.Nutrition.keySet()) {
            if (s.contains(q)) {
                results.add(s + "\n" + MainActivity.Nutrition.get(s) + " Calories");
            }
            else
                Log.d("sss", s);
        }

        if(results.isEmpty())
            results.add("No results found.");
    }

}
