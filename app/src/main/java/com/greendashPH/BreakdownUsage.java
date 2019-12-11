package com.greendashPH;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class BreakdownUsage extends AppCompatActivity {
    String breakdown;
    ImageView breakdownImage;
    TextView bkdTitle;
    LinearLayout breakdowntitle;

    LineChartView lineChartView;

    List<String> axisData;
    List<Float> yAxisData;

    List axisValues;
    List yAxisValues;

    List yAxisLabes;

    TextView usage;
    TextView cost;
    TextView impact;
    TextView source;

    int totalUsage = 0;
    Double totalCost = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_usage);
        Intent breakdownusage = getIntent();

        getSupportActionBar().hide();
        this.breakdown = breakdownusage.getStringExtra("Breakdown");
        System.out.println(breakdown);

        breakdownImage = (ImageView) findViewById(R.id.usageimage);
        bkdTitle = findViewById(R.id.usageType);
        breakdowntitle = findViewById(R.id.breakdowntitle);

        lineChartView = findViewById(R.id.lineChart);

        usage = (TextView)findViewById(R.id.totalUsage);
        cost = (TextView)findViewById(R.id.totalCost);
        impact = (TextView)findViewById(R.id.totalImpact);
        source = (TextView)findViewById(R.id.source);

        axisData = new ArrayList<String>();
        axisValues = new ArrayList();
        yAxisData = new ArrayList<Float>();

        yAxisLabes = new ArrayList<String>();

        yAxisLabes.add("Awake");
        yAxisLabes.add("Sleep");
        yAxisLabes.add("Deep Sleep");

        if (isPortrait()){
            Toast.makeText(BreakdownUsage.this, "Turn for better view", Toast.LENGTH_SHORT ).show();
        }

        switch (breakdown){
            case "electricity":breakdownImage.setBackgroundResource(R.drawable.electricity);
                                bkdTitle.setText("Sleep Movements");
//                                breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.electric));
//                                readCSV(getFileStreamPath("trackedData.csv"));
                                readCSV();
                                createChart(R.color.electric, " ");
                                setTotals("kWh");
                                break;
            case "water":breakdownImage.setBackgroundResource(R.drawable.water);
                            bkdTitle.setText("Water & Sewer (kGal)");
//                            breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.water));
//                            readCSV(getFileStreamPath("trackedData.csv"));
                            break;
            case "gas":breakdownImage.setBackgroundResource(R.drawable.gas);
                        bkdTitle.setText("Natural Gas (DTh)");
//                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.gas));
//                        readCSV(getFileStreamPath("trackedData.csv"));
                        createChart(R.color.gas, " ");
                        setTotals("DTh");
                        break;
            case "thermostat":breakdownImage.setBackgroundResource(R.drawable.thermostat);
                        bkdTitle.setText("HVAC (MMBtu)");
//                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.waste));
//                        readCSV(getFileStreamPath("trackedData.csv"));
                        createChart(R.color.hvac, " ");
                        setTotals("MMBtu");
                        impact.setText("");
                        break;
        }

    }

    private boolean isPortrait(){
        return (getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT);
    }

    private void readCSV() {
        try {
            FileInputStream inputStream = openFileInput("trackedData.csv");
//            InputStream inputStream = getResources().openRawResource(file);
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Float point = Float.parseFloat(data[0]);
                axisData.add(data[1]);
                yAxisData.add(point);
                totalUsage += 1;
                if (point < 2) {
                    totalCost += 1;
                }
//                totalCost += Double.parseDouble(data[2]);
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

//        Axis axis = new Axis();
//        axis.setValues(axisValues);
//        axis.setName("Date");
//        data.setAxisXBottom(axis);
//
//        Axis yAxis = new Axis();
//        yAxis.setName(offset);
//        data.setAxisYLeft(yAxis);

        lineChartView.setInteractive(true);
    }

    private void setTotals(String units){
        int totalMinutes = totalUsage / 60;
        double totalHours = totalMinutes / 60;
        totalHours = Math.floor(totalHours);
        int hours = (int)totalHours;
        int minutes = totalMinutes % 60;
        usage.setText("" + hours + "h " + minutes + "m in bed" );
        double percentage = (totalCost / totalUsage) * 100;
        int quality = (int) percentage;
        cost.setText("" + quality + "% sleep quality");
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        cost.setText("          " + formatter.format(totalCost));
    }

}
