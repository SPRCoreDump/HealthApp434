package edu.umd.jchao.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarBarGraph extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(CalendarBarGraph.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    startActivity(new Intent(CalendarBarGraph.this, CalendarActivity.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(CalendarBarGraph.this, Settings.class));
                    return true;
            }
            return false;
        }
    };

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_bar_graph);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        barChart = findViewById(R.id.bargraph);

        createBarGraph();
    }

    @Override
    protected void onResume() {
        super.onResume();

        createBarGraph();
    }

    public void createBarGraph() {
        Calendar calendar = Calendar.getInstance();

        int cal = MainActivity.netCalories;
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            if (day - 1 == i)
                entries.add(new BarEntry(cal, i));
            else
                entries.add(new BarEntry(0, i));
        }

        BarDataSet set = new BarDataSet(entries, "Calories");

        ArrayList<String> dates = new ArrayList<>();

        dates.add("Sun");
        dates.add("Mon");
        dates.add("Tue");
        dates.add("Wed");
        dates.add("Thu");
        dates.add("Fri");
        dates.add("Sat");

        BarData data = new BarData(dates, set);

        barChart.setData(data);
        barChart.setDescription("");

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        float buffer = 50 > (cal * .05f) ? 50 : (cal * .05f);

        if (cal < 0) {
            leftAxis.setAxisMinValue(cal - buffer);
            rightAxis.setAxisMinValue(cal - buffer);
        } else {
            leftAxis.setAxisMinValue(0);
            rightAxis.setAxisMinValue(0);
        }

        if (cal > Settings.getCals()) {
            leftAxis.setAxisMaxValue(cal + buffer);
            rightAxis.setAxisMaxValue(cal + buffer);
        } else {
            leftAxis.setAxisMaxValue(Settings.getCals() + (Settings.getCals() * .05f));
            rightAxis.setAxisMaxValue(Settings.getCals() + (Settings.getCals() * .05f));
        }

        LimitLine ll = new LimitLine(Settings.getCals(), "Goal: " + Integer.toString(Settings.getCals()));
        ll.setLineWidth(2f);
        ll.setTextSize(12f);

        leftAxis.addLimitLine(ll);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DAY_OF_WEEK);
                boolean switchActivity = false;

                int pos1 = e.toString().indexOf("xIndex: ");
                int pos2 = e.toString().indexOf("(sum): ");
                String day = e.toString().substring(pos1 + 8, pos1 + 9);
                String cal = e.toString().substring(pos2 + 7);

                if (Integer.toString(date - 1).equals(day))
                    switchActivity = true;

                switch (day) {
                    case "0":
                        day = "Sunday";
                        break;
                    case "1":
                        day = "Monday";
                        break;
                    case "2":
                        day = "Tuesday";
                        break;
                    case "3":
                        day = "Wednesday";
                        break;
                    case "4":
                        day = "Thursday";
                        break;
                    case "5":
                        day = "Friday";
                        break;
                    case "6":
                        day = "Saturday";
                        break;
                }

                Toast toast = Toast.makeText(getApplicationContext(), day + "\n" + "Net Calories: " + cal, Toast.LENGTH_SHORT);
                toast.show();

                if (switchActivity)
                    startActivity(new Intent(CalendarBarGraph.this, MainActivity.class));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}
