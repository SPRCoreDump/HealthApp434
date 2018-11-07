package edu.umd.jchao.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(Calendar.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    startActivity(new Intent(Calendar.this, Calendar.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(Calendar.this, Settings.class));
                    return true;
            }
            return false;
        }
    };

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        barChart = findViewById(R.id.bargraph);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(440, 0));
        entries.add(new BarEntry(880, 1));
        entries.add(new BarEntry(660, 2));
        entries.add(new BarEntry(120, 3));
        entries.add(new BarEntry(190, 4));
        entries.add(new BarEntry(910, 5));
        entries.add(new BarEntry(690, 6));
        BarDataSet set = new BarDataSet(entries, "Calories");

        ArrayList<String> dates = new ArrayList<>();
        dates.add("Monday");
        dates.add("Tuesday");
        dates.add("Wednesday");
        dates.add("Thursday");
        dates.add("Friday");
        dates.add("Saturday");
        dates.add("Sunday");

        BarData data = new BarData(dates, set);
        barChart.setData(data);
        barChart.setDescription("");

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }
}
