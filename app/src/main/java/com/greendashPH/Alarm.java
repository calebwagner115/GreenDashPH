package com.greendashPH;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

public class Alarm extends AppCompatActivity implements SensorEventListener {
    BottomNavigationView bottom_menu;
    int perm=-1;
    String status;
    PendingIntent pendingIntent;
    Button SetAlarm;
    AlarmManager alarmManager;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdate;
    private String row;
    Button Stop;

    FileOutputStream trackedData;
    OutputStreamWriter osw;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("status", status); // Saving the Variable theWord

        Bundle b = new Bundle();
        pendingIntent.describeContents();
        //outState.putBundle("PendingIntent", );// Saving the ArrayList fiveDefns
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

//        theWord = savedInstanceState.getString("theWord"); // Restoring theWord
//        fiveDefns = savedInstanceState.getStringArrayList("fiveDefns"); //Restoring fiveDefns
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        status = "off";
        requestPermissions(new String[]{Manifest.permission.WAKE_LOCK}, 1);


        if (checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WAKE_LOCK}, 1);
        }


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        bottom_menu = findViewById(R.id.bottom_navigation);
        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        textAlarmPrompt = (TextView) findViewById(R.id.alarmprompt);
        SetAlarm = (Button) findViewById(R.id.startAlaram);
        bottom_menu.setSelectedItemId(R.id.action_timer);
        mLastUpdate = System.currentTimeMillis();
        mSensorManager = (SensorManager)
                getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Stop= findViewById(R.id.stopalarm);
        Stop.setEnabled(false);

        SetAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SetAlarm.getText().equals("Set Alarm Time")) {
                    Stop.setEnabled(true);
                    textAlarmPrompt.setText("");
                    openTimePickerDialog(false);

                } else {
                    Stop.setEnabled(false);
                    SetAlarm.setText("Set Alarm Time");
                    alarmManager.cancel(pendingIntent);
                    textAlarmPrompt.setText("No Alarm Set");
                    status = "off";
                    findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            //TODO Open setings
            Intent setting = new Intent(Alarm.this, SettingsActivity.class);
            startActivity(setting);
        }
        if (item.getItemId() == R.id.logout) {
            Intent login = new Intent(Alarm.this, LoginScreen.class);
            startActivity(login);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                    perm=1;
                    Toast.makeText(getApplicationContext(),"You Access", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"You declined to allow the app to access your camera", Toast.LENGTH_SHORT).show();

                }
                break;

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
                    break;
//                case R.id.action_daily:
//                    Intent s = new Intent(Alarm.this, SleepTrack.class);
//                    startActivity(s);
//                    finish();
//                    break;
                case R.id.action_history:
                    Intent t = new Intent(Alarm.this, BreakdownUsage.class);
                    t.putExtra("Breakdown","electricity");
                    startActivity(t);
                    finish();
                    break;
                case R.id.action_setting:
                    Intent u = new Intent(Alarm.this, SettingsActivity.class);
                    startActivity(u);
                    return false;
            }
            return true;
        }
    };

    public void setAlarm(View v){

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        Stop.setEnabled(true);
        if(status.equals("off")){
            intent.putExtra("status", "on");

            status="on";
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(System.currentTimeMillis());
            time.add(Calendar.SECOND, 5);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

        } else{
            Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
            intent.putExtra("status", "off");
            status="off";
            sendBroadcast(intent);
            Stop.setEnabled(false);
        }

    }

    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    TextView textAlarmPrompt;
    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(Alarm.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(this, AlarmReceiver.class);
        if(status.equals("off")&&SetAlarm.getText().equals("Set Alarm Time")){
            Stop.setEnabled(true);
            intent.putExtra("status", "on");
            status="on";
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(System.currentTimeMillis());
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
            String s = new SimpleDateFormat("hh:mm a").format(targetCal.getTimeInMillis());
            textAlarmPrompt.setText("\n" +  "Current Alarm: "+ s + "\n");
            SetAlarm.setText("Cancel Alarm");
            findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
            start();
        }

    }

    public void stopAlarm(View v){
        if(status.equals("on")){
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("status", "off");
            sendBroadcast(intent);
            status="off";
            stop();
            Stop.setEnabled(false);
            findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        }
    }

    public void start() {
        try {
            trackedData = openFileOutput("trackedData.csv", Context.MODE_PRIVATE);

            osw = new OutputStreamWriter(trackedData);

        } catch (Exception e) {
            Toast.makeText(this, "Could not start sleep tracker!", Toast.LENGTH_SHORT).show();
        }
    }


    public void stop() {

        try {
            osw.flush();
            osw.close();

            Intent breakdownIntent = new Intent(this, BreakdownUsage.class);
            breakdownIntent.putExtra("Breakdown","electricity");
            startActivity(breakdownIntent);

        } catch (Exception e) {
            Toast.makeText(this, "Could not stop data recording", Toast.LENGTH_SHORT).show();
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
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }


}
