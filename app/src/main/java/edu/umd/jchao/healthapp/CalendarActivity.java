package edu.umd.jchao.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CalendarView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(CalendarActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    startActivity(new Intent(CalendarActivity.this, CalendarActivity.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(CalendarActivity.this, Settings.class));
                    return true;
            }
            return false;
        }
    };

    Calendar cal = Calendar.getInstance();
    int currYear = cal.get(Calendar.YEAR);
    int currMonth = cal.get(Calendar.MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CalendarView mCalendarView = findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (currYear == year && currMonth == month)
                    startActivity(new Intent(CalendarActivity.this, CalendarBarGraph.class));
            }
        });
    }
}
