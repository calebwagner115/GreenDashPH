package com.greendashPH;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class SleepTrack extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdate;
    private String row;

    FileOutputStream trackedData;
    OutputStreamWriter osw;
    BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_track);
        mLastUpdate = System.currentTimeMillis();
        mSensorManager = (SensorManager)
                getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        bottom_menu = findViewById(R.id.bottom_navigation);
        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom_menu.setSelectedItemId(R.id.action_daily);
    }

    private void updateNavigationBarState(int actionId){
        Menu menu = bottom_menu.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
            //item.ch
            System.out.println(""+(item.getItemId() == actionId));
        }
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
                    Intent r = new Intent(SleepTrack.this, Alarm.class);
                    startActivity(r);

                     updateNavigationBarState(R.id.action_timer);
//                    finish();
                    break;
                case R.id.action_daily:
//                    Intent s = new Intent(SleepTrack.this, SleepTrack.class);
//                    startActivity(s);
                    //updateNavigationBarState(R.id.action_daily);

                    //finish();
                    break;
                case R.id.action_history:
                    Intent t = new Intent(SleepTrack.this, BreakdownUsage.class);
                    t.putExtra("Breakdown","electricity");
                    startActivity(t);
                    finish();
                    break;
                case R.id.action_setting:
                    Intent u = new Intent(SleepTrack.this, SettingsActivity.class);
                    startActivity(u);
                    //finish();
                    return false;

            }
           return true;
        }
    };

    public void onStart(View view) {
        try {
            trackedData = openFileOutput("trackedData.csv", Context.MODE_PRIVATE);

            osw = new OutputStreamWriter(trackedData);

        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Could not start sleep tracker!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop(View view) {
        try {
            osw.flush();
            osw.close();

            Intent breakdownIntent = new Intent(this, BreakdownUsage.class);
            breakdownIntent.putExtra("Breakdown","electricity");
            startActivity(breakdownIntent);

        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Could not stop data recording", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime = System.currentTimeMillis();
            if (actualTime  - mLastUpdate > 1000)  {
                mLastUpdate = actualTime;
                float x = event.values[0],  y = event.values[1],
                        z = event.values[2];
                double gravity = 9.81;
                float move = Math.abs(x) + Math.abs(y) + Math.abs(z) - (float)gravity;
                move = Math.abs(move);

                row = "" + move + "," + mLastUpdate + "\n";

                try {
                    osw.write(row);
                    System.out.println(row);

                } catch (Exception e) { }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    protected void onResume()  {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
}

