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
    List<Integer> yAxisData;

    List axisValues;
    List yAxisValues;

    TextView usage;
    TextView cost;

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

        axisData = new ArrayList<String>();
        axisValues = new ArrayList();
        yAxisData = new ArrayList<Integer>();

        if (isPortrait()){
            Toast.makeText(BreakdownUsage.this, "Turn for better view", Toast.LENGTH_SHORT ).show();
        }

        switch (breakdown){
            case "electricity":breakdownImage.setBackgroundResource(R.drawable.electricity);
                                bkdTitle.setText("Electricity (kWh)");
//                                breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.electric));
                                readCSV(R.raw.electricity);
                                createChart(R.color.electric, " ");
                                setTotals();
                                break;
            case "water":breakdownImage.setBackgroundResource(R.drawable.water);
                            bkdTitle.setText("Water & Sewer (kGal)");
//                            breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.water));
                            readCSV(R.raw.water);
                            createChart(R.color.water, " ");
                            setTotals();
                            break;
            case "gas":breakdownImage.setBackgroundResource(R.drawable.gas);
                        bkdTitle.setText("Natural Gas (dTh)");
//                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.gas));
                        readCSV(R.raw.gas);
                        createChart(R.color.gas, " ");
                        setTotals();
                        break;
            case "thermostat":breakdownImage.setBackgroundResource(R.drawable.thermostat);
                        bkdTitle.setText("HVAC (MMBtu)");
//                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.waste));
                        readCSV(R.raw.hvac);
                        createChart(R.color.hvac, " ");
                        setTotals();
                        break;
        }

    }

    private boolean isPortrait(){
        return (getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT);
    }

    private void readCSV(int file) {
        try {
            InputStream inputStream = getResources().openRawResource(file);
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                axisData.add(data[0]);
                yAxisData.add(Integer.parseInt(data[1]));
                totalUsage += Integer.parseInt(data[1]);
                totalCost += Double.parseDouble(data[2]);
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

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setName("Date");
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName(offset);
        data.setAxisYLeft(yAxis);

        lineChartView.setInteractive(true);
    }

    private void setTotals(){
        usage.setText("Total Consumption: " + totalUsage);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        cost.setText("Total Cost: " + formatter.format(totalCost));
    }

}
