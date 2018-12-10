package edu.umd.jchao.healthapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Pattern;

public class Settings extends AppCompatActivity {

    private TextView Tdee;
    private TextView maxCals;
    private String[] biometrics;
    private static int cals = 2000;
    EditText heightFt, heightIn, weight, ageInput;
    RadioButton male, female, lose, maintain, gain;

    //buncha public variables that should be accessed from the outside. There are getters and setters
    public static int heightCmOrFt = 0,
            height2 = 0,
            gender = 0,
            age = 0;
    public static double weightLbOrKg = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(Settings.this, MainActivity.class));
                    return true;
                case R.id.navigation_cal:
                    //mTextMessage.setText("Calednar");
                    startActivity(new Intent(Settings.this, CalendarActivity.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(Settings.this, Settings.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        biometrics = new String[5];

        Tdee = findViewById(R.id.TDEE_display);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        heightFt = findViewById(R.id.height_ft);
        heightIn = findViewById(R.id.height_in);
        weight = findViewById(R.id.weight);
        ageInput = findViewById(R.id.age);
        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);
        lose = findViewById(R.id.lose_weight);
        maintain = findViewById(R.id.maintain_weight);
        gain = findViewById(R.id.gain_weight);

        Button save = findViewById(R.id.save_button);

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                if (getInput()) {
                    int theGender = Integer.parseInt(biometrics[3]);
                    String lineChange = "maintain current";
                    cals = tdee(theGender, false);

                    if (lose.isChecked()) {
                        cals *= 0.88;
                        lineChange = "lose";
                    } else if (gain.isChecked()) {
                        cals *= 1.1;
                        lineChange = "gain";
                    }
                    Tdee.setText("Consume " + cals + "\ncalories to " + lineChange + " weight");

                    writeCSV(biometrics, "Biometrics.csv");
                } else {
                    Tdee.setText("Please enter a valid height, weight, age, and select a gender");
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private int tdee(int gender, boolean metric) {
        double height = Double.parseDouble(biometrics[0]) * 12 + Double.parseDouble(biometrics[1]);
        double weight = Double.parseDouble(biometrics[2]);
        int age = Integer.parseInt(biometrics[4]);
        double ret;

        if (!metric) {
            height = (height * 2.54);
            weight = (weight * 0.4536);
        }

        if (gender == 0) //male
            ret = (66 + (13.7 * weight) + (5 * height) - (6.8 * age));
        else //female
            ret = (655 + (9.6 * weight) + (1.8 * height) - 4.7 * age);

        return (int) ret;
    }

    private boolean getInput() {
        biometrics[0] = heightFt.getText().toString();
        biometrics[1] = heightIn.getText().toString();
        biometrics[2] = weight.getText().toString();
        biometrics[4] = ageInput.getText().toString();
        if (male.isChecked())
            biometrics[3] = 0 + "";
        else
            biometrics[3] = 1 + "";

        if (biometrics[0] == null || biometrics[1] == null || biometrics[2] == null || biometrics[4] == null)
            return false;

        return Pattern.matches("[0-9]+", heightFt.getText()) &&
                Pattern.matches("[1-9]|1[0-1]", heightIn.getText()) &&
                Pattern.matches("([0-9]+|.)+", weight.getText()) &&
                Pattern.matches("[0-9]+", ageInput.getText());
    }

    private void writeCSV(String[] biometrics, String fileLoc) {
        FileWriter fw;
        StringBuilder input = new StringBuilder();

        try {
            fw = new FileWriter(fileLoc);
            for (String s : biometrics) {
                input.append(s).append(",");
            }
            fw.append(input.toString());
            heightCmOrFt = Integer.parseInt(biometrics[0]);
            height2 = Integer.parseInt(biometrics[1]);
            weightLbOrKg = Double.parseDouble(biometrics[2]);
            gender = Integer.parseInt(biometrics[3]);
            age = Integer.parseInt(biometrics[4]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String[] readCSV(String fileLoc) {
        /* Opening CSV and parsing into an ArrayList<String>
         */
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(fileLoc)));
            if ((line = br.readLine()) != null) {
                return line.split(",");
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
        return null;
    }

    public static int getHeightCmOrFt() {
        return heightCmOrFt;
    }

    public static int getHeight2() {
        return height2;
    }

    public static int getCals() {
        return cals;
    }

    public static double getWeightLbOrKg() {
        return weightLbOrKg;
    }

    public static int getAge() {
        return age;
    }

    public static int getGender() {
        return gender;
    }
}
