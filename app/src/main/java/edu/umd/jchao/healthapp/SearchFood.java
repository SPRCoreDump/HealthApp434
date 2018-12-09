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
    int portions;

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

                final String toAdd = (String) adapter.getItemAtPosition(position);
                //takes you back to home page after adding an item
                String[] spl = toAdd.split("\n");
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchFood.this);
                builder.setMessage("How many servings of this food did you have?");
                final EditText input = new EditText(SearchFood.this);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        portions = Integer.parseInt(input.getText().toString());
                        String[] spl = toAdd.split("\n");
                        Log.d("toAdd",  spl[0]);
                        int totalCals = Integer.parseInt(Objects.requireNonNull(MainActivity.Nutrition.get(spl[0]))) * portions;

                        // format (name, description, amount, calories, image)
                        MainActivity.todayList.add(spl[0] + ", ," + portions + "," + totalCals + "," + "Food");
                        //takes you back to home page after adding an item

                        MainActivity.netCalories += totalCals;

                        startActivity(new Intent(SearchFood.this, MainActivity.class));

                    }
                });
                builder.show();
                AlertDialog dialog = builder.create();
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
                results.add(s + "\n" + MainActivity.Nutrition.get(s) + " Calories per Serving");
            }
            else
                Log.d("sss", s);
        }

        if(results.isEmpty())
            results.add("No results found.");
    }

}
