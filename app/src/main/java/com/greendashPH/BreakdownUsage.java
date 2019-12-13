package com.greendashPH;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class BreakdownUsage extends AppCompatActivity {

    BottomNavigationView bottom_menu;

    ImageView breakdownImage;

    LineChartView lineChartView;

    List<String> axisData;
    List<Float> yAxisData;

    List axisValues;
    List yAxisValues;

    TextView time;
    TextView quality;

    int totalPoints = 0;
    double goodPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_usage);

        bottom_menu = findViewById(R.id.bottom_navigation);
        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().hide();

        breakdownImage = (ImageView) findViewById(R.id.usageimage);

        lineChartView = findViewById(R.id.lineChart);

        time = (TextView)findViewById(R.id.sleepTime);
        quality = (TextView)findViewById(R.id.sleepQuality);

        axisData = new ArrayList<String>();
        axisValues = new ArrayList();
        yAxisData = new ArrayList<Float>();

        if (isPortrait()){
            Toast.makeText(BreakdownUsage.this, "Turn for better view", Toast.LENGTH_SHORT ).show();
            findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        }

        breakdownImage.setBackgroundResource(R.drawable.sleeping_mask);
        readCSV();
        createChart(R.color.blue, " ");
        setTotals();

        bottom_menu.setSelectedItemId(R.id.action_history);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_timer:
                    Intent r = new Intent(BreakdownUsage.this, Alarm.class);
                    startActivity(r);
                    finish();
                    break;
//                case R.id.action_daily:
//                    Intent s = new Intent(BreakdownUsage.this, SleepTrack.class);
//                    startActivity(s);
//                    finish();
//                    break;
                case R.id.action_history:
//                    Intent t = new Intent(BreakdownUsage.this, MainActivity.class);
//                    startActivity(t);
//                    finish();
                    break;
                case R.id.action_setting:
                    Intent u = new Intent(BreakdownUsage.this, SettingsActivity.class);
                    startActivity(u);
                    return false;
            }
            return true;
        }
    };

    private boolean isPortrait(){
        return (getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT);
    }

    private void readCSV() {
        try {
            FileInputStream inputStream = openFileInput("trackedData.csv");
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Float point = Float.parseFloat(data[0]);
                axisData.add(data[1]);
                yAxisData.add(point);
                totalPoints += 1;
                if (point < 0.8) {
                    goodPoints += 1;
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createChart(int color, String offset) {
        yAxisValues = new ArrayList();

        Line line = new Line(yAxisValues);
        line.setColor(ContextCompat.getColor(this, color));

        for(int i = 0; i < axisData.size(); i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i)));
        }

        for (int i = 0; i < yAxisData.size(); i++){
            yAxisValues.add(new PointValue(i, yAxisData.get(i)));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        lineChartView.setLineChartData(data);

        lineChartView.setInteractive(true);
    }

    private void setTotals(){
        int totalMinutes = totalPoints / 60;
        double totalHours = totalMinutes / 60;
        totalHours = Math.floor(totalHours);
        int hours = (int)totalHours;
        int minutes = totalMinutes % 60;
        time.setText("" + hours + "h " + minutes + "m in bed" );

        double totalPercentage = (goodPoints / totalPoints) * 100;
        int percentage = (int)totalPercentage;
        quality.setText("" + percentage+ "% sleep quality");
    }

}
