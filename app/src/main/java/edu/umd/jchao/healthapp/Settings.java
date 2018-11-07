package edu.umd.jchao.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends AppCompatActivity {

    private TextView Tdee;
    private String[] biometrics;
    EditText heightFt, heightIn, weight;
    RadioButton male, female;

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
                    startActivity(new Intent(Settings.this, Calendar.class));
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

        biometrics = new String[4];

        Tdee = (TextView) findViewById(R.id.TDEE_display);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        heightFt = findViewById(R.id.height_ft);
        heightIn = findViewById(R.id.height_in);
        weight = findViewById(R.id.weight);
        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);

        Button save = findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(getInput()) {
                    Tdee.setText(tdee(Integer.parseInt(biometrics[3]), false)+"\ncalories to maintain current weight");
                    writeCSV(biometrics, "Biometrics.csv");
                }
                else{
                    Tdee.setText("Please enter a valid height, weight, and select a gender");
                }


            }
        });
    }

    private int tdee(int gender, boolean metric){
        double height = Double.parseDouble(biometrics[0])*12 + Double.parseDouble(biometrics[1]);
        double weight = Double.parseDouble(biometrics[2]);
        int age = 20;

        if(!metric) {
            height = (height*2.54);
            weight = (weight*0.4536);
        }

        if(gender == 0) //male
            return (int)((66+(13.7*weight)+(5*height)-(6.8*age)));
            //return mf.getCheckedRadioButtonId();
        else //female
            return (int)((655+(9.6*weight)+(1.8*height)-4.7*age));
            //return mf.getCheckedRadioButtonId();
    }

    private boolean getInput(){
        biometrics[0] = heightFt.getText().toString();
        biometrics[1] = heightIn.getText().toString();
        biometrics[2] = weight.getText().toString();
        if(male.isChecked())
            biometrics[3] = 0+"";
        else
            biometrics[3] = 1+"";

        return Pattern.matches("[0-9]*", heightFt.getText()) &&
                Pattern.matches("[1-9]|1[0-1]", heightIn.getText()) &&
                Pattern.matches("[0-9]*", weight.getText());
    }

    private boolean writeCSV(String[] biometrics, String fileLoc){
        FileWriter fw = null;
        String input = "";

        if(biometrics[0] == null||biometrics[1]==null||biometrics[2]== null||biometrics[3] == null)
            return false;

        try{
            fw = new FileWriter(fileLoc);
            for(String s : biometrics){
                input += s+",";
            }
            fw.append(input);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }


    private String[] readCSV(String fileLoc) {
        /* Opening CSV and parsing into an ArrayList<String>
         */
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(fileLoc)));
            while ((line = br.readLine()) != null) {
                String[] biometrics = line.split(",");
                return biometrics;
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

}
